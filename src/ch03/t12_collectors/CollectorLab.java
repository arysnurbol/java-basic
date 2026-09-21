package ch03.t12_collectors;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Тапсырма 12 — Коллекторлар (Collectors).
 * Кітап: "Работа с коллекторами", "Основные возможности коллекторов" (135 б.)
 *
 * collect(...) — ең икемді терминалдық операция. Дайын коллекторлар:
 *
 *   Collectors.toList() / toSet()              коллекцияға жинау
 *   Collectors.joining(", ", "[", "]")         жолға біріктіру (бөлгіш, басы, соңы)
 *   Collectors.groupingBy(классификатор)       Map<K, List<T>>
 *   Collectors.groupingBy(кл., counting())     Map<K, Long>  — екі деңгейлі
 *   Collectors.partitioningBy(предикат)        Map<Boolean, List<T>> — ӘРҚАШАН 2 кілт
 *   Collectors.toMap(кілт, мән)                Map<K, V>
 *   Collectors.averagingInt / summingInt       сандық қорытындылар
 *
 * ТҰЗАҚ 1: toMap() қайталанған кілтке тап болса IllegalStateException лақтырады.
 *          Үшінші аргумент — біріктіру функциясы — соны шешеді.
 * ТҰЗАҚ 2: partitioningBy ешбір элемент сәйкес келмесе де true/false кілттерін қалдырады,
 *          ал groupingBy бос топтарды МҮЛДЕ жасамайды.
 */
public class CollectorLab {

    /** "java, go, rust" түрінде біріктіреді. Кеңес: Collectors.joining(", "). */
    public static String joinWithCommas(List<String> items) {
        return null; // TODO
    }

    /** "[java, go]" — жақшалармен. Кеңес: joining-тің үш аргументті нұсқасы. */
    public static String joinBracketed(List<String> items) {
        return null; // TODO
    }

    /** Ұзындығы бойынша топтау: 2 -> ["go"], 4 -> ["java", "rust"]. */
    public static Map<Integer, List<String>> groupByLength(List<String> items) {
        return null; // TODO
    }

    /** Бірінші әрпі -> сол әріптен басталатындардың САНЫ. Кеңес: groupingBy + counting. */
    public static Map<String, Long> countByFirstLetter(List<String> items) {
        return null; // TODO
    }

    /** Жұп/тақ бойынша бөлу. Кілттері ӘРҚАШАН true және false. */
    public static Map<Boolean, List<Integer>> partitionEven(List<Integer> numbers) {
        return null; // TODO
    }

    /** Орташа ұзындық. Бос тізім -> 0.0. Кеңес: Collectors.averagingInt. */
    public static double averageLength(List<String> items) {
        return 0; // TODO
    }

    /**
     * Сөз -> ұзындығы. Қайталанған сөз кездессе — құламай, бірінші мәнді қалдыр
     * (toMap-тің үшінші аргументі: (a, b) -> a).
     */
    public static Map<String, Integer> toLengthMap(List<String> items) {
        return null; // TODO
    }

    /** Бірінші әріптердің жиыны. Кеңес: map + Collectors.toSet(). */
    public static Set<String> firstLetters(List<String> items) {
        return null; // TODO
    }
}
