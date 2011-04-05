package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.amasun.domain.order.Order

/**
 * @author Erik Pragt
 */
class BuyBookScenarioFixture {
    def inventoryService
    def customerService
    def bookService

    String name

    void customerBuysBooksWithTitle(String name, Integer amount, String title) {
        Book book = inventoryService.findByTitle(title)

        Customer customer = customerService.findByName(name)

        Order order = new Order(customer)
        order.addBook(book, amount)

        bookService.placeOrder(order)
    }
}

/*
        Book book = Inventory.get().findByTitle(title);

        Customer customer = CustomerRepository.findByName(customerName);

        Order order = new Order(customer);
        order.addBook(amount, book);

        bookService.buyBooks(order);

        return true;

 */
