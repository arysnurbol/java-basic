package ch03.t13_streams_advanced;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Task13Check {

    public static void run() {
        Check.task("Тапсырма 13 — Примитивті, жалқау және параллель ағындар (StreamAdvanced)");

        Check.eq("sumRange(1, 5)", 15, StreamAdvanced.sumRange(1, 5));
        Check.eq("sumRange(3, 3)", 3, StreamAdvanced.sumRange(3, 3));
        Check.eq("sumRange(5, 1) — бос аралық", 0, StreamAdvanced.sumRange(5, 1));

        Check.eq("average(2, 4, 6)", 4.0, StreamAdvanced.average(2, 4, 6), 1e-9);
        Check.eq("average() — сансыз", 0.0, StreamAdvanced.average(), 1e-9);

        Check.eq("flatten()", List.of("a", "b", "c"),
                StreamAdvanced.flatten(List.of(List.of("a", "b"), List.of(), List.of("c"))));
        Check.eq("flatten(бос)", List.of(), StreamAdvanced.flatten(List.of()));

        Check.eq("allWords()", List.of("java", "go", "rust"),
                StreamAdvanced.allWords(List.of("java go", "rust")));

        Check.eq("firstPowersOfTwo(5)", List.of(1, 2, 4, 8, 16), StreamAdvanced.firstPowersOfTwo(5));
        Check.eq("firstPowersOfTwo(1)", List.of(1), StreamAdvanced.firstPowersOfTwo(1));
        Check.eq("firstPowersOfTwo(0)", List.of(), StreamAdvanced.firstPowersOfTwo(0));

        int[] array = StreamAdvanced.toIntArray(List.of(3, 1, 2));
        Check.eq("toIntArray()", "[3, 1, 2]", array == null ? null : Arrays.toString(array));

        Check.eq("parallelSum()", 5050L,
                StreamAdvanced.parallelSum(new ArrayList<>(rangeList(1, 100))));
        Check.eq("parallelSum(бос)", 0L, StreamAdvanced.parallelSum(List.of()));

        // Жалқаулық: терминалдық операциясыз ЕШТЕҢЕ орындалмайды
        List<String> log1 = new ArrayList<>();
        StreamAdvanced.lazyNoTerminal(List.of("a", "bb", "ccc"), log1);
        Check.eq("терминалдық операциясыз peek() ІСКЕ ҚОСЫЛМАЙДЫ — log бос", List.of(), log1);

        // Short-circuit: findFirst тапқан бойда тоқтайды
        List<String> log2 = new ArrayList<>();
        Optional<String> found = StreamAdvanced.firstLongEnough(List.of("a", "bb", "ccc", "dddd"), log2);
        Check.eq("firstLongEnough() нәтижесі", Optional.of("ccc"), found);
        Check.eq("тек ҚАРАЛҒАН элементтер log-та — \"dddd\" мүлде тексерілмеді",
                List.of("a", "bb", "ccc"), log2);

        List<String> log3 = new ArrayList<>();
        Check.eq("ештеңе табылмаса -> empty", Optional.empty(),
                StreamAdvanced.firstLongEnough(List.of("a", "bb"), log3));
        Check.eq("табылмаса бүкіл тізім қаралады", List.of("a", "bb"), log3);
    }

    private static List<Integer> rangeList(int from, int to) {
        List<Integer> result = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            result.add(i);
        }
        return result;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
