package nl.jworks

/**
 * @author Erik Pragt
 */
class StopTestExceptionFixture {

    StopTestExceptionFixture() {
        throw new StopTestException("This should stop the test!") // stop exception in constructor. Currently not supported by Fitnesse
    }

    void throwException() {
        throw new StopTestException("This should stop the test!") // stop exception in constructor. Supported by Fitnesse
    }
}
