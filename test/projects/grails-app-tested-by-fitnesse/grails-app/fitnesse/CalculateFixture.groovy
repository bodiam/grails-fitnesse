/**
 * @author Erik Pragt
 */

class CalculateFixture {
    def calculateService

    int operand1
    int operand2

    int start = 0

    CalculateFixture() {
    }

    CalculateFixture(int start) {
        this.start = start
    }

    int expectation() {
        calculateService.addition(operand1, operand2) + start
    }

    public void setCalculateService(value) {
        this.calculateService = value
    }
}