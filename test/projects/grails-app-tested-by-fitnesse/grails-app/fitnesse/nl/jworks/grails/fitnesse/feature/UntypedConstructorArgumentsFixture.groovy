package nl.jworks.grails.fitnesse.feature

/**
 * @author Erik Pragt
 */
class UntypedConstructorArgumentsFixture {
    String input
    int output

    UntypedConstructorArgumentsFixture() {

    }

    UntypedConstructorArgumentsFixture(input, output = 5) {
        this.input = input
        this.output = output
    }

    int output() {
        return output
    }
}
