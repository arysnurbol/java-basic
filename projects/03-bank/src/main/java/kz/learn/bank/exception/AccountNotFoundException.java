package kz.learn.bank.exception;

/**
 * Шот нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Account not found: KZ0001"
 */
public class AccountNotFoundException extends BankException {

    private final String number;

    public AccountNotFoundException(String number) {
        super("Account not found: " + number);
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
