package nl.jworks.fitnesse


import fitnesse.slim.converters.*;

import java.beans.PropertyEditorManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.*
import fitnesse.slim.*
import java.lang.reflect.InvocationTargetException

/**
 * This is the API for executing a SLIM statement. This class should not know
 * about the syntax of a SLIM statement.
 */

public class ProtectedStatementExecutor implements StatementExecutorInterface {

  public static final String SLIM_HELPER_LIBRARY_INSTANCE_NAME = "SlimHelperLibrary";

  protected Map<String, Object> instances = new HashMap<String, Object>();
  protected List<Library> libraries = new ArrayList<Library>();

  protected List<MethodExecutor> executorChain = new ArrayList<MethodExecutor>();

  protected VariableStore variables = new VariableStore();
  protected List<String> paths = new ArrayList<String>();

  protected boolean stopRequested = false;
  protected String lastActor;

  public ProtectedStatementExecutor() {
      addConverters()
      registerEditors()
      addMethodExecutors()
      addSlimHelperLibraryToLibraries();
  }
    
  protected void addConverters() {
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
  }

  protected void registerEditors() {
      PropertyEditorManager.registerEditor(Map.class, MapEditor.class);
  }

  protected void addMethodExecutors() {
      executorChain.add(new FixtureMethodExecutor(instances));
      executorChain.add(new SystemUnderTestMethodExecutor(instances));
      executorChain.add(new LibraryMethodExecutor(libraries));
  }

  protected void addSlimHelperLibraryToLibraries() {
    SlimHelperLibrary slimHelperLibrary = new SlimHelperLibrary();
   // slimHelperLibrary.setStatementExecutor(this);
    libraries.add(new Library(SLIM_HELPER_LIBRARY_INSTANCE_NAME, slimHelperLibrary));
  }

  public void setVariable(String name, Object value) {
    variables.setSymbol(name, new MethodExecutionResult(value, Object.class));
  }

  protected void setVariable(String name, MethodExecutionResult value) {
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

    for (Library library : libraries) {
      if (library.instanceName.equals(instanceName)) {
        return library.instance;
      }
    }
    throw new SlimError(String.format("message:<<NO_INSTANCE %s.>>", instanceName));
  }

  public Converter getConverter(Class<?> k) {
    return ConverterSupport.getConverter(k);
  }

  public Object create(String instanceName, String className, Object[] args) {
    try {
      if (hasStoredActor(className)) {
        addToInstancesOrLibrary(instanceName, getStoredActor(className));
      } else {
        String replacedClassName = variables.replaceSymbolsInString(className);
        Object instance = createInstanceOfConstructor(replacedClassName, replaceSymbols(args));
        addToInstancesOrLibrary(instanceName, instance);
      }
      return "OK";
    } catch (SlimError e) {
      return couldNotInvokeConstructorException(className, args);
    } catch (IllegalArgumentException e) {
      return couldNotInvokeConstructorException(className, args);
    } catch (InvocationTargetException e) {
        if (isStopTest(e.cause)) {
            return exceptionToString(e.cause);
        } else {
            return couldNotInvokeConstructorException(className, args);
        }
    } catch (Throwable e) {

      return exceptionToString(e);
    }
  }

  protected void addToInstancesOrLibrary(String instanceName, Object instance) {
    if (isLibrary(instanceName)) {
      libraries.add(new Library(instanceName, instance));
    } else {
      setInstance(instanceName, instance);
    }
  }

  public void setInstance(String instanceName, Object instance) {
    instances.put(instanceName, instance);
  }

  protected boolean hasStoredActor(String nameWithDollar) {
    if (!variables.containsValueFor(nameWithDollar)) {
      return false;
    }
    Object potentialActor = getStoredActor(nameWithDollar);
    return potentialActor != null && !(potentialActor instanceof String);
  }

  protected Object getStoredActor(String nameWithDollar) {
    return variables.getStored(nameWithDollar);
  }

  protected boolean isLibrary(String instanceName) {
    return instanceName.startsWith("library");
  }

  protected String couldNotInvokeConstructorException(String className, Object[] args) {
    return exceptionToString(new SlimError(String.format(
        "message:<<COULD_NOT_INVOKE_CONSTRUCTOR %s[%d]>>", className, args.length)));
  }

  protected Object createInstanceOfConstructor(String className, Object[] args) throws Exception {
    Class<?> k = searchPathsForClass(className);
    Constructor<?> constructor = getConstructor(k, k.getConstructors(), args);
    if (constructor == null)
      throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s>>", className));

    Object newInstance = constructor.newInstance(ConverterSupport.convertArgs(args, constructor.getParameterTypes()));
    if (newInstance instanceof StatementExecutorConsumer) {
      ((StatementExecutorConsumer) newInstance).setStatementExecutor(this);
    }
    return newInstance;

  }

  protected Class<?> searchPathsForClass(String className) {
    Class<?> k = getClass(className);
    if (k != null)
      return k;
    List<String> reversedPaths = new ArrayList<String>(paths);
    Collections.reverse(reversedPaths);
    for (String path : reversedPaths) {
      k = getClass(path + "." + className);
      if (k != null)
        return k;
    }
    throw new SlimError(String.format("message:<<NO_CLASS %s>>", className));
  }

  protected Class<?> getClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  protected Constructor<?> getConstructor(Class k, Constructor<?>[] constructors, Object[] args) {
    for (Constructor<?> constructor : constructors) {
      Class<?>[] arguments = constructor.getParameterTypes();
      if (arguments.length == args.length)
        return constructor;
    }
    throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s[%d]>>", k.name, args.length));  // CHANGED (replaced line)!
  }

  public Object call(String instanceName, String methodName, Object... args) {
    try {
      return getMethodExecutionResult(instanceName, methodName, args).returnValue();
    } catch (Throwable e) {
      return exceptionToString(e);
    }
  }

  protected MethodExecutionResult getMethodExecutionResult(String instanceName, String methodName, Object... args)
      throws Throwable {
    MethodExecutionResults results = new MethodExecutionResults();
    for (int i = 0; i < executorChain.size(); i++) {
      MethodExecutionResult result = executorChain.get(i).execute(instanceName, methodName, replaceSymbols(args));
      if (result.hasResult()) {
        return result;
      }
      results.add(result);
    }
    return results.getFirstResult();
  }

  public Object callAndAssign(String variable, String instanceName, String methodName, Object[] args) {
    try {
      MethodExecutionResult result = getMethodExecutionResult(instanceName, methodName, args);
      setVariable(variable, result);
      return result.returnValue();
    } catch (Throwable e) {
      return exceptionToString(e);
    }
  }

  protected Object[] replaceSymbols(Object[] args) {
    return variables.replaceSymbols(args);
  }

  protected String exceptionToString(Throwable exception) {
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

  protected boolean isStopTest(Throwable throwable) {                                                     // CHANGED (added line)
    return throwable.getClass().toString().contains("StopTest")                                         // CHANGED (added line)
  }


  public boolean stopHasBeenRequested() {
    return stopRequested;
  }

  public void reset() {
    stopRequested = false;
  }

}
