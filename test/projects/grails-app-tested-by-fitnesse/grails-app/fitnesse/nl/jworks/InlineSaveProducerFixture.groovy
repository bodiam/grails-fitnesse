package nl.jworks

import nl.jworks.domain.CarProducer

class InlineSaveProducerFixture {

    CarProducer producer

    void execute() {
        producer.save()
    }
}
