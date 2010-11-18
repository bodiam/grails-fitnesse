package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimFactory
import fitnesse.slim.NameTranslator
import fitnesse.slim.NameTranslatorIdentity
import fitnesse.slim.StatementExecutorInterface

/**
 * @author Erik Pragt
 */
class GrailsSlimFactory extends SlimFactory {

  private NameTranslator identityTranslator = new NameTranslatorIdentity();

  public StatementExecutorInterface getStatementExecutor() throws Exception {
    return new GrailsStatementExecutor();
  }

  @Override
  public NameTranslator getMethodNameTranslator() {
    return identityTranslator;
  }
}
