
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU;
import grails.util.GrailsUtil as GU;
import grails.util.GrailsWebUtil as GWU
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.support.*
import java.lang.reflect.Modifier;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.codehaus.groovy.grails.commons.spring.GrailsRuntimeConfigurator as GRC;
import org.apache.tools.ant.taskdefs.optional.junit.*
import org.springframework.mock.web.*
import org.springframework.core.io.*
import org.springframework.web.context.request.RequestContextHolder;
import org.codehaus.groovy.grails.plugins.*
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.TransactionStatus
import org.apache.commons.logging.LogFactory

Ant.property(environment: "env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

compilationFailures = []
testingBaseURL = null
testingInProcessJetty = false

// Change default env to test
scriptEnv = "test"

includeTargets << grailsScript("Init")
includeTargets << grailsScript("Bootstrap")
includeTargets << grailsScript("RunApp")
includeTargets << grailsScript("RunWar")

generateLog4jFile = true

target('default': "Startup Grails to act as a Fitnesse server") {
    depends(classpath, checkVersion, parseArguments, clean, cleanTestReports, configureProxy)

    runFitnesseServer()
}


target(runFitnesseServer: "The Fitnesse implementation target") {
    depends(packageApp)

    // We accept commands of the form:
    // grails functional-tests [URL] [testname1] [testname2] [testnameN]
    // Where all in [...] are optional
    ftArgs = argsMap["params"]
    if (ftArgs && ftArgs[0] =~ "^http(s)?://") {
        testingBaseURL = ftArgs[0]

        // Shift the args
        ftArgs.remove(0)
    } else {
        // Default to internally hosted app
        testingBaseURL = "http://localhost:$serverPort$serverContextPath"
        if (!testingBaseURL.endsWith('/')) testingBaseURL += '/'
        testingInProcessJetty = true
    }

    if (testingInProcessJetty) {
        // Do init required to simulate runWar
        depends(configureProxy)
        if (!argsMap["dev-mode"]) war()
    }

    compileTests()
    packageTests()

    def server
    def completed = false
    def previousRunMode

    try {
        if (testingInProcessJetty) {
            def savedOut = System.out
            def savedErr = System.err
            try {
                new File(reportsDir, "bootstrap-out.txt").withOutputStream {outStream ->
                    System.out = new PrintStream(outStream)
                    new File(reportsDir, "bootstrap-err.txt").withOutputStream {errStream ->
                        System.err = new PrintStream(errStream)

                        if (argsMap["dev-mode"]) {
                            println "Running tests in dev mode"
                            server = configureHttpServer()
                        }
                        else {
                            server = configureHttpServerForWar()
                        }
                        // start it
                        server.start()
                    }
                }
            } finally {
                System.out = savedOut
                System.err = savedErr
            }
        }


        System.out.println "Functional tests running with base url: ${testingBaseURL}"
        // @todo Hmmm this doesn't look like the right event to use
        event("AllTestsStart", ["Starting run-functional-tests"])
        doFunctionalTests()
        event("AllTestsEnd", ["Finishing run-functional-tests"])
        produceReports()
        completed = true
    }
    catch (Exception ex) {
        ex.printStackTrace()
        throw ex
    }
    finally {
        if (testingInProcessJetty && server) {
            stopWarServer()
        }
        System.setProperty('grails.run.mode', previousRunMode)
        if (completed) {
            processResults()
        }
    }
}

target(packageTests: "Puts some useful things on the classpath") {
    Ant.copy(todir: testDirPath) {
        fileset(dir: "${basedir}", includes: "application.properties")
    }
    Ant.copy(todir: testDirPath, failonerror: false) {
        fileset(dir: "${basedir}/grails-app/conf", includes: "**", excludes: "*.groovy, log4j*, hibernate, spring")
        fileset(dir: "${basedir}/grails-app/conf/hibernate", includes: "**/**")
        fileset(dir: "${basedir}/src/java") {
            include(name: "**/**")
            exclude(name: "**/*.java")
        }
        fileset(dir: "${basedir}/test/functional") {
            include(name: "**/**")
            exclude(name: "**/*.java")
            exclude(name: "**/*.groovy)")
        }
    }

}
target(compileTests: "Compiles the functional test cases") {
    event("CompileStart", ['functional-tests'])

    def destDir = testDirPath
    Ant.mkdir(dir: destDir)
    try {
        def nonTestCompilerClasspath = compilerClasspath.curry(false)
        Ant.groovyc(destdir: destDir,
                projectName: grailsAppName,
                encoding: "UTF-8",
                classpathref: "grails.classpath", {
                    nonTestCompilerClasspath.delegate = delegate
                    nonTestCompilerClasspath.call()
                    src(path: "${basedir}/test/functional")
                })
    }
    catch (Exception e) {
        event("StatusFinal", ["Compilation Error: ${e.message}"])
        exit(1)
    }

    classLoader = new URLClassLoader([new File(destDir).toURI().toURL()] as URL[],
            classLoader)
    Thread.currentThread().contextClassLoader = classLoader

    event("CompileEnd", ['functional-tests'])
}