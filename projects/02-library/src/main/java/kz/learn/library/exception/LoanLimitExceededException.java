package kz.learn.library.exception;

/**
 * Оқырман рұқсат етілгеннен көп экземпляр алмақ болды.
 * getMessage() пішімі: "Loan limit exceeded: S-1 (max 3)"
 */
public class LoanLimitExceededException extends LibraryException {

    private final int limit;
    private final String cardNumber;

    public LoanLimitExceededException(String cardNumber, int limit) {
        super("Loan limit exceeded: " + cardNumber + " (max " + limit + ")");
        this.cardNumber = cardNumber;
        this.limit = limit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getLimit() {
        return limit;
    }
}
