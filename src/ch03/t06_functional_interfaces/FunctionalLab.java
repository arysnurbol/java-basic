package ch03.t06_functional_interfaces;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import java.util.ArrayList;

/**
 * Тапсырма 06 — java.util.function-дағы дайын функционалдық интерфейстер.
 * Кітап: "Распространенные функциональные интерфейсы в java.util.function" (123 б.)
 *
 * НЕГІЗГІ ТӨРТЕУІ (жаттап ал — бәрі осыдан тарайды):
 *
 *   Predicate<T>       T -> boolean     test(t)     .and() .or() .negate()
 *   Function<T,R>      T -> R           apply(t)    .andThen() .compose()
 *   Supplier<T>        ()  -> T         get()
 *   Consumer<T>        T -> ()          accept(t)
 *   BiFunction<T,U,R>  T,U -> R         apply(t,u)
 *
 * compose пен andThen-ді шатастырма:
 *   f.andThen(g).apply(x) == g.apply(f.apply(x))   // алдымен f
 *   f.compose(g).apply(x) == f.apply(g.apply(x))   // алдымен g
 */
public class FunctionalLab {

    /** Transformer-ді қолданады. Өзіңнің интерфейсің де лямбда қабылдайды. */
    public static String transform(String input, Transformer transformer) {
        return transformer.apply(input);
    }

    /** null емес әрі бос емес (тек бос орындардан тұрмайтын) жолдарды өткізетін предикат. */
    public static Predicate<String> notBlank() {
        return  input -> input != null && !input.trim().isEmpty();
    }

    /** lo мен hi аралығындағы (екеуін де қоса) сандарды өткізетін предикат. */
    public static Predicate<Integer> inRange(int lo, int hi) {
        return input -> input >= lo && input <= hi;
    }

    /**
     * Жолдың ұзындығын қайтарады, СОСЫН оны екі есе арттырады.
     * Кеңес: Function.andThen тізбегі — екі бөлек лямбданы біріктір.
     */
    public static Function<String, Integer> doubledLength() {
        Function<String, Integer> lenFunction = String::length;
        return lenFunction.andThen(len -> len * 2);
    }

    /** Шақырған сайын ЖАҢА бос ArrayList беретін жеткізуші. */
    public static Supplier<List<String>> newList() {
        return () -> new ArrayList<>();
    }

    /** Берілген мәтінді StringBuilder-ге жалғайтын тұтынушы. */
    public static Consumer<StringBuilder> appender(String text) {
        return builder -> builder.append(text);
    }

    /** Екі санды қосып, жолға айналдыратын функция: (2, 3) -> "5". */
    public static BiFunction<Integer, Integer, String> sumAsText() {
        return (a, b) -> String.valueOf(a + b);
    }

    /**
     * Предикатқа сәйкес келетін элементтерді ЖАҢА тізімге жинайды.
     * Stream-сіз, қарапайым циклмен жаз — предикаттың қалай қолданылатынын көру үшін.
     */
    public static <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
