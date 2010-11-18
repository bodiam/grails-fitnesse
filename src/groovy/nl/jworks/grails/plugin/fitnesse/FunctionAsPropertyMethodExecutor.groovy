package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.MethodExecutionResult

/**
 * Converts a method, by Robert C. Martin referred to as a 'function' to a normal getter function. This allows you
 * to write Decision Table fixtures without the need for a special method without the 'get' prefix.
 *
 * Without this method executor, your fixture would look like this:
 *
 * <pre>
 * class MyFixture {
 *    String name
 *
 *    String name() {
 *      return name
 *    }
 * }
 * </pre>
 *
 * Because of this class, you can rewrite the fixture to:
 *
 * <pre>
 * class MyFixture {
 *    String name
 * }
 * </pre>
 *
 * @author Erik Pragt
 */
class FunctionAsPropertyMethodExecutor extends GroovyMethodExecutor {

    public FunctionAsPropertyMethodExecutor(Map<String, Object> instances) {
        super(instances)
    }

    @Override
    public MethodExecutionResult execute(String instanceName, String methodName, Object[] args) throws Throwable {
        Object instance = instances.get(instanceName);
        if (instance == null) {
            return MethodExecutionResult.noInstance(instanceName);
        }
        return findAndInvoke(convertFunctionToProperty(methodName), args, instance);
    }

    private String convertFunctionToProperty(String methodName) {
        "get${methodName[0].toUpperCase() + methodName[1..-1]}"
    }

}
