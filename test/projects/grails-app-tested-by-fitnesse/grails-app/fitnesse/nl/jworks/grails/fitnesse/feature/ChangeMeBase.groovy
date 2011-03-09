package nl.jworks.grails.fitnesse.feature

class ChangeMeBase {
    String name
    int age
    String city

    String sentence() {
        "$name is $age years old and is from $city"
    }
}
