includeTargets << grailsScript('_GrailsCreateArtifacts')

createFitnesseArtifact = { String type ->
    def suffix = 'Fixture'
    
    promptForName(type: type)
    def name = argsMap['params'][0]
    name = purgeRedundantArtifactSuffix(name, suffix)
    
    createArtifact(name: name, suffix: suffix, type: type, path: 'grails-app/fitnesse')
}
