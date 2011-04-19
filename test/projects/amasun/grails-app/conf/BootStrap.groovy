import nl.jworks.amasun.domain.book.Book
import nl.jworks.amasun.domain.book.Inventory
import nl.jworks.amasun.domain.book.PromoPackage
import nl.jworks.amasun.domain.book.DiscountType

class BootStrap {

    def init = { servletContext ->

        if (Book.count() == 0) {
            assert new Inventory(
                    book:new Book(
                            author: 'Peter Ledbrook',
                            title: 'Grails in Action',
                            isbn: '0-8352-2051-6',
                            price: 10,
                            details: "Grails in Action is a comprehensive look at Grails for Java developers. It covers the nuts and bolts of the core Grails components and is jam-packed with tutorials, techniques, and insights from the trenches."
                    ),
                    amount:5
            ).save()

            assert new Inventory(
                    book:new Book(
                            author: 'Dierk Koenig',
                            title: 'Groovy in Action',
                            isbn: '0-8352-3891-9',
                            price: 25,
                            details: "Groovy in Action introduces Groovy by example, presenting lots of reusable code while explaining the underlying concepts. Java developers new to Groovy find a smooth transition into the dynamic programming world. Groovy experts gain a solid reference that challenges them to explore Groovy deeply and creatively."
                    ),
                    amount:60
            ).save()

            assert new Inventory(
                    book:new Book(
                            author: 'Jason Rudolph',
                            title: 'Getting started with Grails',
                            isbn: '0-8352-8936-1',
                            price: 49,
                            details: "Grails is an open-source, rapid web application development framework that provides a super-productive full-stack programming model based on the Groovy scripting language and built on top of Spring, Hibernate, and other standard Java frameworks. Ruby on Rails pioneered the innovative coupling of a powerful programming language and an opinionated framework that favors sensible defaults over complex configuration, but many organizations aren't yet ready to stray from the safety of Java or forgo their current Java investments. Grails makes it possible to achieve equivalent productivity in a Java-centric environment. Over the course of this book, the reader will explore the various aspects of Grails and also experience Grails by building a Grails app."
                    ),
                    amount:60
            ).save()

            assert new PromoPackage(
                    book1:Book.findByTitle('Grails in Action'),
                    book2:Book.findByTitle('Groovy in Action'),
                    discountAmount: 10,
                    discountType: DiscountType.AMOUNT
            ).save()
        }
    }
    def destroy = {
    }
}
