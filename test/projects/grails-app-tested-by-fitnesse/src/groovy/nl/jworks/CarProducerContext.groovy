package nl.jworks

import nl.jworks.domain.CarProducer

class CarProducerContext {

    static CarProducerContext instance = new CarProducerContext()

    CarProducer producer
}
