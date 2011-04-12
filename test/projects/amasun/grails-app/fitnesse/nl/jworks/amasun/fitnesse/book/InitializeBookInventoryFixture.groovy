package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.Inventory
import nl.jworks.amasun.domain.book.PromoPackage
import nl.jworks.amasun.domain.order.Order

/**
 * @author Erik Pragt
 */
class InitializeBookInventoryFixture {
    @Delegate
    Book book

    int amount

    def inventoryService

    InitializeBookInventoryFixture() {
        PromoPackage.list()*.delete()
        Inventory.list()*.delete()
        Order.list()*.delete()
    }

    void reset() {
        book = new Book()
    }

    void execute() {
        inventoryService.addBooks(book, amount)
    }
}
