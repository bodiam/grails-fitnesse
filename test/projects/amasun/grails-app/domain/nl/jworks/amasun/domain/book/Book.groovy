package nl.jworks.amasun.domain.book

class Book {
    static belongsTo = Inventory

    String author
    String title
    String isbn
    Integer price

    String details

    static constraints = {
        price(min: 0)
        isbn(unique: true)
        author(size: 1..255)
        title(size: 1..255)

        details(nullable:true, size:1..4096)
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Book)) return false

        Book book = (Book) o

        if (isbn != book.isbn) return false

        return true
    }

    int hashCode() {
        return isbn.hashCode()
    }
}
