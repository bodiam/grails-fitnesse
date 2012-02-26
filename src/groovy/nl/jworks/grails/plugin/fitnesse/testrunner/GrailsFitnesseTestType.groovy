package nl.jworks.grails.plugin.fitnesse.testrunner

import fitnesse.Arguments
import nl.jworks.fitnesse.GrailsFitnesseCommandRunner
import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Class used by running 'grails test-app integration:fitnesse'.
 * This TestType is registered using the _Events.groovy in the Grails eco system.
 */
class GrailsFitnesseTestType extends GrailsTestTypeSupport {
    public static final List DEFAULT_FRONTPAGE_SUITE = ["FrontPage?suite"]

    private final Logger log = LoggerFactory.getLogger(getClass())

    GrailsFitnesseTestType(String name, String relativeSourcePath) {
        super(name, relativeSourcePath)
    }

    @Override
    protected ClassLoader getTestClassLoader() {
        return null // no test classloader needed
    }

    @Override
    protected int doPrepare() {
        return 1 // Currently there is no easy way to know how many tests to run, so to trigger the Grails runner, I assume there's always at least one test to run
    }

    @Override
    protected GrailsTestTypeResult doRun(GrailsTestEventPublisher eventPublisher) {
        FitnesseGrailsTestTypeResult result = new FitnesseGrailsTestTypeResult()

        List<String> testSuiteNames = determineTestSuiteNames()
        List<String> commandPatterns = createCommandPatterns(testSuiteNames)

        Arguments arguments = new Arguments(omitUpdates: true, rootPath: 'wiki', command:'dummy-will-be-overruled-by-command-pattern')
        arguments.setPort(CH.config.grails.plugin.fitnesse.wiki.port as String ?: Arguments.DEFAULT_COMMAND_PORT as String)
        arguments.setRootPath(CH.config.grails.plugin.fitnesse.wiki.dir ?: Arguments.DEFAULT_PATH)
        GrailsFitnesseCommandRunner runner = new GrailsFitnesseCommandRunner(commandPatterns, result, eventPublisher)
        // Fill the result and publish the test results to the event publisher
        runner.launchFitNesse arguments

        return result
    }

    private List<String> determineTestSuiteNames() {
        List<String> testSuiteNames = CH.config.grails.plugin.fitnesse.wiki.defaultSuite ?: DEFAULT_FRONTPAGE_SUITE

        if (testTargetPatterns && !isDefaultGrailsPattern(testTargetPatterns)) {
            testSuiteNames = testTargetPatterns.collect { GrailsTestTargetPattern pattern -> "${pattern.rawPattern}" }
        }
        return testSuiteNames
    }

    private List<String> createCommandPatterns(List<String> testSuiteNames) {
        List<String> commandPatterns = testSuiteNames.collect { "$it&format=xml"}

        if (log.debugEnabled) {
            log.debug "Executing command patterns:"
            commandPatterns.each { String commandPattern ->
                log.debug commandPattern
            }
        }
        return commandPatterns
    }


    private boolean isDefaultGrailsPattern(GrailsTestTargetPattern[] testTargetPatterns) {
        return testTargetPatterns && testTargetPatterns[0].rawPattern == "**.*"
    }
}
