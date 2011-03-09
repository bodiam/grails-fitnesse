package nl.jworks.grails.fitnesse.feature.hibernate

class InlineSaveProducerFixture {

    CarProducer producer

    void execute() {
        producer.save()
    }

    int producersCount() {
        CarProducer.count()
    }
}
