package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.service.book.InventoryService
import nl.jworks.amasun.domain.book.Book

/**
 * @author Erik Pragt
 */
class CheckBookInventoryFixture {
    @Delegate
    Book book

    Integer amount

    InventoryService inventoryService

    void reset() {
        book = new Book()
    }

    void execute() {
        this.amount = inventoryService.getAmount(book)
    }
}
