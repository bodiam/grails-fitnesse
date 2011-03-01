package fitnesse.grails

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionSynchronizationManager

abstract class EndTransactionFixture {

    PlatformTransactionManager transactionManager

    abstract void doEndTransaction(TransactionStatus transactionStatus);

    void table(def table) {
        TransactionStatus currentTransaction = TransactionSynchronizationManager.getResource(transactionManager)
        doEndTransaction(currentTransaction)
        TransactionSynchronizationManager.unbindResource(transactionManager)
    }
}
