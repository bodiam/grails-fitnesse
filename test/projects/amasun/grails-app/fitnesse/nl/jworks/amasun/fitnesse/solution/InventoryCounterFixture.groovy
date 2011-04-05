package nl.jworks.amasun.fitnesse.solution

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.Inventory

/**
 * @author Erik Pragt
 */
class InventoryCounterFixture {
    int totalNumberOfBooks() {
        Inventory.list()*.amount.sum()
    }

    int numberOfUniqueBooks() {
        Book.count()
    }


}
