package nl.jworks

import nl.jworks.domain.CarProducer

class LoadProducerAndPutInContextFixture {

    public LoadProducerAndPutInContextFixture(String name) {
        CarProducer producer = CarProducer.findByName(name)
        CarProducerContext.instance.producer = producer
    }

}
