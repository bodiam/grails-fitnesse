package nl.jworks.amasun.service.book

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.Inventory
import nl.jworks.amasun.domain.book.PromoPackage
import nl.jworks.amasun.domain.order.Order

class InventoryService {

    void addBooks(Book book, delta) {
        //Inventory inventory = Inventory.find(new Inventory(book:book))
        Inventory inventory = findByAuthorAndTitle(book.author, book.title)

        if(!inventory) {
            inventory = new Inventory(book:book)
        }
        
        inventory.incrementAmount(delta)
        inventory.save()
    }


    Book findByTitle(String title) {
        Book.findByTitle(title)
    }


    Integer getAmount(Book book) {
        return findByAuthorAndTitle(book.author, book.title).amount
    }


    void deductInventory(Book book, Integer amount) {
        def inventory = findByAuthorAndTitle(book.author, book.title)

        if(inventory.amount - amount < 0) {
            throw new IllegalArgumentException("Not enough books in stock: current stock is ${stock}, trying to deduct ${amount}")
        }

        inventory.amount = inventory.amount - amount
        inventory.save()
    }


    private Inventory findByAuthorAndTitle(author, title) {
        Inventory.find("from Inventory as i where i.book.author = :author and i.book.title = :title", [author:author, title:title])
    }

    void addPromoPackage(PromoPackage promoPackage) {
        promoPackage.save()
    }

    public PromoPackage getPromoPackage(Order order) {
        PromoPackage.list().find { promopackage -> promopackage.isEligible(order) }
    }

    public boolean isPromoOrder(Order order) {
        return getPromoPackage(order) != null
    }
}

/*
package nl.devnology.domain.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.devnology.domain.order.Order;

public class Inventory {
    private static Inventory inventory = new Inventory();

    private static Map<Book, Integer> books = new HashMap<Book, Integer>();
    private static List<PromoPackage> promoPackages = new ArrayList<PromoPackage>();

    public static Inventory get() {
        return inventory;
    }


    public void addBook(Book book, Integer amount) {
        Integer currentAmount = books.get(book);

        if(currentAmount == null) {
            currentAmount = amount;
        } else {
            currentAmount += amount;
        }

        books.put(book, currentAmount);
    }

    public Book findByTitle(String title) {
        for(Map.Entry<Book, Integer> entry: books.entrySet()) {
            Book book = entry.getKey();
            if(book.getTitle().equals(title)) {
                return book;
            }
        }

        return null;
    }

    public Book findByIsbn(String isbn) {
        for(Map.Entry<Book, Integer> entry: books.entrySet()) {
            Book book = entry.getKey();
            if(book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        return null;

    }

    public Integer countBooks(Book book) {
        return books.get(book);
    }

    public void deductInventory(Book book, Integer amount) {
        Integer stock = books.get(book);

        if(stock - amount < 0) {
            throw new IllegalArgumentException("Not enough books in stock: current stock is " + stock + ", trying to deduct " + amount);
        }

        stock -= amount;

        books.put(book, stock);
    }

    public void addPromoPackage(PromoPackage promoPackage) {
        promoPackages.add(promoPackage);
    }


    public PromoPackage getPromoPackage(Order order) {
        for (PromoPackage promoPackage : promoPackages) {
            if(promoPackage.isEligible(order)) {
                return promoPackage;
            }
        }
        return null;

    }

    public boolean isPromoOrder(Order order) {
        for (PromoPackage promoPackage : promoPackages) {
            if(promoPackage.isEligible(order)) {
                return true;
            }
        }
        return false;
    }
}

*/