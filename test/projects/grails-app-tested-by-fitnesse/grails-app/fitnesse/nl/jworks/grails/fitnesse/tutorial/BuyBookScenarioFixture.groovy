package nl.jworks.grails.fitnesse.tutorial

/**
 * @author Erik Pragt
 */
class BuyBookScenarioFixture {
    def bookService

    void customerBuysBooksWithTitle(int amount, title) {
        amount.times {
            bookService.buyBook(title)
        }
    }
}
