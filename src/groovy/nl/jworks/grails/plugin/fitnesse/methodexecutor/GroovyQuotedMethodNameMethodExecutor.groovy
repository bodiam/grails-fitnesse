package nl.jworks.grails.plugin.fitnesse.methodexecutor

import fitnesse.slim.MethodExecutionResult

/**
 * Allows you to use methods surrounded by quotes. Example:
 * <pre>
 * class MyFixture {
 *    void "execute me"() {
 *      println "hello"
 *    }
 * }
 * </pre>
 *
 * @author Erik Pragt
 */
class GroovyQuotedMethodNameMethodExecutor extends GroovyMethodExecutor {

    public GroovyQuotedMethodNameMethodExecutor(Map<String, Object> instances) {
        super(instances)
    }

    @Override
    public MethodExecutionResult execute(String instanceName, String methodName, Object[] args) throws Throwable {
        Object instance = instances.get(instanceName);
        if (instance == null) {
            return MethodExecutionResult.noInstance(instanceName);
        }
        return findAndInvoke(convertMethodNameToQuotedMethodName(methodName), args, instance);
    }

    private String convertMethodNameToQuotedMethodName(String methodName) {
        char[] characters = methodName.toCharArray()

        characters.collect { character ->
            if (Character.isUpperCase(character)) {
                return " " + Character.toLowerCase(character)
            } else {
                return character
            }
        }.join('')
    }
}
