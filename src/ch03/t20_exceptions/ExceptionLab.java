package ch03.t20_exceptions;

import java.util.List;

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
        return 0; // TODO
    }

    /**
     * try / catch / finally блоктарының орындалу РЕТІН жазады.
     * shouldThrow == false -> ["try", "finally"]
     * shouldThrow == true  -> ["try", "catch", "finally"]
     * Яғни try ішінде "try" қос, сосын шарт бойынша exception лақтыр,
     * catch-та "catch", finally-де "finally" қос та, тізімді қайтар.
     */
    public static List<String> executionOrder(boolean shouldThrow) {
        return null; // TODO
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
        return null; // TODO
    }

    /**
     * Ақша шешеді. amount қалдықтан үлкен болса — InsufficientFundsException лақтыр.
     * amount теріс болса — IllegalArgumentException (бұл бағдарламашының қатесі, unchecked).
     * Әйтпесе жаңа қалдықты қайтар.
     */
    public static double withdraw(double balance, double amount) throws InsufficientFundsException {
        return 0; // TODO
    }

    /**
     * try-with-resources: "A" және "B" деген екі TrackedResource аш,
     * денеде log-қа "body" жаз. Тізімді қайтар.
     * Күтілетін нәтиже: [open:A, open:B, body, close:B, close:A]
     * Назар аудар: жабылу реті — ашылудың КЕРІСІНШЕ.
     */
    public static List<String> resourceOrder() {
        return null; // TODO
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
        return null; // TODO
    }
}
