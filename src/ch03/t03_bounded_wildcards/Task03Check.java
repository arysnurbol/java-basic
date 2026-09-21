package ch03.t03_bounded_wildcards;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.List;

public class Task03Check {

    public static void run() {
        Check.task("Тапсырма 03 — Ограниченные типы және wildcard-тар (Bounds)");

        Check.eq("max(Integer)", 9, Bounds.max(List.of(3, 9, 1)));
        Check.eq("min(Integer)", 1, Bounds.min(List.of(3, 9, 1)));
        Check.eq("max(String) — Comparable болғандықтан жұмыс істейді",
                "cherry", Bounds.max(List.of("apple", "cherry", "banana")));
        Check.eq("бір элемент", 5, Bounds.max(List.of(5)));
        Check.throwsEx("max(бос тізім) -> IllegalArgumentException",
                IllegalArgumentException.class, () -> Bounds.max(List.<Integer>of()));
        Check.throwsEx("min(null) -> IllegalArgumentException",
                IllegalArgumentException.class, () -> Bounds.min(null));

        // Мына үш жол ? extends Number болмаса КОМПИЛЯЦИЯЛАНБАЙДЫ
        Check.eq("sumOf(List<Integer>)", 6.0, Bounds.sumOf(List.of(1, 2, 3)), 1e-9);
        Check.eq("sumOf(List<Double>)", 1.5, Bounds.sumOf(List.of(0.5, 1.0)), 1e-9);
        Check.eq("sumOf(List<Long>)", 30.0, Bounds.sumOf(List.of(10L, 20L)), 1e-9);
        Check.eq("sumOf(бос тізім)", 0.0, Bounds.sumOf(List.of()), 1e-9);

        List<Integer> ints = new ArrayList<>();
        Bounds.addIntegers(ints, 1, 4);
        Check.eq("addIntegers(List<Integer>, 1, 4)", List.of(1, 2, 3, 4), ints);

        // ? super Integer болғандықтан Number тізіміне де жаза аламыз
        List<Number> mixed = new ArrayList<>(List.of(0.5));
        Bounds.addIntegers(mixed, 7, 8);
        Check.eq("addIntegers(List<Number>, 7, 8)", List.of(0.5, 7, 8), mixed);

        Check.eq("clamp(15, 0, 10)", 10, Bounds.clamp(15, 0, 10));
        Check.eq("clamp(-3, 0, 10)", 0, Bounds.clamp(-3, 0, 10));
        Check.eq("clamp(4, 0, 10)", 4, Bounds.clamp(4, 0, 10));
        Check.eq("clamp(2.5, 0.0, 1.0)", 1.0, Bounds.clamp(2.5, 0.0, 1.0));

        List<Object> sink = new ArrayList<>();
        Bounds.copy(List.of("a", "b"), sink);
        Check.eq("copy(List<String>, List<Object>) — PECS", List.of("a", "b"), sink);
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
