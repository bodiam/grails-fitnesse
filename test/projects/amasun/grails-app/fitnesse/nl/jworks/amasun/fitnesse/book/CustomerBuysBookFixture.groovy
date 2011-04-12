package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.amasun.service.book.BookService
import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.book.Book

class CustomerBuysBookFixture {
    private Customer customer

    BookService bookService
    Order order

    CustomerBuysBookFixture(String customerName) {
        this.customer = Customer.findByName(customerName)

        order = new Order(customer)
    }

    void buyBookWithIsbn(int amount, String isbn) {
        buyBooksWithIsbn(amount, isbn)
    }

    void buyBooksWithIsbn(int amount, String isbn) {
        Book book = bookService.findByIsbn(isbn)

        order.addBook(book, amount)
    }

    void placeOrder() {
        bookService.placeOrder(order)
    }
}
