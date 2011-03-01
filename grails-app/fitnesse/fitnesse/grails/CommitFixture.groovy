package fitnesse.grails

import org.springframework.transaction.TransactionStatus

class CommitFixture extends EndTransactionFixture {

    @Override
    void doEndTransaction(TransactionStatus transactionStatus) {
        transactionManager.commit(transactionStatus)
    }
}
