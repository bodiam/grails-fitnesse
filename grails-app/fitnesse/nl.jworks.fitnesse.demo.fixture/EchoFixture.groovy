package nl.jworks.fitnesse.demo.fixture

/**
 * @author Erik Pragt
 */
class EchoFixture {
    String input
    String output

    void execute() {
        output = input
    }
}
