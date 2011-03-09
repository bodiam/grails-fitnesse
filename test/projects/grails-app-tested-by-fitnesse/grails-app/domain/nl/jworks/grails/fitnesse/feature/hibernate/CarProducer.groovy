package nl.jworks.grails.fitnesse.feature.hibernate

class CarProducer {
    String name
    static hasMany = [models: CarModel]
}
