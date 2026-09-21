package ch03.t07_method_refs;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Тапсырма 07 — Ссылки на методы (method references).
 * Кітап: "Ссылки на методы" (124 б.)
 *
 * Method reference — дайын методты шақырудан басқа ештеңе істемейтін лямбданың
 * қысқа жазылуы. Компилятор үшін екеуі бірдей, адам үшін біріншісі оқылымды.
 *
 * ТӨРТ ТҮРІ:
 *
 *   1. Статикалық метод        Integer::parseInt        (s -> Integer.parseInt(s))
 *   2. НАҚТЫ объектінің методы  text::length            (() -> text.length())
 *   3. КЕЗ КЕЛГЕН объектінің методы  String::toUpperCase (s -> s.toUpperCase())
 *   4. Конструктор              ArrayList::new          (() -> new ArrayList<>())
 *
 * 2 мен 3-тің айырмасы басты шатасу көзі:
 *   text::length  -> объект ҰСТАЛҒАН, параметр қалмады    -> Supplier<Integer>
 *   String::length -> объект ПАРАМЕТР болады              -> Function<String,Integer>
 *
 * Әр методты алдымен лямбдамен жазып көр, сосын method reference-ке қысқарт.
 */
public class MethodRefLab {

    /** 1-түрі: "42" -> 42. Кеңес: Integer::parseInt */
    public static Function<String, Integer> parser() {
        return s -> Integer.parseInt(s);
    }

    /** 3-түрі: кез келген жолды үлкен әріпке. Кеңес: String::toUpperCase */
    public static Function<String, String> upperCaser() {
        return s -> s.toUpperCase();
    }

    /**
     * 2-түрі: НАҚТЫ text объектісінің ұзындығын беретін жеткізуші.
     * Кеңес: text::length — параметр қалмайды, өйткені объект ұсталып қалды.
     */
    public static Supplier<Integer> lengthOf(String text) {
        return () -> text.length();
    }

    /** 4-түрі: шақырған сайын жаңа бос ArrayList. Кеңес: ArrayList::new */
    public static Supplier<List<String>> listFactory() {
        return () -> new ArrayList<>();
    }

    /** 4-түрі, аргументі бар: жолдан StringBuilder жасайды. Кеңес: StringBuilder::new */
    public static Function<String, StringBuilder> builderFactory() {
        return s -> new StringBuilder(s);
    }

    /**
     * 3-түрі, екі аргументпен: (text, prefix) -> text.startsWith(prefix).
     * Бірінші аргумент методтың ИЕСІ болады. Кеңес: String::startsWith
     */
    public static BiFunction<String, String, Boolean> startsWith() {
        return (s1, s2) -> s1.startsWith(s2);
    }

    /** Ұзындығы бойынша салыстырғыш. Кеңес: Comparator.comparingInt(String::length) */
    public static Comparator<String> byLength() {
        return (s1, s2) -> Integer.compare(s1.length(), s2.length());
    }

    /**
     * Барлық жолды санға айналдырады: ["1","2"] -> [1, 2].
     * Кеңес: items.stream().map(Integer::parseInt).toList()
     * Жолдың біреуі сан болмаса — NumberFormatException өздігінен шығады, ұстама.
     */
    public static List<Integer> parseAll(List<String> raw) {
        return raw.stream().map(Integer::parseInt).toList();
    }
}
