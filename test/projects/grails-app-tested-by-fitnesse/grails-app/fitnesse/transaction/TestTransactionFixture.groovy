package transaction

import nl.jworks.domain.CarModel
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.transaction.support.TransactionCallback



class TestTransactionFixture {

    def transactionManager

    void create() {

    }

    int count() {
        CarModel.count()
    }

    void rollback() {
        new TransactionTemplate(transactionManager).execute({status ->
            new CarModel(name: "VW").save()

            status.setRollbackOnly()
        } as TransactionCallback)

    }



}