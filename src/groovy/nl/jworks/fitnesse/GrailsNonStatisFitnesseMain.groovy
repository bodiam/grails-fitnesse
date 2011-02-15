package nl.jworks.fitnesse

import fitnesse.FitNesseContext
import fitnesse.FitNesse
import fitnesse.Arguments
import nl.jworks.grails.plugin.fitnesse.testrunner.FitnesseGrailsTestTypeResult

class GrailsNonStaticFitnesseMain extends NonStaticFitNesseMain {
    private FitnesseGrailsTestTypeResult result

    GrailsNonStaticFitnesseMain(FitnesseGrailsTestTypeResult result) {
        this.result = result
    }

    @Override
    protected void executeSingleCommand(Arguments arguments, FitNesse fitnesse, FitNesseContext context) throws Exception {
        context.doNotChunk = true;

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        fitnesse.executeSingleCommand(arguments.getCommand(), baos);

        String xmlWithHttpHeaders = baos.toString("UTF-8")

        println "Result before: ${xmlWithHttpHeaders}"

        String xml = stripHttpHeaders(xmlWithHttpHeaders)

        println "Result after: ${xml}"

        def testResults = new XmlSlurper().parseText(xml)

        testResults.result.each { result ->
            def counts =result.counts

            println "${counts.right}"
            println "${counts.wrong}"
            println "${counts.ignores}"
            println "${counts.exceptions}"
            
            this.result.incrementRightCount counts.right.text().toInteger()
            this.result.incrementWrongCount counts.wrong.text().toInteger()
            this.result.incrementIgnores counts.ignores.text().toInteger()
            this.result.incrementExceptions counts.exceptions.text().toInteger()

        }

        System.out.println("-----Command Complete-----");
        fitnesse.stop();
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
