package nl.jworks.grails.plugin.fitnesse;


import fitnesse.slim.Converter
import fitnesse.slim.FixtureMethodExecutor
import fitnesse.slim.Library
import fitnesse.slim.LibraryMethodExecutor
import fitnesse.slim.MethodExecutionResult
import fitnesse.slim.MethodExecutionResults
import fitnesse.slim.MethodExecutor
import fitnesse.slim.Slim
import fitnesse.slim.SlimError
import fitnesse.slim.SlimServer
import fitnesse.slim.StatementExecutorInterface
import fitnesse.slim.SystemUnderTestMethodExecutor
import fitnesse.slim.VariableStore
import fitnesse.slim.converters.BooleanArrayConverter
import fitnesse.slim.converters.BooleanConverter
import fitnesse.slim.converters.CharConverter
import fitnesse.slim.converters.DateConverter
import fitnesse.slim.converters.DoubleArrayConverter
import fitnesse.slim.converters.DoubleConverter
import fitnesse.slim.converters.IntConverter
import fitnesse.slim.converters.IntegerArrayConverter
import fitnesse.slim.converters.ListConverter
import fitnesse.slim.converters.MapEditor
import fitnesse.slim.converters.StringArrayConverter
import fitnesse.slim.converters.StringConverter
import fitnesse.slim.converters.VoidConverter
import java.beans.PropertyEditorManager
import java.lang.reflect.Constructor
import org.codehaus.groovy.grails.commons.ApplicationHolder
import java.lang.reflect.InvocationTargetException
import org.apache.commons.lang.StringUtils
import grails.util.GrailsUtil
import org.springframework.beans.factory.BeanCreationException

/**
 * This is the API for executing a SLIM statement. This class should not know
 * about the syntax of a SLIM statement.
 */

public class GrailsStatementExecutor implements StatementExecutorInterface {

    private Map<String, Object> instances = new HashMap<String, Object>();
    private List<Library> libraries = new ArrayList<Library>();

    private List<MethodExecutor> executorChain = new ArrayList<MethodExecutor>();

    private VariableStore variables = new VariableStore();
    private List<String> paths = new ArrayList<String>();

    private boolean stopRequested = false;

    public GrailsStatementExecutor() {
        Slim.addConverter(void.class, new VoidConverter());
        Slim.addConverter(String.class, new StringConverter());
        Slim.addConverter(int.class, new IntConverter());
        Slim.addConverter(double.class, new DoubleConverter());
        Slim.addConverter(Integer.class, new IntConverter());
        Slim.addConverter(Double.class, new DoubleConverter());
        Slim.addConverter(char.class, new CharConverter());
        Slim.addConverter(boolean.class, new BooleanConverter());
        Slim.addConverter(Boolean.class, new BooleanConverter());
        Slim.addConverter(Date.class, new DateConverter());
        Slim.addConverter(List.class, new ListConverter());
        Slim.addConverter(Integer[].class, new IntegerArrayConverter());
        Slim.addConverter(int[].class, new IntegerArrayConverter());
        Slim.addConverter(String[].class, new StringArrayConverter());
        Slim.addConverter(boolean[].class, new BooleanArrayConverter());
        Slim.addConverter(Boolean[].class, new BooleanArrayConverter());
        Slim.addConverter(double[].class, new DoubleArrayConverter());
        Slim.addConverter(Double[].class, new DoubleArrayConverter());

        Slim.addConverter(Object.class, new ObjectConverter());

        PropertyEditorManager.registerEditor(Map.class, MapEditor.class);

        executorChain.add(new GroovyMethodExecutor(instances));
        executorChain.add(new FunctionAsPropertyMethodExecutor(instances));
        executorChain.add(new GroovyQuotedMethodNameMethodExecutor(instances));
        executorChain.add(new SystemUnderTestMethodExecutor(instances));
        executorChain.add(new LibraryMethodExecutor(libraries));
    }

    public void setVariable(String name, Object value) {
        variables.setSymbol(name, value);
    }

    public Object addPath(String path) {
        paths.add(path);
        return "OK";
    }

    public Object getInstance(String instanceName) {
        Object instance = instances.get(instanceName);
        if (instance != null) {
            return instance;
        }

        for (Library library: libraries) {
            if (library.instanceName.equals(instanceName)) {
                return library.instance;
            }
        }
        throw new SlimError(String.format("message:<<NO_INSTANCE %s.>>", instanceName));
    }

