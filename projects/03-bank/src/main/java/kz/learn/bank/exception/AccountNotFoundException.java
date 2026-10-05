package kz.learn.bank.exception;

/**
 * Шот нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Account not found: KZ0001"
 */
public class AccountNotFoundException extends BankException {

    // TODO: өрістерді жаз

    public AccountNotFoundException(String number) {
        super("TODO"); // TODO: хабарламаны дұрыс құрастыр
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getNumber() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
