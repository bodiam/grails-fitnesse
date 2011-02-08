package nl.jworks.fitnesse

import fitnesse.FitNesseContext
import fitnesse.FitNesse
import fitnesse.Arguments

class GrailsNonStaticFitnesseMain extends NonStaticFitNesseMain {


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

        def counts = testResults.result.counts

        println "${counts.right}"
        println "${counts.wrong}"
        println "${counts.ignores}"
        println "${counts.exceptions}"

        System.out.println("-----Command Complete-----");
        fitnesse.stop();
    }

    private String stripHttpHeaders(String input) {
        StringBuffer buffer = new StringBuffer()
        input.eachLine { line ->
            if(line.contains("<")) {
                buffer << line
            }
        }

        return buffer.toString()
    }
}
