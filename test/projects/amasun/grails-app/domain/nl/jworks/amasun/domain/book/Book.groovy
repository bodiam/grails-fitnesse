package nl.jworks.amasun.domain.book

class Book {
    static belongsTo = Inventory

    String author
    String title
    String isbn
    Integer price

    static constraints = {
        price(min: 0)
        isbn(unique: true)
        author(size: 1..255)
        title(size: 1..255)
    }
}
