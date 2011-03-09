package nl.jworks.grails.fitnesse.feature.hibernate

import nl.jworks.CarProducerContext

class CheckModelsForProducerInContextFixture {

    int size() {
        CarProducerContext.instance.producer.models.size()
    }

}
