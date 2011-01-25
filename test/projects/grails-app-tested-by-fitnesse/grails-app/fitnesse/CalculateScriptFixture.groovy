/**
 * @author Erik Pragt
 */
class CalculateScriptFixture {
    def calculateService

    def thatAddingAndEquals(int argument1, int argument2) {
        try {
            return calculateService.addition(argument1, argument2)
        } catch (e) {
            return false
        }
    }
}