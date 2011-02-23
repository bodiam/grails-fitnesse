package fitnesse.grails

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.hibernate.SessionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager

class RollbackFixture {

    PlatformTransactionManager transactionManager
    SessionFactory sessionFactory

    void table(def table){
        println "rollback on: ${transactionManager.getTransaction(/*new DefaultTransactionDefinition()*/).transaction.sessionHolder.transaction}"
        TransactionStatus st = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY))
        transactionManager.rollback(st)
        println "rolledback: " + transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY)).transaction.sessionHolder.transaction.rolledBack
    }
}
