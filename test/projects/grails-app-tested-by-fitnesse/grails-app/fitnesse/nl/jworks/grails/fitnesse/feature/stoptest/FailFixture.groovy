package nl.jworks.grails.fitnesse.feature.stoptest

/**
 * Failpoint in the test. This should not happen, since the stoptest should have stopped the test.
 *
 * @author Erik Pragt
 */
class FailFixture {

    public FailFixture() {
        throw new RuntimeException("This should not happen! Test is now failed!")
    }
}
