package nl.jworks.grails.fitnesse.feature.querydsl

class NestedPropertyQueryFixture {
    static queryFixture = true

    static mapping = ["author.name", "author.birthYear", "title"]

    def queryResults() {
        return [new Book(title:'Grails in Action', author: new Author(name:"Peter Ledbrook", birthYear: 1980))]
    }
}
