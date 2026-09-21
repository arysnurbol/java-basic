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

    // TODO: екі final өріс

    public InsufficientFundsException(double requested, double available) {
        super(""); // TODO: хабарламаны дұрыс құрастыр
        // TODO: өрістерді меншікте
    }

    public double getRequested() {
        return 0; // TODO
    }

    public double getAvailable() {
        return 0; // TODO
    }

    /** Қанша ақша жетпей тұр. */
    public double getShortfall() {
        return 0; // TODO
    }
}
