package nl.jworks.grails.plugin.fitnesse.testrunner

/**
 * Groovy representation of a Fitnesse XML Test Result, for which an example can be found below.
 *
 * <result>
 *   <counts>
 *     <right>1</right>
 *     <wrong>0</wrong>
 *     <ignores>0</ignores>
 *     <exceptions>0</exceptions>
 *   </counts>
 *   <runTimeInMillis>28</runTimeInMillis>
 *   <relativePageName>AutoLoadOnChangeTest</relativePageName>
 *   <pageHistoryLink>FrontPage.GrailsTestSuite.GrailsFitnesseFeatures.AutoLoadOnChangeTest?pageHistory&amp;resultDate=20110215214258&amp;format=xml</pageHistoryLink>
 * </result>
 */
class FitnesseTestResult {
    int right
    int wrong
    int ignores
    int exceptions
    String pageHistoryLink

    int runTimeInMillis

    String relativePageName
}
