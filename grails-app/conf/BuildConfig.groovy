grails.project.dependency.resolution = {
    repositories {
        mavenCentral()
        mavenRepo('http://givwenzen.googlecode.com/svn/maven2/')
    }
    dependencies {
        compile('com.googlecode.givwenzen:givwenzen:1.0.2') {
            excludes('slf4j-api', 'slf4j-simple', 'fitlibrary')
        }
    }
}
