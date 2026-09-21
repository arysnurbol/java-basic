package ch03.t08_list_set;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Task08Check {

    public static void run() {
        Check.task("Тапсырма 08 — Коллекциялар: List және Set (CollectionLab)");

        List<String> list = CollectionLab.mutableList("a", "b");
        Check.eq("mutableList() мазмұны", List.of("a", "b"), list);
        Check.noThrow("mutableList() нәтижесіне add жасауға болады", () -> list.add("c"));

        List<String> dupes = List.of("bolat", "aisha", "bolat", "chingiz", "aisha");

        Set<String> ordered = CollectionLab.uniqueKeepingOrder(dupes);
        Check.eq("uniqueKeepingOrder() — қайталанбайды, реті сақталады",
                new LinkedHashSet<>(List.of("bolat", "aisha", "chingiz")), ordered);
        Check.eq("реті тізім түрінде де дәл келеді",
                List.of("bolat", "aisha", "chingiz"), asList(ordered));

        Set<String> sorted = CollectionLab.sortedUnique(dupes);
        Check.eq("sortedUnique() — әліпби реті", List.of("aisha", "bolat", "chingiz"), asList(sorted));
        Check.isTrue("sortedUnique() TreeSet қайтарады", sorted instanceof TreeSet);

        List<String> toClean = new ArrayList<>(List.of("java", "go", "kotlin", "c", "rust"));
        Check.eq("removeShorterThan(toClean, 4) -> өшірілген саны", 2,
                CollectionLab.removeShorterThan(toClean, 4));
        Check.eq("тізімнің ӨЗІ өзгерді", List.of("java", "kotlin", "rust"), toClean);
        Check.eq("ештеңе сәйкес келмесе -> 0", 0, CollectionLab.removeShorterThan(toClean, 1));

        List<String> ro = CollectionLab.readOnlyCopy(List.of("x", "y"));
        Check.eq("readOnlyCopy() мазмұны", List.of("x", "y"), ro);
        Check.throwsEx("readOnlyCopy() нәтижесіне add -> UnsupportedOperationException",
                UnsupportedOperationException.class, () -> ro.add("z"));

        List<String> a = List.of("java", "go", "rust", "go");
        List<String> b = List.of("go", "rust", "python");
        Check.eq("intersection()", List.of("go", "rust"), asList(CollectionLab.intersection(a, b)));
        Check.eq("difference()", List.of("java"), asList(CollectionLab.difference(a, b)));
        Check.eq("difference() қиылыспаса — бәрі қалады", List.of("java"),
                asList(CollectionLab.difference(List.of("java"), List.of("go"))));
        Check.eq("intersection() қиылыспаса — бос", List.of(),
                asList(CollectionLab.intersection(List.of("java"), List.of("go"))));
    }

    /** null-ге төзімді: іске аспаған метод бүкіл тапсырманы құлатпасын. */
    private static List<String> asList(Collection<String> source) {
        return source == null ? null : new ArrayList<>(source);
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
