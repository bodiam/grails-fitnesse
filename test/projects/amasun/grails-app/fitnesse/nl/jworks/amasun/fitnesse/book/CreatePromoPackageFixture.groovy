package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.book.DiscountType
import nl.jworks.amasun.domain.book.PromoPackage

/**
 * @author Erik Pragt
 */
class CreatePromoPackageFixture {

    def bookService
    def inventoryService

    String book1Isbn
    String book2Isbn
    Integer discountAmount

    private String discountType

    void execute() {
        def book1 = bookService.findByIsbn(book1Isbn)
        def book2 = bookService.findByIsbn(book2Isbn)

        inventoryService.addPromoPackage(new PromoPackage(book1:book1, book2:book2, discountAmount:discountAmount))
    }

    // TODO: This conversion should be gone after 1.1 plugin has been built
    void setDiscountType(String x) {
        println "x" + x

        println DiscountType.valueOf(x)

        println "y" + x
    }

}



/**

 public class CreatePromoPackage {
     private BookService bookService = new BookService();

     private Inventory inventory;
     private Book book1;
     private Book book2;
     private Integer discountAmount;
     private DiscountType discountType;

     public CreatePromoPackage() {
         inventory = Inventory.get();
     }

     public void setBook1Isbn(String book1isbn) {
         book1 = inventory.findByIsbn(book1isbn);
     }

     public void setBook2Isbn(String book2isbn) {
         book2 = inventory.findByIsbn(book2isbn);
     }

     public void setDiscountAmount(Integer discount) {
         discountAmount = discount;
     }

     public void setDiscountType(String type) {
         discountType = DiscountType.valueOf(type);
     }

     public void execute() {
         inventory.addPromoPackage(new PromoPackage(book1, book2, discountAmount, discountType));

     }
 }
*/