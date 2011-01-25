
/**
 * @author Erik Pragt
 */
class DiscountScenarioFixture {
    def discountService

    int years

    void givenARenewalOfYear(int years) {
        this.years = years
    }
    boolean asAgentIWantToGiveEurosDiscount(String agentName, int discount) {
        def agent = Agent.findByName(agentName)

       return discountService.calculateRealTotalAboluteReallyReallyTotalDiscount(agent, year) == discount as BigDecimal
    }
}
