package nl.jworks.amasun.controller.shop

import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.order.Order
import nl.jworks.amasun.domain.customer.Customer

class ShopController {
    def bookService

    def index = {
        render(view: 'featured')
    }

    def details = {
        [book: Book.get(params.id)]
    }

    def cart = {
        Order order = session.order
        if (!order) {
            order = new Order(new Customer(name: 'Erik Pragt'))
            session.order = order
        }

        if (params.id) {
            def book = Book.get(params.id)

            bookService.orderBook(order, book, 1)
        }

        [order: order]
    }

    def about = { }
}
