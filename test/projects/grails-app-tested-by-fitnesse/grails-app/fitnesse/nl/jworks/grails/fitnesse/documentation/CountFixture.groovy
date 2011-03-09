package nl.jworks.grails.fitnesse.documentation

/**
 * Used in the example for Query Tables.
 *
 * @author Erik Pragt
 */
class CountFixture {
    int number

    CountFixture(int number) {
        this.number = number
    }

    List query() {
        def table = []
        
        number.times {
            def row = []
            row << ["result", it]
            table << row
        }

        return table
    }
}