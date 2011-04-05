package nl.jworks.guide

/**
 * @author Erik Pragt
 */

class CalculateFixture {
    int operand1
    int operand2

    int start = 0

    CalculateFixture() {
    }

    CalculateFixture(int start) {
        this.start = start
    }

    int expectation() {
        operand1 + operand2 + start
    }
}