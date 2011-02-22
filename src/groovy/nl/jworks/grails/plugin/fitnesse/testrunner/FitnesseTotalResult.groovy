package nl.jworks.grails.plugin.fitnesse.testrunner

/**
 * Holder for the complete XML message sent by Fitnesse.
 */
class FitnesseTotalResult {
    String rootPath

    int totalRight
    int totalWrong
    int totalIgnores
    int totalExceptions
    int totalRunTimeInMillis

    List<FitnesseTestResult> testResults = []

    Iterator<FitnesseTestResult> iterator() {
        testResults.iterator()
    }
}
