package ch03.t12_collectors;

import ch03.check.Check;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Task12Check {

    public static void run() {
        Check.task("Тапсырма 12 — Коллекторлар (CollectorLab)");

        List<String> langs = List.of("java", "go", "rust", "groovy");

        Check.eq("joinWithCommas()", "java, go, rust, groovy", CollectorLab.joinWithCommas(langs));
        Check.eq("joinWithCommas(бос)", "", CollectorLab.joinWithCommas(List.of()));
        Check.eq("joinBracketed()", "[java, go, rust, groovy]", CollectorLab.joinBracketed(langs));
        Check.eq("joinBracketed(бос)", "[]", CollectorLab.joinBracketed(List.of()));

        Check.eq("groupByLength()",
                Map.of(4, List.of("java", "rust"), 2, List.of("go"), 6, List.of("groovy")),
                CollectorLab.groupByLength(langs));
        Check.eq("groupByLength(бос) — бос топтар ЖАСАЛМАЙДЫ", Map.of(),
                CollectorLab.groupByLength(List.of()));

        Check.eq("countByFirstLetter()", Map.of("j", 1L, "g", 2L, "r", 1L),
                CollectorLab.countByFirstLetter(langs));

        Map<Boolean, List<Integer>> parts = CollectorLab.partitionEven(List.of(1, 2, 3, 4));
        Check.eq("partitionEven() жұптар", List.of(2, 4), parts == null ? null : parts.get(true));
        Check.eq("partitionEven() тақтар", List.of(1, 3), parts == null ? null : parts.get(false));

        Map<Boolean, List<Integer>> allOdd = CollectorLab.partitionEven(List.of(1, 3));
        Check.eq("partitioningBy бос жақты да қалдырады — кілттер саны", 2,
                allOdd == null ? -1 : allOdd.size());
        Check.eq("сәйкес келмеген жақ — бос тізім", List.of(), allOdd == null ? null : allOdd.get(true));

        Check.eq("averageLength()", 4.0, CollectorLab.averageLength(langs), 1e-9);
        Check.eq("averageLength(бос) -> 0.0", 0.0, CollectorLab.averageLength(List.of()), 1e-9);

        Check.eq("toLengthMap()", Map.of("java", 4, "go", 2, "rust", 4, "groovy", 6),
                CollectorLab.toLengthMap(langs));
        Check.noThrow("toLengthMap() қайталанған кілтте ҚҰЛАМАЙДЫ",
                () -> CollectorLab.toLengthMap(List.of("go", "go")));
        Check.eq("қайталанғанда бірінші мән қалады", Map.of("go", 2),
                CollectorLab.toLengthMap(List.of("go", "go")));

        Check.eq("firstLetters()", Set.of("j", "g", "r"), CollectorLab.firstLetters(langs));
        Check.eq("firstLetters(бос)", Set.of(), CollectorLab.firstLetters(List.of()));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
