package nl.jworks.amasun.domain.book

class Inventory {
    Book book
    
    Integer amount = 0

    static transients = ['author', 'title']

    void incrementAmount(Integer delta) {
        this.amount += delta
    }

    String getAuthor() {
        book.author
    }

    String getTitle() {
        book.title
    }

}
