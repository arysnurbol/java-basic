package ch02.t14_encapsulation;

/**
 * Тапсырма 14 — Инкапсуляция: деректі жасырып, өзгертуді методтар арқылы бақылау.
 * Кітап: "Инкапсуляция" (104-б.)
 *
 * ЕРЕЖЕ: balance үшін setBalance() ЖАЗБА. Баланс тек deposit/withdraw арқылы өзгереді.
 */
public class BankAccount {

    private String owner;
    private double balance;

    /**
     * initialBalance теріс болса — IllegalArgumentException.
     * owner null немесе бос болса — IllegalArgumentException.
     */
    public BankAccount(String owner, double initialBalance) {
        if  (owner == null) {
            throw new IllegalArgumentException("owner is null");
        } else if  (owner.equals("")) {
            throw new IllegalArgumentException("owner is empty");
        }
        if  (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance is negative");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    /** amount <= 0 болса — IllegalArgumentException. */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount is negative");
        }
        balance += amount;
    }

    /**
     * amount <= 0 болса — IllegalArgumentException.
     * amount > balance болса — IllegalStateException.
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount is negative");
        }
        if  (amount > balance) {
            throw new IllegalStateException("amount is greater than balance");
        }
        balance -= amount;
    }
}
