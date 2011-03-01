package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimFactory
import fitnesse.slim.NameTranslator
import fitnesse.slim.NameTranslatorIdentity
import fitnesse.slim.StatementExecutorInterface
import fitnesse.slim.SlimServer
import org.hibernate.SessionFactory
import org.springframework.transaction.PlatformTransactionManager

/**
 * @author Erik Pragt
 */
class GrailsSlimFactory extends SlimFactory {

    SessionFactory sessionFactory
    private NameTranslator identityTranslator = new NameTranslatorIdentity()

    public StatementExecutorInterface getStatementExecutor() throws Exception {
        return new GrailsStatementExecutor()
    }

    @Override
    public NameTranslator getMethodNameTranslator() {
        return identityTranslator
    }

    @Override
    SlimServer getSlimServer(boolean verbose) {
        return new HibernateSessionSlimServer(verbose, this, sessionFactory)
    }


}
