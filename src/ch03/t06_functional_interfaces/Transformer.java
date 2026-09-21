package ch03.t06_functional_interfaces;

/**
 * Тапсырма 06 — ӨЗІҢНІҢ функционалдық интерфейсің.
 * Кітап: "Функциональные интерфейсы", "Определение функционального интерфейса" (123 б.)
 *
 * @FunctionalInterface — міндетті емес, бірақ ПАЙДАЛЫ аннотация:
 * егер біреу бұл интерфейске екінші дерексіз метод қосса, компилятор қате береді.
 *
 * Сынап көр: төменге "String other();" деп қосып, компиляцияны жүгірт.
 */
@FunctionalInterface
public interface Transformer {

    String apply(String input);

    /**
     * default метод дерексіз ЕМЕС, сондықтан интерфейс функционалдық болып қала береді.
     * Алдымен осы, сосын next қолданылады: a.andThen(b).apply(x) == b.apply(a.apply(x)).
     */
    default Transformer andThen(Transformer next) {
        return (input) -> next.apply(this.apply(input));
    }

    /** Кірісті өзгертпей қайтаратын дайын трансформер. */
    static Transformer identity() {
        return (input) -> input;
    }
}
