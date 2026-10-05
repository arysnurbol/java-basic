package kz.learn.bank.exception;

import kz.learn.bank.model.Money;

/**
 * Шотта ақша жетпесе (комиссия мен овердрафтты ескергенде).
 * requested — сома + комиссия; available — balance + overdraftLimit.
 *
 * getMessage() пішімі (Money.toString арқылы):
 *   "Insufficient funds on KZ0001: requested 1100.00 ₸, available 500.00 ₸"
 */
public class InsufficientFundsException extends BankException {

    // TODO: өрістерді жаз

    public InsufficientFundsException(String number, Money requested, Money available) {
        super("TODO"); // TODO: хабарламаны дұрыс құрастыр
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getNumber() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getRequested() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getAvailable() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
