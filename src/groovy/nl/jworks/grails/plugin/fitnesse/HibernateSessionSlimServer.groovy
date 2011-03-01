package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimFactory
import fitnesse.slim.SlimServer
import org.hibernate.SessionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.hibernate.Session
import org.springframework.orm.hibernate3.SessionHolder

class HibernateSessionSlimServer extends SlimServer {

    SessionFactory sessionFactory

    public HibernateSessionSlimServer(boolean verbose, SlimFactory slimFactory, SessionFactory sessionFactory) {
        super(verbose, slimFactory)
        this.sessionFactory = sessionFactory
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
            session.disconnect()
        }
    }
}
