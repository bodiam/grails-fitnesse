grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenRepo('http://givwenzen.googlecode.com/svn/maven2/')
    }
    dependencies {
        runtime('com.googlecode.givwenzen:givwenzen:1.0.2') {
            excludes('slf4j-api', 'slf4j-simple', 'fitlibrary', 'xml-apis')
        }
    }
}
