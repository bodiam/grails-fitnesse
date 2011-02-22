package tutorial

/**
 * @author Erik Pragt
 */
class CreateBookInventoryFixture {
    Book book

    int amount

    def bookService

    CreateBookInventoryFixture() {
        Book.list()*.delete()
    }

    void execute() {

        amount.times {
            book.discard()
            book.id = null
            bookService.addBook(book)
        }
    }
}
