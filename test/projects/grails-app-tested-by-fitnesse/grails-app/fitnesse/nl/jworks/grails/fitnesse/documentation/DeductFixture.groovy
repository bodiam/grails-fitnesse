package nl.jworks.grails.fitnesse.documentation

/**
 * Used in the example for Import Tables.
 *
 * @author Erik Pragt
 */
class DeductFixture {
    int operand1
    int operand2

    int result() {
        operand1 - operand2
    }
}
