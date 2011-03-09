package nl.jworks.grails.fitnesse.feature

import nl.jworks.grails.fitnesse.tutorial.Book

/**
 * @author Erik Pragt
 */
class QuotedMethodNameFixture {

    boolean "register user"() {
        new Book(author:'Erik') // heck do I know why this is needed!! It doesn't compile when I don't put it in here...

        return true
    }
}
