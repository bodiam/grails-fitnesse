
loadFitnesseTestTypeClass = {->
    def doLoad = {-> classLoader.loadClass('nl.jworks.grails.plugin.fitnesse.testrunner.GrailsFitnesseTestType') }
    try {
        doLoad()
    } catch (ClassNotFoundException e) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}

loadFitnesseTestTypes = {
    if (!binding.variables.containsKey("unitTests")) return

    Class fitnesseTestTypeClass = loadFitnesseTestTypeClass()

    [unit: unitTests, integration: integrationTests].each { name, types ->
        if (!types.any { it.class == fitnesseTestTypeClass }) {
            types << fitnesseTestTypeClass.newInstance('fitnesse', name)
        }
    }
}

eventAllTestsStart = {
    loadFitnesseTestTypes()
}

eventPackagePluginsEnd = {
    loadFitnesseTestTypes()
}