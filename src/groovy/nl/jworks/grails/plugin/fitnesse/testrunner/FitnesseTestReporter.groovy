package nl.jworks.grails.plugin.fitnesse.testrunner

import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.codehaus.groovy.grails.test.report.junit.JUnitReports
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest
import org.junit.runner.Description
import junit.framework.JUnit4TestCaseFacade
import junit.framework.Test
import junit.framework.AssertionFailedError
import org.codehaus.groovy.runtime.DefaultGroovyMethods

class FitnesseTestReporter {

    FitnesseTotalResult result
    String xml
    String commandPattern
    Binding buildBinding
    def testSuite

    private testsByDescription = [:]
    private descriptionsByTestResult = [:]
    
    public FitnesseTestReporter(String commandPattern, FitnesseTotalResult result, String xml, Binding buildBinding) {
        this.result = result
        this.xml = xml
        this.commandPattern = commandPattern
        this.buildBinding = buildBinding
    }
    
    public void writeXmlResults() {
        String prefix = commandPattern.replaceAll("[\\?, \\.]", "-").split("&")[0]
        File dir = new File(System.getProperty("fitnesse.build.reportsDir"))
        if(!dir.exists()) {
            dir.mkdirs()
        }
        File xmlFile = new File(System.getProperty("fitnesse.build.reportsDir"), prefix + ".xml")
        if (xmlFile.createNewFile()) {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(xmlFile))
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(bos))
            bw.write(xml)
            bw.close()
        } else {
            println "Couldn't create XML file"
        }
    }

    public void reportJUnitResults() {
        def factory = JUnitReportsFactory.createFromBuildBinding(buildBinding)
        JUnitReports reports = factory.createReports(getSuiteName())
        def suite = getTestSuite()
        reports.startTestSuite(suite)
        result.each {FitnesseTestResult testResult ->
            def description = getDescriptionFor(testResult)
            def test = getTest(description)
            reports.startTest(test)
            if(testResult.exceptions) {
                (1..testResult.exceptions).each {
                    reports.addError(getTest(description), new Exception("Fitnesse reported exception in [${testResult.relativePageName}]"))
                }
            }
            if(testResult.wrong) {
                (1..testResult.wrong).each {
                    reports.addFailure(getTest(description), new AssertionFailedError("Fitnesse reported assertion wrong in [${testResult.relativePageName}]"))
                }
            }
            reports.endTest(test)
        }
        if(result.totalExceptions) {
            reports.setSystemError("Total Exceptions: ${result.totalExceptions}")
        } else {
            reports.setSystemError("")
        }
        reports.setSystemOutput("Runtime: ${result.totalRunTimeInMillis}, Pass: ${result.totalRight}, Fail: ${result.totalWrong}, Ignore: ${result.totalIgnores}")
        suite.runTime = result.totalRunTimeInMillis
        def runs = result.totalRight + result.totalWrong + result.totalIgnores
        suite.setCounts(runs, result.totalWrong, result.totalIgnores)
        reports.endTestSuite(suite)
    }
    
    protected String getSuiteName() {
        return commandPattern.split("\\?")[0]
    }
    
    protected JUnitTest getTestSuite() {
        if(!testSuite) {
            testSuite = new JUnitTest(getSuiteName())
        }        
        testSuite
    }

    // JUnitReports requires us to always pass the same Test instance
    // for a test, so we cache it; this scheme also works for the case
    // where testFailure() is invoked without a prior call to testStarted()
    private Test getTest(description) {
        def test = testsByDescription.get(description)
        if (test == null) {
            test = createJUnit4TestCaseFacade(description)
            testsByDescription.put(description, test)
        }
        test
    }
    
    private Description getDescriptionFor(FitnesseTestResult testResult) {
        def desc = descriptionsByTestResult.get(testResult)
        if (desc == null) {
            desc = Description.createSuiteDescription(testResult.relativePageName)
//            DefaultGroovyMethods.mixin(desc.metaClass, FitnesseDescription)
            descriptionsByTestResult.put(testResult, desc)
        }
        desc
    }

    static Test createJUnit4TestCaseFacade(Description description) {
        def ctor = JUnit4TestCaseFacade.getDeclaredConstructor(Description)
        ctor.accessible = true
        ctor.newInstance(description)
    }
}
