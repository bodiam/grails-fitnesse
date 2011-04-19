package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.customer.Customer
import nl.jworks.grails.plugin.fitnesse.Fixture

@Fixture
class CheckOrderForCustomer {
    @Delegate
    private Order order

    CheckOrderForCustomer(String name) {
        this.order = Order.findByCustomer(Customer.findByName(name))
    }

    boolean discountIs(Integer amount) {
        order.discount == amount
    }
}
