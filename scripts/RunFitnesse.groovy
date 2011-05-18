import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import java.awt.Desktop

includeTargets << grailsScript('_GrailsInit')
includeTargets << grailsScript('_GrailsPackage')

target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, compile, createConfig)
    def fitnesseConfig = CH.config.grails.plugin.fitnesse

    Thread.start {

    ant.java(jar: "${fitnessePluginDir}/lib/fitnesse.jar", fork: true) {
        arg(value: '-d')
        arg(path: "${fitnesseConfig.wiki.dir ?: 'wiki'}")
        arg(line: "-p ${fitnesseConfig.wiki.port ?: 9090}")
    }
    }

    sleep 2000

    if(Desktop.isDesktopSupported()) {
      Desktop desktop = Desktop.desktop
      desktop.browse(new URI("http://localhost:9090"))
    }

    while(true) { }
}
