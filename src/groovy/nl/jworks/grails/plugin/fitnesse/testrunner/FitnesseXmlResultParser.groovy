package nl.jworks.grails.plugin.fitnesse.testrunner

import groovy.util.slurpersupport.GPathResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Parses the Fitnesse XML format output into Groovy objects.
 */
class FitnesseXmlResultParser {
    private final Logger log = LoggerFactory.getLogger(getClass())

    FitnesseTotalResult parseFitnesseXml(String xml) {
        println "Parsing Fitnesse xml: $xml"

        GPathResult testResults = parseXml(xml)

        FitnesseTotalResult totalResult = new FitnesseTotalResult(
                totalRight: testResults.finalCounts.right.text().toInteger(),
                totalWrong: testResults.finalCounts.wrong.text().toInteger(),
                totalIgnores: testResults.finalCounts.ignores.text().toInteger(),
                totalExceptions: testResults.finalCounts.exceptions.text().toInteger(),
                totalRunTimeInMillis: testResults.totalRunTimeInMillis.text().toInteger(),
                rootPath: testResults.rootPath.text()
        )

        totalResult.testResults = testResults.result.collect { result ->
            def counts = result.counts
            new FitnesseTestResult(
                    right: counts.right.text().toInteger(),
                    wrong: counts.wrong.text().toInteger(),
                    ignores: counts.ignores.text().toInteger(),
                    exceptions: counts.exceptions.text().toInteger(),
                    runTimeInMillis: result.runTimeInMillis.text().toInteger(),
                    relativePageName: result.relativePageName.text(),
                    pageHistoryLink: result.pageHistoryLink.text()
            )
        }

        return totalResult
    }

    private GPathResult parseXml(String xml) {
        try {
            XmlSlurper slurper = new XmlSlurper()
            slurper.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            slurper.setFeature("http://xml.org/sax/features/namespaces", false)
            return slurper.parseText(xml)
        } catch (e) {
            throw new IllegalArgumentException("Input XML cannot be parsed: ${xml}", e)
        }
    }
}
