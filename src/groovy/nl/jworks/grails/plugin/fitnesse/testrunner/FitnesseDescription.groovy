package nl.jworks.grails.plugin.fitnesse.testrunner

import org.junit.runner.Description

//@Category(Description)
class FitnesseDescription {

    FitnesseTestResult result

    public void setResult(FitnesseTestResult result) {
        this.result = result
    }

    public int testCount() {
        return result.right + result.wrong + result.exceptions + result.ignores
    }
}
