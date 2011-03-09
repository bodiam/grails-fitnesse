package nl.jworks.grails.fitnesse.feature.hibernate

class CheckModelsForProducerInContextFixture {

    int size() {
        CarProducerContext.instance.producer.models.size()
    }

}
