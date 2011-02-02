package nl.jworks.domain

class CarProducer {
    String name
    static hasMany = [models: CarModel]
}
