package fitnesse.grails

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionSynchronizationManager

class BeginTransactionFixture {

    PlatformTransactionManager transactionManager

    void table(def table) {
        DefaultTransactionDefinition requireNewTransactionDefinition =
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW)
        TransactionStatus transactionStatus = transactionManager.getTransaction(requireNewTransactionDefinition)
        TransactionSynchronizationManager.unbindResourceIfPossible(transactionManager)
        TransactionSynchronizationManager.bindResource(transactionManager, transactionStatus)
    }
}
