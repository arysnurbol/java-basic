package ch03.t20_exceptions;

import java.util.List;
import java.util.ArrayList;

/**
 * Тапсырма 20 — Ерекше жағдайларды өңдеу.
 * Кітап: "Понятие исключений", "Базовая обработка исключений с помощью try, catch и finally",
 *        "Выбрасывание и создание пользовательских исключений" (155–157 б.)
 *
 * ИЕРАРХИЯ:
 *   Throwable
 *     +-- Error              JVM-нің өлімші қатесі (OutOfMemoryError). ҰСТАМА.
 *     +-- Exception          checked
 *           +-- RuntimeException   unchecked (NPE, IllegalArgument, NumberFormat...)
 *
 * finally ӘРҚАШАН орындалады — return жасасаң да, exception лақтырсаң да.
 * Бірақ ресурс жабу үшін оны қолданба: try-with-resources әлдеқайда қысқа әрі қауіпсіз.
 *
 * ЕКІ ЖАМАН ӘДЕТ:
 *   catch (Exception e) { }   — қатені жұту. Ешқашан олай жасама.
 *   catch (Exception e) { throw new RuntimeException(e.getMessage()); }
 *                             — СЕБЕПТІ жоғалту. Дұрысы: new RuntimeException(msg, e).
 */
public class ExceptionLab {

    /** Санға айналдырады; болмаса fallback. Кеңес: catch (NumberFormatException e). */
    public static int parseOrDefault(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * try / catch / finally блоктарының орындалу РЕТІН жазады.
     * shouldThrow == false -> ["try", "finally"]
     * shouldThrow == true  -> ["try", "catch", "finally"]
     * Яғни try ішінде "try" қос, сосын шарт бойынша exception лақтыр,
     * catch-та "catch", finally-де "finally" қос та, тізімді қайтар.
     */
    public static List<String> executionOrder(boolean shouldThrow) {
        List<String> log = new ArrayList<>();
        try {
            log.add("try");
            if (shouldThrow) {
                // Егер true болса, қолмен қате лақтырамыз
                throw new RuntimeException("Qate");
            }
        } catch (RuntimeException e) {
            log.add("catch");
        } finally {
            // finally блогы қате болса да, болмаса да ӘРҚАШАН орындалады
            log.add("finally");
        }
        return log;
    }

    /**
     * КӨП ҰСТАУ (multi-catch): catch (NumberFormatException | NullPointerException e).
     *   null   -> "null"
     *   "abc"  -> "san emes"
     *   "42"   -> "42"
     * Кеңес: raw.trim() null-де NPE береді, Integer.parseInt("abc") — NumberFormatException.
     * Екеуін БІР catch-пен ұстап, e түріне қарай жауап қайтар.
     */
    public static String classify(String raw) {
        try {
            // raw null болса — trim() әдісі NullPointerException береді.
            // raw "abc" болса — parseInt() әдісі NumberFormatException береді.
            String trimmed = raw.trim();
            Integer.parseInt(trimmed);
            return trimmed;
        } catch (NumberFormatException | NullPointerException e) {
            // e ерекше жағдайының нақты қай типке жататынын тексереміз
            if (e instanceof NullPointerException) {
                return "null";
            } else {
                return "san emes";
            }
        }
    }

    /**
     * Ақша шешеді. amount қалдықтан үлкен болса — InsufficientFundsException лақтыр.
     * amount теріс болса — IllegalArgumentException (бұл бағдарламашының қатесі, unchecked).
     * Әйтпесе жаңа қалдықты қайтар.
     */
    public static double withdraw(double balance, double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Somma teris bolmauy kerek");
        }
        if (amount > balance) {
            // Өзіміздің checked ерекше жағдайымызды шақырамыз (сұралған ақша, бар ақша)
            throw new InsufficientFundsException(amount, balance);
        }
        return balance - amount;
    }

    /**
     * try-with-resources: "A" және "B" деген екі TrackedResource аш,
     * денеде log-қа "body" жаз. Тізімді қайтар.
     * Күтілетін нәтиже: [open:A, open:B, body, close:B, close:A]
     * Назар аудар: жабылу реті — ашылудың КЕРІСІНШЕ.
     */
    public static List<String> resourceOrder() {
        List<String> log = new ArrayList<>();

        // try-with-resources жақша ішінде ресурстарды жариялаймыз
        try (TrackedResource resA = new TrackedResource("A", log);
             TrackedResource resB = new TrackedResource("B", log)) {

            log.add("body"); // try блогының ішкі жұмысы

        } // Осы жерде resB, сосын resA автоматты түрде жабылып, close() шақырылады

        return log;
    }

    /**
     * ТІЗБЕКТЕУ (chaining): raw-ды санға айналдыруға тырыс; NumberFormatException шықса,
     * оны СЕБЕП ретінде сақтап, жаңа IllegalStateException ҚАЙТАР (лақтырма):
     *     new IllegalStateException("Baptau qate: " + raw, e)
     * Сәтті болса — null қайтар.
     *
     * Неге маңызды: себепті жоғалтсаң, стектрейсте нақты не болғаны көрінбей қалады.
     */
    public static IllegalStateException wrapFailure(String raw) {
        try {
            Integer.parseInt(raw);
            return null; // Егер сәтті өтсе, ештеңе қайтармаймыз (null)
        } catch (NumberFormatException e) {
            // e қатесін жаңа IllegalStateException конструкторына беріп, себебін сақтаймыз
            return new IllegalStateException("Baptau qate: " + raw, e);
        }
    }
}
