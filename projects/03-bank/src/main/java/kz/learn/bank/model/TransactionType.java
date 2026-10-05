package kz.learn.bank.model;

/**
 * Транзакция түрі. Өрісі бар enum: әр константа өз мәнін конструктор арқылы алады.
 * credit = true — ақша шотқа КІРЕДІ (баланс өседі), false — ШЫҒАДЫ.
 */
public enum TransactionType {
    DEPOSIT(true),
    WITHDRAWAL(false),
    TRANSFER_IN(true),
    TRANSFER_OUT(false),
    FEE(false);

    // TODO: өрісті жаз

    TransactionType(boolean credit) {
        // TODO: өрісті сақта (мұнда exception лақтырма — enum жүктелмей қалады)
    }

    public boolean isCredit() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
