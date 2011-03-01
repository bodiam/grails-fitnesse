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
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class HibernateSessionSlimServer extends SlimServer {

    private final Logger log = LoggerFactory.getLogger(getClass())

    SessionFactory sessionFactory

    public HibernateSessionSlimServer(boolean verbose, SlimFactory slimFactory, SessionFactory sessionFactory) {
        super(verbose, slimFactory)
        this.sessionFactory = sessionFactory
    }

    @Override
    void serve(Socket s) {
        log.debug("Serving test request on port: $s.localPort")
        Session session = SessionFactoryUtils.getSession(sessionFactory, true)
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
        try {
            super.serve(s)
            log.debug("Successfully finished serving a test request on port: $s.localPort")
        } catch (Throwable th) {
            log.error('Unexpected exception while serving a test request', th)
        } finally {
            try {
                session.flush()
                session.disconnect()
            } catch (Throwable th) {
                log.error('Unexpected exception while closing hibernate session after serving a test request', th)
            }
        }
    }
}
