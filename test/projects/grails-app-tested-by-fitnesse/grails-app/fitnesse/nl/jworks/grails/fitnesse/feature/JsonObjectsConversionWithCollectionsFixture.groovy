package nl.jworks.grails.fitnesse.feature

import nl.jworks.grails.fitnesse.feature.hibernate.CarProducer
import nl.jworks.grails.fitnesse.feature.hibernate.CarModel

class JsonObjectsConversionWithCollectionsFixture {
    CarProducer producer

    List<CarModel> models

    boolean match() {
        producer.models*.name.containsAll(models*.name)
    }
}
