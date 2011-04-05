package nl.jworks.amasun.fitnesse.solution

/**
 * @author Erik Pragt
 */
class BuyBookScenarioForCustomerFixture {
    
    String customerName

    BuyBookScenarioForCustomerFixture(String customerName) {
        this.customerName = customerName
    }

    void customerBuysBooksWithTitle(Integer amount, String title) {

    }

    void placeOrder() {

    }

    Integer chargedTotal() {
        return 0
    }
}

/*
|script       |buy book scenario for customer|Rod Johnson                                                    |
|customer buys|1                |books with title|Large Scale Application Development with NCQRS|
|customer buys|1                |books with title|IT                                            |
|place order|
|check        |charged subtotal |59                                                             |
|check        |applied discount |10                                                             |
|check        |charged total    |49                                                             |

 */
