package nl.jworks.amasun.service.book

import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.PromoPackage

class BookService {
    def inventoryService

    static transactional = true

    void placeOrder(Order order) {
        order.books.each { Book book ->
            def amount = order.getAmount(book)

            inventoryService.deductInventory(book, amount)
        }

        order.save()
    }

    Book findByIsbn(String isbn) {
        Book.findByIsbn(isbn)
    }

    void orderBook(Order order, Book book, Integer amount) {
        order.addBook(book, amount)

        Integer discount = calculateDiscount(order)
        order.applyDiscount(discount)
    }

    private Integer calculateDiscount(Order order) {
        if (!inventoryService.isPromoOrder(order)) {
            return 0
        }

        PromoPackage promoPackage = inventoryService.getPromoPackage(order)

        return promoPackage.calculatedDiscount
    }
}
