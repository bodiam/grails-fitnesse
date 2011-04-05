package nl.jworks.amasun.domain.book

import nl.jworks.amasun.domain.order.Order


class PromoPackage {
    static belongsTo = Book   

    Book book1
    Book book2
    Integer discountAmount
    //DiscountType discountType

    static constraints = {
    }

    public boolean isEligible(Order order) {
        return order.containsBook(book1) && order.containsBook(book2)
    }

//    public Integer caclulateDiscount() {
//        if(discountType == DiscountType.AMOUNT) {
//            return discountAmount;
//        } else {
//            return (int) ((book1.getPrice() + book2.getPrice()) * (discountAmount / 100d));
//        }
//    }
        
}
