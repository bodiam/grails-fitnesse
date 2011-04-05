package nl.jworks.amasun.domain.order

import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.amasun.domain.book.Book

class Order {
    static belongsTo = [customer:Customer]

    Map<Book, Integer> contents = [:]

    Order(Customer customer) {
        this.customer = customer
    }

    static constraints = {
    }

    static mapping = {
        table 'bookorder'
    }

    void addBook(Book book, Integer amount) {
        def currentAmount = contents[book]

        if(!currentAmount) {
            currentAmount = 0
        }

        currentAmount += amount

        contents[book] = currentAmount
    }

    Set<Book> getBooks() {
        return contents.keySet()
    }

    Integer getAmount(Book book) {
        return contents[book]
    }

    boolean containsBook(Book book) {
        return getBooks().contains(book)
    }
}


/*
    private Customer customer;

    private Map<Book, Integer> contents = new HashMap<Book, Integer>();
    private Integer discount;

    public Order(Customer customer) {
        this.customer = customer;
    }

    public void addBook(Integer amount, Book book) {
        Integer currentAmount = contents.get(book);

        if(currentAmount == null) {
            currentAmount = 0;
        }

        currentAmount += amount;

        contents.put(book, currentAmount);
    }

    public Set<Book> getBooks() {
        return contents.keySet();
    }

    public Integer getAmount(Book book) {
        return contents.get(book);
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean containsBook(Book book) {
        return contents.keySet().contains(book);
    }

    public Integer getDiscount() {
        return discount;
    }

    public Integer getTotal() {
        return getSubtotal() - getDiscount();
    }

    public Integer getSubtotal() {
        Integer totalPrice = 0;

        for (Map.Entry<Book, Integer> entry : contents.entrySet()) {
            totalPrice += (entry.getKey().getPrice() * entry.getValue());
        }
        return totalPrice;
    }

    public void applyDiscount(Integer discount) {
        this.discount = discount;
    }
*/