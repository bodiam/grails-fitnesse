package nl.jworks.amasun.service.customer

import nl.jworks.amasun.domain.customer.Customer

class CustomerService {

    static transactional = true

    def add(Customer customer) {
        customer.save()
    }

    Customer findByName(String name) {
        Customer.findByName(name)
    }
}
