import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import nl.jworks.grails.plugin.fitnesse.FitnesseFixtureArtefactHandler
import nl.jworks.grails.plugin.fitnesse.GrailsFitnesseSlimServer
import nl.jworks.grails.plugin.fitnesse.GrailsSlimFactory
import nl.jworks.groovy.ClosureMetaMethodWithReturnType
import org.codehaus.groovy.grails.plugins.GrailsPluginUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource

class FitnesseGrailsPlugin {
    // Plugin defaults
    public static final int DEFAULT_SERVER_PORT = 8085
    public static final boolean DEFAULT_VERBOSITY = false

    private final Logger log = LoggerFactory.getLogger("nl.jworks.grails.plugin.fitnesse.FitnesseGrailsPlugin")

    // the plugin version
    def version = "2.0.4"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def scopes = CH.config.grails.plugin.fitnesse.disabled ? [excludes: ['war', 'all']] : [:]

    def author = "Erik Pragt"
    def authorEmail = "erik.pragt@jworks.nl"
    def title = "Fitnesse Grails Plugin"
    def description = '''The Fitnesse Grails plugin provides a bridge between Fitnesse and Grails.'''
    def documentation = "http://bodiam.github.com/grails-fitnesse-plugin/docs/manual/"

    def artefacts = [FitnesseFixtureArtefactHandler]

    def watchedResources = [
            "file:./grails-app/fitnesse/**/*.groovy",
            "file:./plugins/*/grails-app/fitnesse/**/*.groovy"
    ]

    def doWithWebDescriptor = { xml ->

    }

    def doWithSpring = {
        def startPort = CH.config.grails.plugin.fitnesse.slim.port ?: DEFAULT_SERVER_PORT
        def verbose = CH.config.grails.plugin.fitnesse.slim.verbose ?: DEFAULT_VERBOSITY

        grailsFitnesseSlimServer(GrailsFitnesseSlimServer, startPort, verbose) {
            grailsSlimFactory = ref('grailsSlimFactory')
        }
        grailsSlimFactory(GrailsSlimFactory) {
            sessionFactory = ref('sessionFactory')
        }

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
            if (!artefactIsAlreadyInResources(event.source)) {
                log.debug("New fixture class $event.source.name found, clearing resources cache")
                GrailsPluginUtils.pluginBuildSettings.clearCache()
            } else {
                log.debug("Reloading $event.source.name fixture class")
            }
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
                        [name, findPropertyValue(result, property)]
                    }
                }
            }
        }

        if (fixtureClass.mapping) {
            registerClosureMetaMethodWithReturnType(fixtureClass.metaClass, 'query', List) {->
                mapResults(queryResults(), mapping)
            }
        }
    }

    private def findPropertyValue(object, Number index) {
        return object[index].toString()
    }

    private def findPropertyValue(object, String path) {
        def result = object

        def properties = path.split("\\.")
        properties.each { property ->
           result = result[property]
        }
        return result
    }
    
    def configureFixtureBean = { fixtureClass ->
        "${fixtureClass.propertyName}"(fixtureClass.clazz) { bean ->
            bean.singleton = false
            bean.autowire = 'byName'
        }
    }

    private void registerClosureMetaMethodWithReturnType(MetaClass ownerMetaClass, String name, Class returnType, Closure closure) {
        ClosureMetaMethodWithReturnType metaMethod = new ClosureMetaMethodWithReturnType(name, returnType, this.class, closure)
        if (!(ownerMetaClass in ExpandoMetaClass)) {
            ownerMetaClass.replaceDelegate()
        }
        ownerMetaClass.registerInstanceMethod(metaMethod)
    }

    private boolean artefactIsAlreadyInResources(Class clazz) {
        Resource[] resources = GrailsPluginUtils.pluginBuildSettings.artefactResources

        def classNamesOfResources = resources.collect { Resource resource
            def split = it.path.split('/') as List
            def start = split.lastIndexOf('grails-app') + 2
            split[start..-1].join('.') - '.groovy'
        }

        return clazz.name in classNamesOfResources
    }
}
