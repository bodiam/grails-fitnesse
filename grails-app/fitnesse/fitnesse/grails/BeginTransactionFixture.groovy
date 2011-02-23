package fitnesse.grails

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.TransactionDefinition

class BeginTransactionFixture {

    PlatformTransactionManager transactionManager

    void table(def table) {
        println "begin on: ${transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW)).transaction.sessionHolder.transaction}"
        println "begin on: ${transactionManager.getTransaction(/*new DefaultTransactionDefinition()*/).transaction.sessionHolder.transaction}"
        println "begin on: ${transactionManager.getTransaction(new DefaultTransactionDefinition()).transaction.sessionHolder.transaction}"
        //transactionManager.getTransaction(null)
    }
}
