package nl.jworks.grails.plugin.fitnesse.testrunner

import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import nl.jworks.fitnesse.GrailsNonStaticFitnesseMain


class GrailsFitnesseTestType extends GrailsTestTypeSupport {

    GrailsFitnesseTestType(String name, String relativeSourcePath) {
        super(name, relativeSourcePath)

        println "*" * 80
        println "name $name"
        println "rel $relativeSourcePath"
    }

    @Override
    protected int doPrepare() {
        println "doPrepare"
        return 1
    }

    @Override
    protected GrailsTestTypeResult doRun(GrailsTestEventPublisher eventPublisher) {
        println "doRun"
        println "doRun1"

        FitnesseGrailsTestTypeResult result = new FitnesseGrailsTestTypeResult()
println "doRun2"
        eventPublisher.testCaseStart("CASE ALL")
        println "doRun3"
        eventPublisher.testStart("ALL")

println "doRun4"
//        String[] arguments = ["-o", "-d", "wiki", "-c", "FitNesse.UserGuide.TwoMinuteExample?test&format=xml"] as String[]
        String[] arguments = ["-o", "-d", "wiki", "-c", "FrontPage.GrailsTestSuite.SlimTestSystem?suite&format=xml"] as String[]
        println "Starting Fitnesse"
        new GrailsNonStaticFitnesseMain(result).start(arguments)
        println "Fitnesse started / ended"

        println "doRun5"
        eventPublisher.testEnd("ALL")
        println "doRun6"
        eventPublisher.testCaseEnd("CASE ALL")
println "doRun7 : ${result}"
        return result
    }

    @Override
    protected ClassLoader getTestClassLoader() {
        println "getTestClassLoader"
        
        return null
    }


}
