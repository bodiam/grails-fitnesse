package nl.jworks.grails.plugin.fitnesse;


import fitnesse.slim.Slim
import fitnesse.slim.SlimError
import grails.util.GrailsUtil
import java.lang.reflect.InvocationTargetException
import nl.jworks.fitnesse.ProtectedStatementExecutor
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.springframework.beans.BeanInstantiationException
import org.springframework.beans.factory.BeanCreationException
import java.lang.reflect.Constructor
import fitnesse.slim.Library
import fitnesse.slim.MethodExecutionResults
import fitnesse.slim.MethodExecutionResult
import fitnesse.slim.LibraryMethodExecutor
import fitnesse.slim.SystemUnderTestMethodExecutor

/**
 * This is the API for executing a SLIM statement. This class should not know
 * about the syntax of a SLIM statement.
 */

// TODO: Refactor statement executor to get rid of code duplication!
//public class GrailsStatementExecutor implements StatementExecutorInterface {
public class GrailsStatementExecutor extends ProtectedStatementExecutor {

    public GrailsStatementExecutor() {
        Slim.addConverter(Object.class, new ObjectConverter())
    }

    @Override
    protected void addMethodExecutors() {
        executorChain.add(new GroovyMethodExecutor(instances));
        executorChain.add(new FunctionAsPropertyMethodExecutor(instances));
        executorChain.add(new GroovyQuotedMethodNameMethodExecutor(instances));
        executorChain.add(new SystemUnderTestMethodExecutor(instances));
        executorChain.add(new LibraryMethodExecutor(libraries));
    }

    public Object create(String instanceName, String className, Object[] args) {
        String replacedClassName = variables.replaceSymbolsInString(className) + "Fixture";
        // This is needed for fixture names which are not camel cased, like 'Calculate'.
        if (!replacedClassName.contains(".")) {
            replacedClassName = StringUtils.capitalize(replacedClassName)
        }

        try {
            if (hasStoredActor(className)) {
                addToInstancesOrLibrary(instanceName, getStoredActor(className));
            } else {
                Class<?> clazz = searchPathsForClass(replacedClassName)
                Constructor<?> matchingConstructor = getConstructor(clazz, args);
                Object[] convertedArgs = GroovyConverterSupport.convertArgs(replaceSymbols(args), matchingConstructor.getParameterTypes())

                Object instance = getFixtureBean(clazz, convertedArgs);
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
                return couldNotInvokeConstructorException(replacedClassName, args);
            }
        } catch (Throwable e) {

            return exceptionToString(e);
        }
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
            if (!(e.cause in BeanInstantiationException)) {
                throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s[%d]>>", clazz.name, args.length))
            } else {
                Exception thrownInConstructor = e.cause?.cause
                throw thrownInConstructor ?: e
            }
        }
    }

    protected Class<?> getClass(String className) {
        try {
            // Load from the Grails application so all classes are in the latest version
            ApplicationHolder.application.classLoader.loadClass(className)
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected Constructor<?> getConstructor(Class<?> clazz, Object[] args) {
        Constructor<?>[] constructors = clazz.getConstructors()
        for (Constructor<?> constructor: constructors) {
            Class<?>[] arguments = constructor.getParameterTypes();
            if (arguments.length == args.length)
                return constructor;
        }
        throw new SlimError(String.format("message:<<NO_CONSTRUCTOR %s[%d]>>", clazz.name, args.length));
    }

    protected String exceptionToString(Throwable unsanitizedException) {
        // TODO: make configurable
        Throwable exception = GrailsUtil.sanitize(unsanitizedException)

        return super.exceptionToString(exception)
    }
}
