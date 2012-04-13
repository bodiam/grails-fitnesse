import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

includeTargets << new File("$fitnessePluginDir/scripts/_FitnesseCommon.groovy")

includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsPackage')


target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, compile, createConfig)
    def fitnesseConfig = CH.config.grails.plugin.fitnesse
//    def jarLoc = grailsSettings.runtimeDependencies.find { it.name.contains('fitnesse')}.path

    int port = fitnesseConfig.wiki.port ?: 9090

    printMessage "Fitnesse server starting. Browse to http://localhost:${port}"

    ant.java(jar: "${fitnessePluginDir}/lib/fitnesse.jar", fork: true, errorProperty:'fitnesse.failed') {
//    ant.java(jar: "${jarLoc}", fork: true) {
        arg(value: '-d')
        arg(path: "${fitnesseConfig.wiki.dir ?: 'wiki'}")
        arg(line: "-p ${port}")
    }

    if(antProp('fitnesse.failed')) {
        errorMessage "Fitnesse server failed to start. Please run using -verbose option to see the error."
    }
}

final String antProp(String property) {
    ant.project.properties[property] as String
}