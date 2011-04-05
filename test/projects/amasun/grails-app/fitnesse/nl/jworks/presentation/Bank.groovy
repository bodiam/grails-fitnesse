package nl.jworks.presentation

/**
 * @author Erik Pragt
 */
class Bank {
    def accounts = [:]

    void deposit(int amount, String account) {
        def money = accounts[account]

        if(!money) {
            money = 0
        }

        money += amount

        accounts[account] = money
    }

    int getAmount(String account) {
        accounts[account]
    }
}
