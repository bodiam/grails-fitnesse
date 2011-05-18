package nl.jworks.grails.fitnesse.feature.querydsl

class NestedPropertyKeyValueQueryFixture {
    static queryFixture = true

    static mapping = ["name":"author.name", "birthYear":"author.birthYear", "title":"title"]

    def queryResults() {
        return [new Book(title:'Grails in Action', author: new Author(name:"Peter Ledbrook", birthYear: 1980))]
    }
}
