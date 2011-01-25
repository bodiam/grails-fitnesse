package nl.jworks.service

class PersonService {

    static transactional = true

    def registerPerson(p) {
        assert p.save()
    }
}
