package fitnesse.grails

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.TransactionDefinition

class CommitFixture {

    PlatformTransactionManager transactionManager

    void table(def table) {
        transactionManager.commit(transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY)))
    }
}
