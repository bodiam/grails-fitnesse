includeTargets << grailsScript('_GrailsInit')
includeTargets << new File("${fitnessePluginDir}/scripts/_CreateFitnesseArtifact.groovy")

target('default': 'Creates a Fitnesse fixture') {
    depends(checkVersion, parseArguments)

    createFitnesseArtifact('FitnesseQueryFixture')
}
