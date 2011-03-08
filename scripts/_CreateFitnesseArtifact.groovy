includeTargets << grailsScript('_GrailsCreateArtifacts')

createFitnesseArtifact = { String type ->
    def suffix = 'Fixture'

    promptForName(type: type)
    def name = argsMap['params'][0]

    if (name && suffix) {
        if (name =~ /.+$suffix$/) {
            name = name.replaceAll(/$suffix$/, "")
        }
    }

    createArtifact(name: name, suffix: suffix, type: type, path: 'grails-app/fitnesse')
}
