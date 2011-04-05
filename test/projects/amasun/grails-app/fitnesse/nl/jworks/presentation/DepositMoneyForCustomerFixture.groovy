package nl.jworks.presentation

/**
 * @author Erik Pragt
 */
class DepositMoneyForCustomerFixture {
    private Bank bank = new Bank()

    String account

    DepositMoneyForCustomerFixture(String account) {
        this.account = account
    }

    void deposit(int money) {
        bank.deposit(money, account)
    }

    int totalDeposits() {
        bank.getAmount(account)
    }
}