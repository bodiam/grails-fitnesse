package fitnesse.grails

import org.springframework.transaction.TransactionStatus

class RollbackFixture extends EndTransactionFixture {

    @Override
    void doEndTransaction(TransactionStatus transactionStatus) {
        transactionManager.rollback(transactionStatus)
    }
}
