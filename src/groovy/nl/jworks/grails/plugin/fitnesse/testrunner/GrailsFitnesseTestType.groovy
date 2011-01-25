package nl.jworks.grails.plugin.fitnesse.testrunner

import org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher


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
        return 0
    }

    @Override
    protected GrailsTestTypeResult doRun(GrailsTestEventPublisher eventPublisher) {
        println "doRun"
        return null
    }
}
