package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.MethodExecutor
import fitnesse.slim.MethodExecutionResult
import java.lang.reflect.Method
import fitnesse.slim.Converter
import java.lang.reflect.InvocationTargetException
import fitnesse.slim.SlimError

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
            return new MethodExecutionResult(invokeGroovyMethod(instance, method, args))
        }
        return MethodExecutionResult.noMethod(methodName, instance.getClass(), args.length)
    }
    
    protected Object findMatchingGroovyMethod(String methodName, Class<? extends Object> k, int nArgs) {
        def methods = k.methods.toList() + k.metaClass.methods
        methods.find { method ->
            method.name == methodName && nArgs == method.parameterTypes.size()
        }
    }
    
    protected Object invokeGroovyMethod(Object instance, Object method, Object[] args) throws Throwable {
        Object[] convertedArgs = convertArgs(method, args)
        Object retval = callMethod(instance, method, convertedArgs)
        Class<?> retType = method.returnType
        if ((retType == List.class || retType == Object.class) && retval instanceof List)
            return retval
        return convertToString(retval, retType)
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

    private Object[] convertArgs(Object method, Object[] args) {
        return GroovyConverterSupport.convertArgs(args, method.parameterTypes)
    }
    /**
     * All these methods are copy and pasted because they are not protected yet.
     */
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

