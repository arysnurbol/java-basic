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

    private final String number;
    private final Money requested;
    private final Money available;

    public InsufficientFundsException(String number, Money requested, Money available) {
        super("Insufficient funds on " + number + ": requested " + requested + ", available " + available);
        this.number = number;
        this.requested = requested;
        this.available = available;
    }

    public String getNumber() {
        return number;
    }

    public Money getRequested() {
        return requested;
    }

    public Money getAvailable() {
        return available;
    }
}
