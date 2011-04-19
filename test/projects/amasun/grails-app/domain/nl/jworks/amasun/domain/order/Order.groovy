package nl.jworks.amasun.domain.order

import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.PromoPackage

class Order {
    static belongsTo = [customer:Customer]

    private Map<Book, Integer> contents = [:] // basket...
    private Integer discount

    static transients = ['discount', 'total', 'subtotal','totalItems']

    static mapping = {
        table 'bookorder'
    }

    protected Order() {
        
    }

    Order(Customer customer) {
        this.customer = customer
    }


    void addBook(Book book, Integer amount) {
        def currentAmount = contents[book]

        if(!currentAmount) {
            currentAmount = 0
        }

        currentAmount += amount

        contents[book] = currentAmount
    }


    Set<Book> getBooks() {
        return contents.keySet()
    }

    Integer getAmount(Book book) {
        return contents[book]
    }

    boolean containsBook(Book book) {
        return getBooks().contains(book)
    }

    void applyDiscount(Integer discount) {
        this.discount = discount
    }

    Integer getDiscount() {
        return discount ?: 0
    }

    Integer getTotal() {
        return getSubtotal() - getDiscount()
    }

    Integer getTotalItems() {
        contents.collect { k,v -> v }.sum() ?: 0
    }

    Integer getSubtotal() {
        contents.collect { k,v -> k.price * v }.sum() ?: 0
    }
}
