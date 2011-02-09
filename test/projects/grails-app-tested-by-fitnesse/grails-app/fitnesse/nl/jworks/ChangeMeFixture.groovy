package nl.jworks

/**
 * Use this class for testing the reloading of changes made to this fixture.
 * 
 * @author Erik Pragt
 */
class ChangeMeFixture {
    String name
    int age
    String city

    String sentence() {
        "$name is $age years old and is from $city"
    }
}
