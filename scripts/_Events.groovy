
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

        println "*************** $name : $types"

        if (!types.any { it.class == specTestTypeClass }) {
            println "> spec : $specTestTypeClass"

            println "before: " + types

            types << specTestTypeClass.newInstance('fitnesse', name)

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


/*
eventAllTestsStart = {
    // java -jar fitnesse.jar -c "FrontPage.GrailsTestSuite.SlimTestSystem?suite&format=text"
    event("StatusFinal", ["Super duper plugin action complete!"])
    
    println "hello!!!!!!!!!" + getPluginLocation()
}

eventStatusFinal = {
    //def proc = "java -jar ${getPluginLocation()}/lib/fitnesse.jar -c \"FrontPage.GrailsTestSuite.SlimTestSystem.DecisionTables?test&format=text\"".execute()
    def proc = "java -jar ${getPluginLocation()}/lib/fitnesse.jar -c \"FrontPage.GrailsTestSuite.SlimTestSystem.DecisionTables?test&format=text\"".execute()
    proc.waitFor()

println "return code: ${ proc.exitValue()}"
println "stderr: ${proc.err.text}"
println "stdout: ${proc.in.text}" // *out* from the external program is *in* for groovy

    println "the tests are done! cool!"
}

ConfigObject getBuildConfig() {
    GroovyClassLoader classLoader = new GroovyClassLoader(getClass().getClassLoader())
    ConfigObject buildConfig = new ConfigSlurper().parse(classLoader.loadClass('BuildConfig'))
    return buildConfig
}

String getPluginLocation() {
    return getBuildConfig()?.grails?.plugin?.location?.'grails-fitnesse'
}

*/