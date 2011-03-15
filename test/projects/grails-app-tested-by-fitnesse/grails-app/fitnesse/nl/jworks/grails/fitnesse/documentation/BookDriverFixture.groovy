package nl.jworks.grails.fitnesse.documentation

import nl.jworks.grails.plugin.fitnesse.Fixture


@Fixture
class BookDriverFixture {

    boolean bookAvailableFromWith(String author, String title) {
        return true
    }

    void buyBookFromAuthor2WithTitle(String author, String title) {

    }

    boolean shoppingCartContainsFromAuthor(String author, String title) {
        return true
    }
}


