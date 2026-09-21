package ch03.t11_stream_ops;

import java.util.List;
import java.util.Optional;

/**
 * Тапсырма 11 — Stream API: құру, аралық және терминалдық операциялар.
 * Кітап: "Понимание Stream API", "Создание потоков", "Операции с потоками",
 *        "Терминальные операции" (131–134 б.)
 *
 * Поток (stream) — коллекция ЕМЕС. Ол дерек сақтамайды, тек өткізеді.
 * Кез келген құбыр үш бөліктен тұрады:
 *
 *     дерек көзі  ->  аралық операциялар  ->  терминалдық операция
 *     list.stream()   .filter().map()         .toList()
 *
 * АРАЛЫҚ операциялар (жаңа stream қайтарады, ЕШТЕҢЕ ІСТЕМЕЙДІ):
 *     filter, map, sorted, distinct, limit, skip, peek
 *
 * ТЕРМИНАЛДЫҚ операциялар (бүкіл құбырды ІСКЕ ҚОСАДЫ, stream аяқталады):
 *     toList, collect, forEach, count, reduce, min, max,
 *     anyMatch, allMatch, noneMatch, findFirst, findAny
 *
 * ЕКІ ЕРЕЖЕ:
 *   1. Терминалдық операциясыз ЕШТЕҢЕ орындалмайды (13-тапсырмада көресің).
 *   2. Бір stream-ді екі рет қолдануға болмайды — IllegalStateException.
 */
public class StreamOps {

    /** Ұзындығы minLen-нен ҰЗЫН есімдерді үлкен әріппен, әліпби ретімен. */
    public static List<String> longNamesUpper(List<String> names, int minLen) {
        return null; // TODO
    }

    /** Алғашқы n жұп сан (кірістегі ретті сақтап). Кеңес: filter + limit. */
    public static List<Integer> firstNEven(List<Integer> numbers, int n) {
        return null; // TODO
    }

    /** Қайталанбайтын, сұрыпталған элементтер. Кеңес: distinct + sorted. */
    public static List<String> distinctSorted(List<String> items) {
        return null; // TODO
    }

    /** prefix-тен басталатын элементтердің саны. Кеңес: filter + count. */
    public static long countStartingWith(List<String> items, String prefix) {
        return 0; // TODO
    }

    /**
     * Ұзындығы len-нен ұзын БІРІНШІ элемент.
     * Табылмаса Optional.empty() — null ҚАЙТАРМА.
     */
    public static Optional<String> firstLongerThan(List<String> items, int len) {
        return null; // TODO
    }

    /** Барлығы оң сан ба. Бос тізім -> true (allMatch-тің мінезі осындай). */
    public static boolean allPositive(List<Integer> numbers) {
        return false; // TODO
    }

    /** Кемінде біреуі теріс пе. Бос тізім -> false. */
    public static boolean anyNegative(List<Integer> numbers) {
        return false; // TODO
    }

    /** Квадраттарының қосындысы. Кеңес: map + reduce(0, Integer::sum). */
    public static int sumOfSquares(List<Integer> numbers) {
        return 0; // TODO
    }

    /** Ең ұзын элемент; тең болса — БІРІНШІСІ. Бос тізім -> Optional.empty(). */
    public static Optional<String> longest(List<String> items) {
        return null; // TODO
    }

    /**
     * skip(k) арқылы алғашқы k элементті аттап, қалғанын қайтарады.
     * k тізімнен үлкен болса — бос тізім.
     */
    public static List<String> afterFirst(List<String> items, int k) {
        return null; // TODO
    }
}
