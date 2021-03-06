package nl.jworks.grails.plugin.fitnesse.methodexecutor

import fitnesse.slim.MethodExecutor
import fitnesse.slim.MethodExecutionResult
import fitnesse.slim.Converter
import java.lang.reflect.InvocationTargetException
import nl.jworks.grails.plugin.fitnesse.GroovyConverterSupport
import java.lang.reflect.Type
import java.lang.reflect.Field

/**
 * @author Erik Pragt
 */
class GroovyMethodExecutor extends MethodExecutor {

    protected final Map<String, Object> instances

    public GroovyMethodExecutor(Map<String, Object> instances) {
        this.instances = instances
    }

    @Override
    public MethodExecutionResult execute(String instanceName, String methodName, Object[] args) throws Throwable {
        Object instance = instances.get(instanceName)
        if (instance == null) {
            return MethodExecutionResult.noInstance(instanceName)
        }
        return findAndInvoke(methodName, args, instance)
    }

    protected MethodExecutionResult findAndInvoke(String methodName, Object[] args, Object instance) throws Throwable {
        def method = findMatchingGroovyMethod(methodName, instance.getClass(), args.length)
        if (method) {
            return new MethodExecutionResult(invokeGroovyMethod(instance, method, args), method.returnType)
        }
        return MethodExecutionResult.noMethod(methodName, instance.getClass(), args.length)
    }
    
    protected def findMatchingGroovyMethod(String methodName, Class<? extends Object> k, int nArgs) {
        def methods = k.methods.toList() + k.metaClass.methods
        methods.find { method ->
            method.name == methodName && nArgs == method.parameterTypes.size()
        }
    }
    
    protected Object invokeGroovyMethod(Object instance, method, Object[] args) throws Throwable {
        Object[] convertedArgs = convertGroovyArgs(instance, method, args)
        return callMethod(instance, method, convertedArgs)
    }

    private Object callMethod(Object instance, Object method, Object[] convertedArgs) throws Throwable {
        Object retval = null
        try {
            retval = method.invoke(instance, convertedArgs)
        } catch (InvocationTargetException e) {
            throw e.getCause()
        }
        return retval
    }

    private Object[] convertGroovyArgs(Object instance, method, Object[] args) {
        Type[] methodParameterTypes = args.size() == 1 ? determineParameterType(instance, method) : method.parameterTypes
        return GroovyConverterSupport.convertArgs(args, methodParameterTypes)
    }

    private Type[] determineParameterType(Object instance, method) {
        MetaBeanProperty property = instance.metaClass.properties.find { MetaBeanProperty property ->
            property.setter?.name == method.name
        }
        if (property?.field) {
            Field field = property.field.field
            return field.type in Collection ? [field.genericType] : [field.type]
        } else {
            return method.parameterTypes
        }
    }

    private Object convertToString(Object retval, Class<?> retType) {
      Converter converter = GroovyConverterSupport.getConverter(retType);
      if (converter != null)
        return converter.toString(retval);
      if (retval == null)
        return "null";
      else
        return retval.toString();
    }
}

