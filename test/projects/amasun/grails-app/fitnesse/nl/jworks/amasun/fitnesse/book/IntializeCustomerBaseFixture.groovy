package nl.jworks.amasun.fitnesse.book

import nl.jworks.amasun.domain.customer.Customer

/**
 * @author Erik Pragt
 */
class IntializeCustomerBaseFixture {
    String name

    def customerService

    void execute() {
        customerService.add(new Customer(name:name));
    }
}

