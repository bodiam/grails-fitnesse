package nl.jworks.grails.plugin.fitnesse.testrunner

import fitnesse.Arguments
import nl.jworks.fitnesse.GrailsFitnesseCommandRunner
import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Class used by running 'grails test-app :fitnesse'. Registered using the _Events.groovy in the Grails eco system.
 */
class GrailsFitnesseTestType extends GrailsTestTypeSupport {

    private final Logger log = LoggerFactory.getLogger(getClass())

    GrailsFitnesseTestType(String name, String relativeSourcePath) {
        super(name, relativeSourcePath)
    }

    @Override
    protected int doPrepare() {
        return 1 // Currently no way to know how many tests to run, so I assume there's always on to run
    }

    @Override
    protected GrailsTestTypeResult doRun(GrailsTestEventPublisher eventPublisher) {
        FitnesseGrailsTestTypeResult result = new FitnesseGrailsTestTypeResult()

        List<String> commandPatterns = ["FrontPage?suite&format=xml"]

        if (testTargetPatterns) {
            commandPatterns = testTargetPatterns.collect { GrailsTestTargetPattern pattern -> "${pattern.rawPattern}&format=xml" }
        }

        if(log.debugEnabled) {
            log.debug "Executing command patterns:"
            commandPatterns.each { String commandPattern ->
                log.debug commandPattern
            }
        }

        Arguments arguments = new Arguments(omitUpdates: true, rootPath: 'wiki', command:'dummy-will-be-overruled-by-command-pattern')

        GrailsFitnesseCommandRunner runner = new GrailsFitnesseCommandRunner(commandPatterns, result, eventPublisher)
        // Fill the result and publish the test results to the event publisher
        runner.launchFitNesse arguments

        return result
    }

    @Override
    protected ClassLoader getTestClassLoader() {
        return null
    }
}
