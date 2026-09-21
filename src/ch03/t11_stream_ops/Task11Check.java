package ch03.t11_stream_ops;

import ch03.check.Check;

import java.util.List;
import java.util.Optional;

public class Task11Check {

    public static void run() {
        Check.task("Тапсырма 11 — Stream API: негізгі операциялар (StreamOps)");

        List<String> names = List.of("Chingiz", "Bo", "Dana", "Aisha", "Ed");

        Check.eq("longNamesUpper(3)", List.of("AISHA", "CHINGIZ", "DANA"),
                StreamOps.longNamesUpper(names, 3));
        Check.eq("longNamesUpper(100) -> бос", List.of(), StreamOps.longNamesUpper(names, 100));

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Check.eq("firstNEven(3)", List.of(2, 4, 6), StreamOps.firstNEven(numbers, 3));
        Check.eq("firstNEven(0)", List.of(), StreamOps.firstNEven(numbers, 0));
        Check.eq("firstNEven(100) — барынан көп сұраса", List.of(2, 4, 6, 8),
                StreamOps.firstNEven(numbers, 100));

        Check.eq("distinctSorted()", List.of("a", "b", "c"),
                StreamOps.distinctSorted(List.of("c", "a", "b", "a", "c")));
        Check.eq("distinctSorted(бос)", List.of(), StreamOps.distinctSorted(List.of()));

        Check.eq("countStartingWith(\"B\")", 1L, StreamOps.countStartingWith(names, "B"));
        Check.eq("countStartingWith(\"Z\")", 0L, StreamOps.countStartingWith(names, "Z"));
        Check.eq("countStartingWith(\"\") — бәрі сәйкес", 5L, StreamOps.countStartingWith(names, ""));

        Check.eq("firstLongerThan(3)", Optional.of("Chingiz"), StreamOps.firstLongerThan(names, 3));
        Check.eq("firstLongerThan(100) -> empty", Optional.empty(), StreamOps.firstLongerThan(names, 100));

        Check.isTrue("allPositive([1,2,3])", StreamOps.allPositive(List.of(1, 2, 3)));
        Check.isFalse("allPositive([1,-2])", StreamOps.allPositive(List.of(1, -2)));
        Check.isTrue("allPositive(бос) -> true (allMatch мінезі!)", StreamOps.allPositive(List.of()));

        Check.isTrue("anyNegative([1,-2])", StreamOps.anyNegative(List.of(1, -2)));
        Check.isFalse("anyNegative([1,2])", StreamOps.anyNegative(List.of(1, 2)));
        Check.isFalse("anyNegative(бос) -> false", StreamOps.anyNegative(List.of()));

        Check.eq("sumOfSquares([1,2,3])", 14, StreamOps.sumOfSquares(List.of(1, 2, 3)));
        Check.eq("sumOfSquares(бос)", 0, StreamOps.sumOfSquares(List.of()));
        Check.eq("sumOfSquares([-2])", 4, StreamOps.sumOfSquares(List.of(-2)));

        Check.eq("longest()", Optional.of("Chingiz"), StreamOps.longest(names));
        Check.eq("longest() — тең болса біріншісі", Optional.of("ab"),
                StreamOps.longest(List.of("ab", "cd")));
        Check.eq("longest(бос) -> empty", Optional.empty(), StreamOps.longest(List.of()));

        Check.eq("afterFirst(2)", List.of("Dana", "Aisha", "Ed"), StreamOps.afterFirst(names, 2));
        Check.eq("afterFirst(0)", names, StreamOps.afterFirst(names, 0));
        Check.eq("afterFirst(100)", List.of(), StreamOps.afterFirst(names, 100));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
