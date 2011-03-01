package nl.jworks

import nl.jworks.domain.CarProducer

class InlineSaveProducerFixture {

    CarProducer producer

    void execute() {
        //CarProducer.list()*.delete(flush: true)
        producer.save()
    }

    int producersCount() {
        CarProducer.count()
    }
}
