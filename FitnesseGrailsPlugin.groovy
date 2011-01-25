import nl.jworks.grails.plugin.fitnesse.FitnesseFixtureArtefactHandler
import nl.jworks.grails.plugin.fitnesse.GrailsFitnesseSlimServer
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class FitnesseGrailsPlugin {
    // the plugin version
    def version = "0.4"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Erik Pragt"
    def authorEmail = "erik.pragt@jworks.nl"
    def title = "Fitnesse Grails Plugin"
    def description = '''The Fitnesse Grails plugin provides a bridge between Fitnesse and Grails.'''
    def documentation = "http://bodiam.github.com/grails-fitnesse-plugin/docs/manual/"

    def artefacts = [ FitnesseFixtureArtefactHandler ]

    def watchedResources = [
            "file:./grails-app/fitnesse/**/*.groovy",
            "file:../../plugins/*/grails-app/fitnesse/**/*.groovy"
    ]

    def doWithWebDescriptor = { xml ->

    }

    def doWithSpring = {
        def startPort = CH.config.grails.plugins.fitnesse.server.port ?: 8085
        def verbose = CH.config.grails.plugins.fitnesse.server.verbose ?: false

        grailsFitnesseSlimServer(GrailsFitnesseSlimServer, startPort, verbose)

        final beanConfigureClosure = configureFixtureBean.clone()
        beanConfigureClosure.delegate = delegate

        application.fitnesseFixtureClasses.each { fixtureClass ->
            beanConfigureClosure.call(fixtureClass)
        }
    }

    def doWithDynamicMethods = { ctx ->
        application.fitnesseFixtureClasses.each {
            addFixtureDynamicMethods(it)
        }
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
        if (application.isFitnesseFixtureClass(event.source)) {
            def fixtureClass = application.addArtefact(FitnesseFixtureArtefactHandler.TYPE, event.source)

            final beanConfigureClosure = configureFixtureBean.clone()
            beans {
                beanConfigureClosure.delegate = delegate
                beanConfigureClosure.call(fixtureClass)
            }.registerBeans(event.ctx)

            addFixtureDynamicMethods(fixtureClass)
        }
    }

    def onConfigChange = { event ->

    }

    def addFixtureDynamicMethods = { fixtureClass ->
        if (fixtureClass.isQueryFixture) {
            fixtureClass.metaClass.mapResults = { Collection results, def propertyMapping ->
                if (propertyMapping in Collection) {
                    propertyMapping = propertyMapping.inject([:]) { map, property ->
                        map << new MapEntry(property, property)
                    }   
                }

                results.collect { result ->
                    propertyMapping.collect { name, property ->
                        [name, result[property].toString()]
                    }   
                }
            }
        }

        if (fixtureClass.mapping) {
            fixtureClass.metaClass.query = { ->
                mapResults(queryResults(), mapping)
            }
        }
    }

    def configureFixtureBean = { fixtureClass ->
        "${fixtureClass.propertyName}"(fixtureClass.clazz) { bean ->
            bean.singleton = false
            bean.autowire = 'byName'
        }
    }
}
