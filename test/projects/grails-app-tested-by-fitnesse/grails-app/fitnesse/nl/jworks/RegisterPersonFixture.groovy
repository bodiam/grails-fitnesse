package nl.jworks

import nl.jworks.domain.Person

/**
 * @author Erik Pragt
 */
class RegisterPersonFixture {
    def personService

    String name
    int age

    // TODO: |register person|name:erik, age:10] ??
    // | register person   |
    // |with name | erik |
    // |with age | 10 |

    void registerPersonWithNameAndAge(String name, int age) {
        println "Currently ${Person.count()} persons"

        personService.registerPerson(new Person(name:name, age:age))

        println "Now ${Person.count()} persons!"
    }

    boolean "register user"() {
        true
    }


}
