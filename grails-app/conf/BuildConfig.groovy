grails.project.dependency.resolution = {
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    repositories {
        mavenCentral()
        mavenRepo('http://givwenzen.googlecode.com/svn/maven2/')
    }
    dependencies {
        compile('org.fitnesse:fitnesse:20111025')
        compile('org.htmlparser:htmlparser:2.1')
        compile('com.googlecode.givwenzen:givwenzen:1.0.2') {
            excludes('slf4j-api', 'slf4j-simple', 'fitlibrary', 'xml-apis')
        }
    }
}