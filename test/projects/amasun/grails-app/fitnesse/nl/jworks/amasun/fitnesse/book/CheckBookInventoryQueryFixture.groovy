package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.service.book.InventoryService

class CheckBookInventoryQueryFixture {
    static queryFixture = true

    static mapping = ["author", "title", "amount"]

    InventoryService inventoryService

    def queryResults() {
        inventoryService.checkInventory()
    }
}
