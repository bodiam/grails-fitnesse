package nl.jworks.guide

/**
 * @author Erik Pragt
 */
class CalculateScriptFixture {
    def thatAddingAndEquals(int argument1, int argument2) {
        try {
            return argument1 + argument2
        } catch (e) {
            return false
        }
    }
}