package tutorial

class BookService {

    static transactional = true

    def addBook(book) {
        book.save()
    }

    def buyBook(title) {
        Book.findByTitle(title).delete()
    }

    def checkInventory() {
        return Book.executeQuery("select b.title, b.author, count(*) from Book b group by title, author")
    }
}
