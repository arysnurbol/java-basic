package kz.learn.library.exception;

/**
 * Оқырман рұқсат етілгеннен көп экземпляр алмақ болды.
 * getMessage() пішімі: "Loan limit exceeded: S-1 (max 3)"
 */
public class LoanLimitExceededException extends LibraryException {

    // TODO: өрістерді жаз

    public LoanLimitExceededException(String cardNumber, int limit) {
        super("TODO"); // TODO: хабарламаны дұрыс құрастыр
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getCardNumber() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getLimit() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
