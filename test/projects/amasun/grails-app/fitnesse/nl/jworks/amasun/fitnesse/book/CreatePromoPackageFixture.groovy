package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.book.DiscountType
import nl.jworks.amasun.domain.book.PromoPackage
import nl.jworks.amasun.service.book.InventoryService
import nl.jworks.amasun.service.book.BookService

/**
 * @author Erik Pragt
 */
class CreatePromoPackageFixture {

    BookService bookService
    InventoryService inventoryService

    String book1Isbn
    String book2Isbn
    Integer discountAmount

    DiscountType discountType

    void execute() {
        def book1 = bookService.findByIsbn(book1Isbn)
        def book2 = bookService.findByIsbn(book2Isbn)

        inventoryService.addPromoPackage(new PromoPackage(book1:book1, book2:book2, discountAmount:discountAmount, discountType:discountType))
    }
}