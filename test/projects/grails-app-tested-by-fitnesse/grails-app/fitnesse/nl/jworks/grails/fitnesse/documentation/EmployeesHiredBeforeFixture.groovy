package nl.jworks.grails.fitnesse.documentation

/**
 * Fixture for the SubSetQuery table.
 */
class EmployeesHiredBeforeFixture {
    Date hireDate

    EmployeesHiredBeforeFixture(Date hireDate) {
        this.hireDate = hireDate
    }


    List query() {
        [
                [
                        ["employee number", "8832"],
                        ["first name", "James"],
                        ["last name", "Grenning"],
                        ["hire date", "12-May-1999"]
                ],
                [
                        ["employee number", "1429"],
                        ["first name", "Bob"],
                        ["last name", "Martin"],
                        ["hire date", "10-Oct-1975"]
                ],
                [
                        ["employee number", "9924"],
                        ["first name", "Bill"],
                        ["last name", "Mitchell"],
                        ["hire date", "19-Dec-1966"]
                ],
        ]
    }
}
