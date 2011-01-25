package tutorial

/**
 * @author Erik Pragt
 */
class CheckBookInventoryFixture {
    def queryFixture = true  // indication that this is a query fixture
    static mapping = [title: "book.title", author: 1, amount: 2]  // the mapping

    def bookService       // injected service

    def queryResults() {  // queryResults() method, which must be named like this!
        bookService.checkInventory()
    }
}
