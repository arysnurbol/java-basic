package ch03.t20_exceptions;

/**
 * Тапсырма 20 — өзіңнің CHECKED ерекше жағдайың.
 * Кітап: "Выбрасывание и создание пользовательских исключений" (156 б.)
 *
 * CHECKED vs UNCHECKED:
 *   extends Exception         -> checked. Компилятор МІНДЕТТЕЙДІ: не ұста, не throws жаз.
 *                                Шақырушы шынымен ӨҢДЕЙ алатын жағдайларға.
 *   extends RuntimeException  -> unchecked. Міндеттемейді.
 *                                Бағдарламашының қатесіне (null, қате аргумент).
 *
 * Бұл класс — checked. Ақша жетпеуі — қалыпты іскерлік жағдай, оны шақырушы өңдей алады.
 *
 * Керек:
 *   - екі final өріс: requested, available (double);
 *   - конструктор super(...) арқылы хабарлама берсін:
 *       "Qarajat jetkiliksiz: surangan 500.0, bar 300.0"
 *   - getRequested(), getAvailable(), getShortfall() (айырмасы).
 */
public class InsufficientFundsException extends Exception {

    private final double requested;
    private final double available;

    public InsufficientFundsException(double requested, double available) {
        // Тапсырмада сұралған хабарлама форматын құрастырамыз:
        super("Qarajat jetkiliksiz: surangan " + requested + ", bar " + available);
        this.requested = requested;
        this.available = available;
    }

    public double getRequested() {
        return requested;
    }

    public double getAvailable() {
        return available;
    }

    /** Қанша ақша жетпей тұр. */
    public double getShortfall() {
        return requested - available;
    }
}
