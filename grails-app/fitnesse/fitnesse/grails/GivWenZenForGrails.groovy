package fitnesse.grails

import nl.jworks.grails.plugin.fitnesse.Fixture

import org.givwenzen.DomainStepFinder
import org.givwenzen.GivWenZenExecutorCreator
import org.givwenzen.GivWenZenForSlim

@Fixture
class GivWenZenForGrails extends GivWenZenForSlim {
	GivWenZenForGrails() {
		super(GivWenZenExecutorCreator.instance().domainStepFinder(new DomainStepFinder("nl.jworks.grails.fitnesse.gwz")).create()); // TODO configurable package name
	}
}