package nl.jworks.amasun.domain.order

import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.amasun.domain.book.Book

class Order {
    static belongsTo = [customer:Customer]

    private Map<Book, Integer> contents = [:]
    private Integer discount

    static transients = ['discount', 'total', 'subtotal']

    protected Order() {
        
    }

    Order(Customer customer) {
        this.customer = customer
    }

    static mapping = {
        table 'bookorder'
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
        return discount;
    }

    Integer getTotal() {
        println getSubtotal()
        println getDiscount()

        return getSubtotal() - getDiscount()
    }

    Integer getSubtotal() {
        contents.collect { k,v -> k.price * v }.sum()
    }
}
