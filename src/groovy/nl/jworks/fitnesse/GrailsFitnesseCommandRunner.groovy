package nl.jworks.fitnesse

import fitnesse.FitNesseContext
import fitnesse.FitNesse
import fitnesse.Arguments
import nl.jworks.grails.plugin.fitnesse.testrunner.FitnesseGrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import nl.jworks.grails.plugin.fitnesse.testrunner.FitnesseXmlResultParser
import nl.jworks.grails.plugin.fitnesse.testrunner.FitnesseTotalResult
import nl.jworks.grails.plugin.fitnesse.testrunner.FitnesseTestResult
import org.codehaus.groovy.grails.test.GrailsTestTypeResult

class GrailsFitnesseCommandRunner extends NonStaticFitNesseMain {
    private FitnesseGrailsTestTypeResult result
    private GrailsTestEventPublisher eventPublisher
    private List<String> commandPatterns

    private FitnesseXmlResultParser resultParser = new FitnesseXmlResultParser()

    GrailsFitnesseCommandRunner(List<String> commandPatterns, FitnesseGrailsTestTypeResult result, GrailsTestEventPublisher eventPublisher) {
        this.commandPatterns = commandPatterns
        this.result = result
        this.eventPublisher = eventPublisher
    }

    @Override
    protected void executeSingleCommand(Arguments arguments, FitNesse fitnesse, FitNesseContext context) throws Exception {
        commandPatterns.each { String commandPattern ->
            String xml = getFitnesseTestXmlResult(commandPattern, fitnesse, context)

            println "x"
            println "$xml"

            FitnesseTotalResult totalResult = resultParser.parseFitnesseXml(xml)

            println "y"

            totalResult.each { FitnesseTestResult testResult ->
                eventPublisher.testCaseStart(totalResult.rootPath + "." + testResult.relativePageName)

                result.incrementExceptions testResult.exceptions
                result.incrementIgnores testResult.ignores
                result.incrementRightCount testResult.right
                result.incrementWrongCount testResult.wrong

                eventPublisher.testCaseEnd(totalResult.rootPath + "." + testResult.relativePageName, "hello", "world")
            }
            System.out.println("-----Command Complete-----");
        }
        fitnesse.stop();
    }

    private String getFitnesseTestXmlResult(String testOrSuite, FitNesse fitnesse, FitNesseContext context) {
        context.doNotChunk = true;

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        fitnesse.executeSingleCommand(testOrSuite, baos);

        String xmlWithHttpHeaders = baos.toString("UTF-8")

        return stripHttpHeaders(xmlWithHttpHeaders)
    }

    private String stripHttpHeaders(String input) {
        StringBuffer buffer = new StringBuffer()
        input.eachLine { line ->
            if (line.contains("<")) {
                buffer << line
            }
        }

        return buffer.toString()
    }
}
