package nl.jworks.grails.fitnesse.feature.hibernate

class CreateCarProducerFixture {

    def carProducerService

    CarProducer producer

    public CreateCarProducerFixture(String name) {
        producer = new CarProducer(name: name)
    }

    public void setModel(String name) {
        producer.addToModels(new CarModel(name: name))
        carProducerService.save(producer)
    }
}
