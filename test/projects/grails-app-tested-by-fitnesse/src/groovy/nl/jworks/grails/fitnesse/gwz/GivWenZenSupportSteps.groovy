package nl.jworks.grails.fitnesse.gwz

import org.givwenzen.annotations.DomainStep
import org.givwenzen.annotations.DomainSteps

@DomainSteps
class GivWenZenSupportSteps {
	private int number

	@DomainStep("the number (\\d+)")
	void setNumber(int number) {
		this.number = number
	}

	@DomainStep("incrementing it by (\\d+)")
	void incrementNumber(int by) {
		number += by
	}

	@DomainStep("the result is (\\d+)")
	boolean expect(int result) {
		number == result
	}
}