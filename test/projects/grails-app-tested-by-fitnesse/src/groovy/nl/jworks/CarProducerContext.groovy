package nl.jworks

import nl.jworks.grails.fitnesse.feature.hibernate.CarProducer

class CarProducerContext {

    static CarProducerContext instance = new CarProducerContext()

    CarProducer producer
}