    public Object create(String instanceName, String className, Object[] args) {
        String replacedClassName = variables.replaceSymbolsInString(className) + "Fixture";
        // This is needed for fixture names which are not camel cased, like 'Calculate'.
        if (!replacedClassName.contains(".")) {
            replacedClassName = StringUtils.capitalize(replacedClassName)
        }

        try {
            Object instance
            Class<?> clazz = searchPathsForClass(replacedClassName)
            Object[] argsWithValues = replaceSymbols(args);
            if (isFixtureClass(clazz)) {
                instance = getFixtureBean(clazz, argsWithValues);
            } else {
                instance = createInstanceOfConstructor(clazz, argsWithValues);
            }

            if (isLibrary(instanceName)) {
                libraries.add(new Library(instanceName, instance));
            } else {
                instances.put(instanceName, instance);
            }
            return "OK";
        } catch (SlimError e) {
            return exceptionToString(e);
        } catch (IllegalArgumentException e) {
            return couldNotInvokeConstructorException(replacedClassName, args);
        } catch (InvocationTargetException e) {
            if (isStopTest(e.cause)) {
                return exceptionToString(e.cause);
            } else {
                return couldNotInvokeConstructorException(replacedClassName, args);
            }
        } catch (Throwable e) {
            return exceptionToString(e);
        }
    }

    private boolean isStopTest(Throwable throwable) {
        return throwable.getClass().toString().contains("StopTest")
    }

    private boolean isLibrary(String instanceName) {
        return instanceName.startsWith("library");
    }

    private String couldNotInvokeConstructorException(String className, Object[] args) {
        return exceptionToString(new SlimError(String.format("message:<<COULD_NOT_INVOKE_CONSTRUCTOR %s[%d]>>", className, args.length)));
    }

    private boolean isFixtureClass(Class<?> clazz) {
        return ApplicationHolder.application.isFitnesseFixtureClass(clazz)
    }

    private Object getFixtureBean(Class<?> clazz, Object[] args) {
        def context = ApplicationHolder.getApplication().mainContext
        String beanName = StringUtils.uncapitalize(clazz.simpleName)
        try {
            return context.autowireCapableBeanFactory.getBean(beanName, *args)
        } catch (BeanCreationException e) {
            throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s[%d]>>", clazz.name, args.length));
        }
    }

    private Object createInstanceOfConstructor(Class<?> k, Object[] args) throws Exception {
        Constructor<?> constructor = getConstructor(k.getConstructors(), args);
        if (constructor == null)
            throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s[%d]>>", k.name, args.length));

        def instance = constructor.newInstance(GroovyConverterSupport.convertArgs(args, constructor.getParameterTypes()))

        return instance;
    }


    private Class<?> searchPathsForClass(String className) {
        Class<?> k = getClass(className);
        if (k != null)
            return k;
        List<String> reversedPaths = new ArrayList<String>(paths);
        Collections.reverse(reversedPaths);
        for (String path: reversedPaths) {
            k = getClass(path + "." + className);
            if (k != null)
                return k;
        }
        throw new SlimError(String.format("message:<<NO_CLASS %s>>", className));
    }

    private Class<?> getClass(String className) {
        try {
            // Load from the Grails application so all classes are in the latest version
            ApplicationHolder.application.classLoader.loadClass(className)
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Constructor<?> getConstructor(Constructor<?>[] constructors, Object[] args) {
        for (Constructor<?> constructor: constructors) {
            Class<?>[] arguments = constructor.getParameterTypes();
            if (arguments.length == args.length)
                return constructor;
        }
        return null;
    }

    public Object call(String instanceName, String methodName, Object... args) {
        try {
            MethodExecutionResults results = new MethodExecutionResults();
            Object[] replacedArgs = replaceSymbols(args)
            for (int i = 0; i < executorChain.size(); i++) {
                MethodExecutionResult result = executorChain.get(i).execute(instanceName, methodName, replacedArgs);
                if (result.hasResult()) {
                    return result.returnValue();
                }
                results.add(result);
            }
            return results.returnValue();
        } catch (Throwable e) {
            return exceptionToString(e);
        }
    }

    private Object[] replaceSymbols(Object[] args) {
        return variables.replaceSymbols(args);
    }

    private String exceptionToString(Throwable unsanitizedException) {
        // TODO: make configurable
        Throwable exception = GrailsUtil.sanitize(unsanitizedException)

        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        exception.printStackTrace(pw);
        if (isStopTest(exception)) {
            stopRequested = true;
            return SlimServer.EXCEPTION_STOP_TEST_TAG + stringWriter.toString();
        } else {
            return SlimServer.EXCEPTION_TAG + stringWriter.toString();
        }
    }

    public boolean stopHasBeenRequested() {
        return stopRequested;
    }

    public void reset() {
        stopRequested = false;
    }

}
