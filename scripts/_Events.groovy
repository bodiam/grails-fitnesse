loadSpecTestTypeClass = {->
    def doLoad = {-> classLoader.loadClass('nl.jworks.grails.plugin.fitnesse.testrunner.GrailsFitnesseTestType') }
    try {
        doLoad()
    } catch (ClassNotFoundException e) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}

loadSpockTestTypes = {
    if (!binding.variables.containsKey("unitTests")) return

    Class specTestTypeClass = loadSpecTestTypeClass()

    [unit: unitTests, integration: integrationTests].each { name, types ->
        println unitTests
        println integrationTests

        println "*** $name : $types"

        if (!types.any { it.class == specTestTypeClass }) {
            println "> spec : $specTestTypeClass"

            println "before: " + types

            types << specTestTypeClass.newInstance('spock', name)

            println "after " + types
        }
    }
}

eventAllTestsStart = {
    loadSpockTestTypes()
}

eventPackagePluginsEnd = {
    loadSpockTestTypes()
}