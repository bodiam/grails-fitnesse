package nl.jworks

/**
 * @author Erik Pragt
 */
class FailFixture {

    public FailFixture() {
        throw new RuntimeException("This should not happen! Test is now failed!")
    }
}
