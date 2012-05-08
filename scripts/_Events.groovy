import org.codehaus.groovy.grails.test.support.GrailsTestMode

def fitnesseTestTypeClassName = 'nl.jworks.grails.plugin.fitnesse.testrunner.GrailsFitnesseTestType'

def loadedTestTypes = []
def runningTests = false
fitnesseTests = []
def testMode = new GrailsTestMode(autowire: true)

softLoadFitnesseClass = { className ->
    try {
        classLoader.loadClass(className)
    } catch (ClassNotFoundException e) {
        null
    }
}

tryToLoadFitnesseTestType = {name, typeClassName ->
    if (name in loadedTestTypes) return
    if (!binding.variables.containsKey("otherTests")) return
    
    def typeClass = softLoadFitnesseClass(typeClassName)
    if (typeClass) {
        if (!fitnesseTests.any { it.class == typeClass }) {
            fitnesseTests << typeClass.newInstance(name, 'fitnesse')
        }
        loadedTestTypes << name
    }
}

tryToLoadFitnesseTestTypes = {
    tryToLoadFitnesseTestType("fitnesse", fitnesseTestTypeClassName)
}

eventAllTestsStart = {
    runningTests = true
    
    tryToLoadFitnesseTestTypes()

    [fitnesseTestTypeClassName].each { testTypeClassName ->
        def testTypeClass = softLoadFitnesseClass(testTypeClassName)
        if (testTypeClass) {
            if (!fitnesseTests.any { it.class == testTypeClass }) {
                fitnesseTests << testTypeClass.newInstance('fitnesse', 'fitnesse')
            }
        }
    }
}

eventPackagePluginsEnd = {
    tryToLoadFitnesseTestTypes()
}

eventTestPhaseStart = { phaseName ->
    if (phaseName == 'fitnesse') {
        // GRAILS-7563
        if (!binding.hasProperty('serverContextPath')) {
            includeTargets << grailsScript("_GrailsPackage")
            createConfig() // GRAILS-7562
            configureServerContextPath()
        }
        
        def baseUrl = argsMap["baseUrl"] ?: "http://${serverHost ?: 'localhost'}:$serverPort${serverContextPath == "/" ? "" : serverContextPath}/"
        System.setProperty('fitnesse.build.baseUrl', baseUrl)

        def reportsDir = new File(grailsSettings.testReportsDir, 'fitnesse')
        System.setProperty('fitnesse.build.reportsDir', reportsDir.absolutePath)
    }
}

fitnesseTestPhasePreparation = {
    integrationTestPhasePreparation()
}
fitnesseTestPhaseCleanUp = {
    integrationTestPhaseCleanUp()
}

// Just upgrade plugins without user input when building this plugin
// Has no effect for clients of this plugin
if (grailsAppName == 'fitnesse') {

    // Override to workaround GRAILS-7296
    org.codehaus.groovy.grails.test.support.GrailsTestTypeSupport.metaClass.getSourceDir = { ->
        new File(delegate.buildBinding.grailsSettings.testSourceDir, delegate.relativeSourcePath)
    }

    def resolveDependenciesWasInteractive = false
    eventResolveDependenciesStart = {
        resolveDependenciesWasInteractive = isInteractive
        isInteractive = false
    }

    eventResolveDependenciesEnd = {
        isInteractive = resolveDependenciesWasInteractive
    }
}
