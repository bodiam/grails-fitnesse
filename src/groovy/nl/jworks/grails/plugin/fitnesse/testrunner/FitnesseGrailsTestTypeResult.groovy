package nl.jworks.grails.plugin.fitnesse.testrunner

import org.codehaus.groovy.grails.test.GrailsTestTypeResult

/**
 * Holder for the results of a testrun.
 */
class FitnesseGrailsTestTypeResult implements GrailsTestTypeResult {

    // Fitnesse properties
    private int rightCount
    private int wrongCount
    private int ignores
    private int exceptions

    int getPassCount() {
        rightCount
    }

    int getFailCount() {
        wrongCount + exceptions
    }

    void incrementRightCount(int delta) {
        this.rightCount += delta
    }
    void incrementWrongCount(int delta) {
        this.wrongCount += delta
    }
    void incrementIgnores(int delta) {
        this.ignores += delta
    }
    void incrementExceptions(int delta) {
        this.exceptions += delta
    }
}
