import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsPackage')

target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, compile, createConfig)
    def fitnesseConfig = CH.config.grails.plugin.fitnesse
    def jarLoc = grailsSettings.runtimeDependencies.find { it.name.contains('fitnesse')}.path
//    ant.java(jar: "${fitnessePluginDir}/lib/fitnesse.jar", fork: true) {
    ant.java(jar: "${jarLoc}", fork: true) {
        arg(value: '-d')
        arg(path: "${fitnesseConfig.wiki.dir ?: 'wiki'}")
        arg(line: "-p ${fitnesseConfig.wiki.port ?: 9090}")
    }
}
