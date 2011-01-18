
includeTargets << grailsScript('_GrailsInit')

target('default': 'Starts Fitnesse Server') {
    depends(checkVersion, parseArguments)

    println "starting fitnesse..."
    fitnesseMain.FitNesseMain.main("-o","-p","19090")
    println "started fitnesse..."


    while(true) { }
}
