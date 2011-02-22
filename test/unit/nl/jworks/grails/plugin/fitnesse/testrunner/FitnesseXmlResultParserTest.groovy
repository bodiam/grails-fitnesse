package nl.jworks.grails.plugin.fitnesse.testrunner

import org.junit.Test


class FitnesseXmlResultParserTest {

    @Test
    void "should parse single xml test result"() {
        String xml = """<?xml version="1.0"?>
<testResults>
  <FitNesseVersion>v20110213</FitNesseVersion>
  <rootPath>GrailsFitnesseFeatures</rootPath>
  <result>
    <counts>
      <right>1</right>
      <wrong>2</wrong>
      <ignores>3</ignores>
      <exceptions>4</exceptions>
    </counts>
    <runTimeInMillis>28</runTimeInMillis>
    <relativePageName>AutoLoadOnChangeTest</relativePageName>
    <pageHistoryLink>FrontPage.GrailsTestSuite.GrailsFitnesseFeatures.AutoLoadOnChangeTest?pageHistory&amp;resultDate=20110215214258&amp;format=xml</pageHistoryLink>
  </result>
  <finalCounts>
    <right>1</right>
    <wrong>2</wrong>
    <ignores>3</ignores>
    <exceptions>4</exceptions>
  </finalCounts>
  <totalRunTimeInMillis>3656</totalRunTimeInMillis>
  </testResults>"""

        FitnesseTotalResult result = new FitnesseXmlResultParser().parseFitnesseXml(xml)

        assert result.rootPath == "GrailsFitnesseFeatures"
        assert result.totalRight == 1
        assert result.totalWrong == 2
        assert result.totalIgnores == 3
        assert result.totalExceptions == 4
        assert result.totalRunTimeInMillis == 3656

        List<FitnesseTestResult> results = result.testResults
        assert results.size() == 1

        // TODO: use hamcrest matcher
        FitnesseTestResult testResult = results[0]

        assert testResult.right == 1
        assert testResult.wrong == 2
        assert testResult.ignores == 3
        assert testResult.exceptions == 4
    }

    @Test
    void "should parse multiple xml test results"() {
        String xml = """<?xml version="1.0"?>
<testResults>
  <FitNesseVersion>v20110213</FitNesseVersion>
  <rootPath>GrailsFitnesseFeatures</rootPath>
   <result>
    <counts>
      <right>3</right>
      <wrong>0</wrong>
      <ignores>1</ignores>
      <exceptions>2</exceptions>
    </counts>
    <runTimeInMillis>85</runTimeInMillis>
    <relativePageName>HibernateLazyLoadingExceptionTest</relativePageName>
    <pageHistoryLink>FrontPage.GrailsTestSuite.GrailsFitnesseFeatures.HibernateLazyLoadingExceptionTest?pageHistory&amp;resultDate=20110215214258&amp;format=xml</pageHistoryLink>
  </result>

  <result>
    <counts>
      <right>0</right>
      <wrong>2</wrong>
      <ignores>0</ignores>
      <exceptions>1</exceptions>
    </counts>
    <runTimeInMillis>101</runTimeInMillis>
    <relativePageName>HibernateSessionExceptionOnSaveTest</relativePageName>
    <pageHistoryLink>FrontPage.GrailsTestSuite.GrailsFitnesseFeatures.HibernateSessionExceptionOnSaveTest?pageHistory&amp;resultDate=20110215214258&amp;format=xml</pageHistoryLink>
  </result>

  <result>
    <counts>
      <right>6</right>
      <wrong>0</wrong>
      <ignores>0</ignores>
      <exceptions>0</exceptions>
    </counts>
    <runTimeInMillis>76</runTimeInMillis>
    <relativePageName>JsonComplexObjects</relativePageName>
    <pageHistoryLink>FrontPage.GrailsTestSuite.GrailsFitnesseFeatures.JsonComplexObjects?pageHistory&amp;resultDate=20110215214258&amp;format=xml</pageHistoryLink>
  </result>
  <finalCounts>
    <right>9</right>
    <wrong>2</wrong>
    <ignores>1</ignores>
    <exceptions>3</exceptions>
  </finalCounts>
  <totalRunTimeInMillis>4893</totalRunTimeInMillis>
  </testResults>"""

        FitnesseTotalResult result = new FitnesseXmlResultParser().parseFitnesseXml(xml)

        assert result.totalRight == 9
        assert result.totalWrong == 2
        assert result.totalIgnores == 1
        assert result.totalExceptions == 3
        assert result.totalRunTimeInMillis == 4893

        List<FitnesseTestResult> results = result.testResults
        assert results.size() == 3
    }


    @Test(expected=IllegalArgumentException)
    void "html instead of xml"() {
        String xml = """<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html>	<head>		<title>Not Found:FrontPage.GrailsTestSuite.SlimTestSystem&format=xml</title>		<link rel="stylesheet" type="text/css" href="/files/css/fitnesse.css" media="screen"/>		<link rel="stylesheet" type="text/css" href="/files/css/fitnesse_print.css" media="print"/>		<script src="/files/javascript/fitnesse.js" type="text/javascript"></script>	</head>	<body>		<div class="sidebar">			<div class="art_niche" onclick="document.location='FrontPage'"></div>			<div class="actions"></div>		</div>		<div class="mainbar">			<div class="header">				<span class="page_title">Not Found:FrontPage.GrailsTestSuite.SlimTestSystem&format=xml</span>			</div>			<div class="main">The requested resource: <i>FrontPage.GrailsTestSuite.SlimTestSystem&format=xml</i> was not found.</div>		</div>	</body></html>"""

        FitnesseTotalResult result = new FitnesseXmlResultParser().parseFitnesseXml(xml)

        assert result.totalRight == 0
    }
}
