package nl.jworks.amasun.service.book

import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.PromoPackage

class BookService {
    def inventoryService

    static transactional = true

    void placeOrder(Order order) {
        Integer discount = calculateDiscount(order)
        order.applyDiscount(discount)

        order.books.each { Book book ->
            def amount = order.getAmount(book)

            inventoryService.deductInventory(book, amount)
        }

        order.save()
    }

    Book findByIsbn(String isbn) {
        Book.findByIsbn(isbn)
    }

    private Integer calculateDiscount(Order order) {
        if (!inventoryService.isPromoOrder(order)) {
            return 0
        }

        PromoPackage promoPackage = inventoryService.getPromoPackage(order)

        return promoPackage.calculatedDiscount
    }
}
