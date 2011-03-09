package nl.jworks.grails.fitnesse.feature.stoptest

/**
 * @author Erik Pragt
 */
class StopTestException extends RuntimeException {
    def StopTestException(String message) {
        super(message)
    }
}
