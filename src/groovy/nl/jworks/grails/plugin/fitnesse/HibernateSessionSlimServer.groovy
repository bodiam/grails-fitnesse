package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimFactory
import fitnesse.slim.SlimServer
import org.hibernate.SessionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.hibernate.Session
import org.springframework.orm.hibernate3.SessionHolder
import org.hibernate.Transaction
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import org.springframework.jdbc.datasource.ConnectionHolder

class HibernateSessionSlimServer extends SlimServer {

    SessionFactory sessionFactory
    PlatformTransactionManager transactionManager

    public HibernateSessionSlimServer(boolean verbose, SlimFactory slimFactory, SessionFactory sessionFactory, PlatformTransactionManager transactionManager) {
        super(verbose, slimFactory)
        this.sessionFactory = sessionFactory
        this.transactionManager = transactionManager
    }

    @Override
    void serve(Socket s) {
        Session session = SessionFactoryUtils.getSession(sessionFactory, true)
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
        try {
            super.serve(s)
        } catch (Throwable th) {
            throw th
        } finally {
            session.flush()
            if (!TransactionSynchronizationManager.actualTransactionActive) {
                session.disconnect()
            } else {
                /*
                 * TODO: this throws a strange exception, maybe it's a bug in Hibernate? Something should be done
                 * about releasing the connection here
                 */
                //session.disconnect()
            }
        }
    }
}