package ch03.t03_bounded_wildcards;

import java.util.List;

/**
 * Тапсырма 03 — Ограниченные параметры типа және wildcard-тар.
 * Кітап: "Ограниченные параметры типа", "Ограниченные сверху/снизу подстановочные знаки" (118–119 б.)
 *
 * ҮШ ТҮРЛІ ШЕКТЕУ:
 *
 *   <T extends Comparable<T>>  — T МІНДЕТТІ түрде Comparable болуы керек.
 *                                Осының арқасында ішінде a.compareTo(b) жазуға болады.
 *   List<? extends Number>     — "Number-дің кез келген ҰРПАҒЫНЫҢ тізімі".
 *                                Одан ОҚУҒА болады, оған ЖАЗУҒА болмайды.
 *   List<? super Integer>      — "Integer-дің кез келген АТА-ТЕГІНІҢ тізімі".
 *                                Оған ЖАЗУҒА болады, оқығанда тек Object шығады.
 *
 * Есте сақтау ережесі — PECS: Producer Extends, Consumer Super.
 * Тізім саған дерек БЕРСЕ (producer) -> extends, дерек ҚАБЫЛДАСА (consumer) -> super.
 *
 * Неге жай List<Number> жетпейді? Өйткені List<Integer> — List<Number>-дің ұрпағы ЕМЕС.
 * Тексерісте sumOf(List<Integer>) шақырылады: қолтаңбаны List<Number> қылып көрші —
 * компиляция қатесін өз көзіңмен көресің.
 */
public class Bounds {

    /**
     * Ең үлкен элемент. Тізім null немесе бос болса — IllegalArgumentException.
     * <T extends Comparable<T>> болғандықтан ішінде compareTo қолдана аласың.
     */
    public static <T extends Comparable<T>> T max(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("can not be empty or null");
        }
        T max = items.get(0);

        for (T item : items) {
            if  (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /** Ең кіші элемент. Шарттары max() сияқты. */
    public static <T extends Comparable<T>> T min(List<T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("can not be empty or null");
        }

        T min = items.get(0);
        for (T item : items) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }

    /**
     * Барлық санның қосындысы. Integer, Double, Long — бәрі өтуі керек.
     * Number-де intValue()/doubleValue() бар.
     */
    public static double sumOf(List<? extends Number> numbers) {

        if  (numbers == null) return 0;
        double sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }

        return sum;
    }

    /**
     * from-дан to-ға дейінгі (екеуін де қоса) бүтін сандарды тізімге ҚОСАДЫ.
     * ? super Integer болғандықтан List<Integer>, List<Number>, List<Object> — бәрі өтеді.
     */
    public static void addIntegers(List<? super Integer> target, int from, int to) {
        for (int i = from; i <= to; i++) {
            target.add(i);
        }
    }

    /**
     * value-ды [min, max] аралығына "қысады": кішісі болса min, үлкені болса max.
     * Екі шектеу қатар: T әрі Number, әрі Comparable болуы керек (& арқылы).
     */
    public static <T extends Number & Comparable<T>> T clamp(T value, T min, T max) {
        // Егер value min-нен кіші болса ( < 0), min-ді қайтарамыз
        if (value.compareTo(min) < 0) {
            return min;
        }
        // Егер value max-тан үлкен болса ( > 0), max-ты қайтарамыз
        if (value.compareTo(max) > 0) {
            return max;
        }
        // Әйтпесе өз мәнін қайтарамыз
        return value;
    }

    /**
     * src-тегі барлық элементті dst-ке көшіреді — классикалық PECS қолтаңбасы.
     * src дерек береді (extends), dst дерек қабылдайды (super).
     */
    public static <T> void copy(List<? extends T> src, List<? super T> dst) {

        if  (src == null || dst == null) return;

        for (T srcItem : src) {
            dst.add(srcItem);
        }
    }
}
