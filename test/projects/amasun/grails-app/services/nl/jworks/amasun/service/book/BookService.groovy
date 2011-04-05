package nl.jworks.amasun.service.book

import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.book.Book

class BookService {
    def inventoryService

    static transactional = true

    void placeOrder(Order order) {

        for(book in order.books) {
            def amount = order.getAmount(book)

            inventoryService.deductInventory(book, amount)
        }
    }

    Book findByIsbn(String isbn) {
        Book.findByIsbn(isbn)
    }
}


/*
    public void buyBooks(Order order) {
        Integer discount = calculateDiscount(order);

        order.applyDiscount(discount);

        // Then deduct the inventory
        for (Book book : order.getBooks()) {
            Integer amount = order.getAmount(book);

            Inventory.get().deductInventory(book, amount);
        }
    }

    private Integer calculateDiscount(Order order) {
        if (!inventory.isPromoOrder(order)) {
            return 0;
        }

        PromoPackage promoPackage = inventory.getPromoPackage(order);

        return promoPackage.caclulateDiscount();
    }

*/