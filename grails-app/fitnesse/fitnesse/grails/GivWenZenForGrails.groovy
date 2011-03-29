package fitnesse.grails

import nl.jworks.grails.plugin.fitnesse.Fixture

import org.givwenzen.DomainStepFinder
import org.givwenzen.GivWenZenExecutorCreator
import org.givwenzen.GivWenZenForSlim
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

@Fixture
class GivWenZenForGrails extends GivWenZenForSlim {
	GivWenZenForGrails() {
        super(GivWenZenExecutorCreator
                .instance()
                .stepClassBasePackage(CH.config.grails.plugin.fitnesse.givWenZen.packageName ?: "bdd.steps")
                .create()
        );
	}
}