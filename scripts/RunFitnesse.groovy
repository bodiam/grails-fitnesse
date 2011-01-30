import fitnesse.Arguments
import fitnesseMain.FitNesseMain
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsPackage')

target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, compile, createConfig)
    def fitnesseConfig = CH.config.grails.plugins.fitnesse

    ant.java(jar: "${fitnessePluginDir}/lib/fitnesse.jar", fork: true) {
	arg(value: '-d')
	arg(path: "${fitnesseConfig.server.dir ?: 'wiki'}")
	arg(line: "-p ${fitnesseConfig.server.port ?: 9090}")
    }
}
