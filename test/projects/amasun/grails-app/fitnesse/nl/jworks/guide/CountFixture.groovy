package nl.jworks.guide

/**
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

/*


|calculate fixture               |
|operand 1|operand 2|expectation?|
|1        |3        |4           |
|9        |1        |10          |
|5        |5        |10          |

|calculate fixture|5                     |
|operand 1        |operand 2|expectation?|
|1                |3        |9           |
|9                |1        |15          |
|5                |5        |15          |

|script|calculate script            |
|check |that adding|3|and|4|equals|7|

!|nl.jworks.DeductFixture   |
|operand 1|operand 2|result?|
|1        |3        |-2     |

|import   |
|nl.jworks|

|deduct fixture             |
|operand 1|operand 2|result?|
|1        |3        |-2     |
|1        |11       |-10    |
|100      |80       |20     |
|35       |1        |34     |
|10       |6        |4      |
|999      |999      |0      |
|500      |106      |394    |
*/