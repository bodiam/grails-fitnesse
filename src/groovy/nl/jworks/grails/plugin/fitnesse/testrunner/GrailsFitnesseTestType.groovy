package nl.jworks.grails.plugin.fitnesse.testrunner

import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import nl.jworks.fitnesse.GrailsNonStaticFitnesseMain


class GrailsFitnesseTestType extends GrailsTestTypeSupport {

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

        eventPublisher.testCaseStart("CASE ALL")
        eventPublisher.testStart("ALL")

//        String[] arguments = ["-o", "-d", "wiki", "-c", "FitNesse.UserGuide.TwoMinuteExample?test&format=xml"] as String[]
        String[] arguments = ["-o", "-d", "wiki", "-c", "FrontPage.GrailsTestSuite.SlimTestSystem?suite&format=xml"] as String[]
        new GrailsNonStaticFitnesseMain(result).start(arguments)

        eventPublisher.testEnd("ALL")
        eventPublisher.testCaseEnd("CASE ALL")
        return result
    }

    @Override
    protected ClassLoader getTestClassLoader() {
        println "getTestClassLoader"
        
        return null
    }


}
