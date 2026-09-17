package ch02.t14_encapsulation;

/**
 * Тапсырма 14 — Инкапсуляция: деректі жасырып, өзгертуді методтар арқылы бақылау.
 * Кітап: "Инкапсуляция" (104-б.)
 *
 * ЕРЕЖЕ: balance үшін setBalance() ЖАЗБА. Баланс тек deposit/withdraw арқылы өзгереді.
 */
public class BankAccount {

    // TODO: екі private өріс: owner (String), balance (double)

    /**
     * initialBalance теріс болса — IllegalArgumentException.
     * owner null немесе бос болса — IllegalArgumentException.
     */
    public BankAccount(String owner, double initialBalance) {
        // TODO
    }

    public String getOwner() {
        // TODO
        return null;
    }

    public double getBalance() {
        // TODO
        return 0;
    }

    /** amount <= 0 болса — IllegalArgumentException. */
    public void deposit(double amount) {
        // TODO
    }

    /**
     * amount <= 0 болса — IllegalArgumentException.
     * amount > balance болса — IllegalStateException.
     */
    public void withdraw(double amount) {
        // TODO
    }
}
