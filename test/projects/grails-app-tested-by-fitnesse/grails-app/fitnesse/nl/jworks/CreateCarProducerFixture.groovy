package nl.jworks

import nl.jworks.domain.CarProducer
import nl.jworks.domain.CarModel

class CreateCarProducerFixture {

    def carProducerService

    CarProducer producer

    public CreateCarProducerFixture(String name) {
        CarProducer.list()*.delete()
        producer = new CarProducer(name: name)
    }

    public void setModel(String name) {
        producer.addToModels(new CarModel(name: name))
        carProducerService.save(producer)
    }
}
