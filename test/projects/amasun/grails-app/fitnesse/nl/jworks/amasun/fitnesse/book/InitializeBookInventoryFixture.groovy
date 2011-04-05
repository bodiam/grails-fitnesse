package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.Inventory
import nl.jworks.amasun.domain.book.PromoPackage

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
    }

    void reset() {
        book = new Book()
    }

    void execute() {
        inventoryService.addBooks(book, amount)
    }

}


/*
   (ApplicationHolder.application.getArtefacts("Domain") as List).each {
      it.newInstance().list()*.delete()
    }
    sessionFactory.currentSession.flush()
    sessionFactory.currentSession.clear()
 */
