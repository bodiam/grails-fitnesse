import nl.jworks.fitnesse.GrailsFitnesseCommandRunner

/*
includeTargets << grailsScript('_GrailsInit')

target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, parseArguments)

    println "starting fitnesse..."
    //fitnesseMain.FitNesseMain.main("-o","-p","19090")
    println "started fitnesse..."

    println "arguments: ${argsMap}"
    println "c : ${argsMap.c}"

    String[] arguments = ["-d", "/Users/erikp/UserFiles/Projects/Personal/fitnesse-grails/fitnesse-with-longer-timeout", "-o", "-c", argsMap.params[0]] as String[]

    println "Running Fitnesse with arguments: ${arguments}"

    BetterFitNesseMain.main(arguments)

    println "past the run"

//    while(true) { }
}

*/

class Fitnesse {

    static void main(args) {

        println "hello!!"

//String[] arguments = ["-d", "/Users/erikp/UserFiles/Projects/Personal/fitnesse-grails/fitnesse-with-longer-timeout", "-o", "-c", "FrontPage.GrailsTestSuite?suite&format=xml"] as String[]

//NonStaticFitNesseMain.main(arguments)


        String[] arguments = ["-c", "FitNesse.UserGuide.TwoMinuteExample?test&format=xml"] as String[]

        new GrailsFitnesseCommandRunner().start(arguments)
//        new NonStaticFitNesseMain().start(arguments)

    }
}
