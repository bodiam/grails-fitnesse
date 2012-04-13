grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.docs.output.dir = "docs"

grails.project.dependency.resolution = {
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        excludes "xml-apis", "xmlParserAPIs"
    }
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenCentral()
        mavenLocal()
        mavenRepo('http://givwenzen.googlecode.com/svn/maven2/')
    }
    dependencies {
//        compile('org.fitnesse:fitnesse:20111025') //Added back to lib to make compatible with Grails 1.3.x
        compile('org.htmlparser:htmlparser:2.1')
        compile('com.googlecode.givwenzen:givwenzen:1.0.2') {
            excludes('slf4j-api', 'slf4j-simple', 'fitlibrary', 'xml-apis')
        }
    }
    plugins {
        build(":tomcat:$grailsVersion", ":hibernate:$grailsVersion", ":release:1.0.1") {
            export = false
        }
    }
}