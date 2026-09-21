package ch03.t07_method_refs;

import java.util.Comparator;
import java.util.List;
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
        return null; // TODO
    }

    /** 3-түрі: кез келген жолды үлкен әріпке. Кеңес: String::toUpperCase */
    public static Function<String, String> upperCaser() {
        return null; // TODO
    }

    /**
     * 2-түрі: НАҚТЫ text объектісінің ұзындығын беретін жеткізуші.
     * Кеңес: text::length — параметр қалмайды, өйткені объект ұсталып қалды.
     */
    public static Supplier<Integer> lengthOf(String text) {
        return null; // TODO
    }

    /** 4-түрі: шақырған сайын жаңа бос ArrayList. Кеңес: ArrayList::new */
    public static Supplier<List<String>> listFactory() {
        return null; // TODO
    }

    /** 4-түрі, аргументі бар: жолдан StringBuilder жасайды. Кеңес: StringBuilder::new */
    public static Function<String, StringBuilder> builderFactory() {
        return null; // TODO
    }

    /**
     * 3-түрі, екі аргументпен: (text, prefix) -> text.startsWith(prefix).
     * Бірінші аргумент методтың ИЕСІ болады. Кеңес: String::startsWith
     */
    public static BiFunction<String, String, Boolean> startsWith() {
        return null; // TODO
    }

    /** Ұзындығы бойынша салыстырғыш. Кеңес: Comparator.comparingInt(String::length) */
    public static Comparator<String> byLength() {
        return null; // TODO
    }

    /**
     * Барлық жолды санға айналдырады: ["1","2"] -> [1, 2].
     * Кеңес: items.stream().map(Integer::parseInt).toList()
     * Жолдың біреуі сан болмаса — NumberFormatException өздігінен шығады, ұстама.
     */
    public static List<Integer> parseAll(List<String> raw) {
        return null; // TODO
    }
}
