package nl.jworks.amasun.domain.book

class Inventory {
    Book book
    Integer amount = 0

    void incrementAmount(Integer delta) {
        this.amount += delta
    }

    static constraints = {
    }    
}
