package ch03.t09_map;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Task09Check {

    public static void run() {
        Check.task("Тапсырма 09 — Map (MapLab)");

        List<String> words = List.of("java", "go", "java", "rust", "go", "java");

        Map<String, Integer> counts = MapLab.wordCount(words);
        Check.eq("wordCount() өлшемі", 3, counts == null ? -1 : counts.size());
        Check.eq("wordCount(java)", 3, counts == null ? null : counts.get("java"));
        Check.eq("wordCount(go)", 2, counts == null ? null : counts.get("go"));
        Check.eq("wordCount(rust)", 1, counts == null ? null : counts.get("rust"));
        Check.eq("бос тізім -> бос map", Map.of(), MapLab.wordCount(List.of()));

        Map<String, List<String>> groups = MapLab.groupByFirstLetter(
                List.of("java", "go", "jakarta", "rust", "gradle"));
        Check.eq("groupByFirstLetter() кілттері СҰРЫПТАЛҒАН", List.of("g", "j", "r"),
                groups == null ? null : new ArrayList<>(groups.keySet()));
        Check.eq("топ \"j\" — қосылу реті", List.of("java", "jakarta"),
                groups == null ? null : groups.get("j"));
        Check.eq("топ \"g\"", List.of("go", "gradle"), groups == null ? null : groups.get("g"));

        Map<String, Integer> unordered = new LinkedHashMap<>();
        unordered.put("c", 3);
        unordered.put("a", 1);
        unordered.put("b", 2);
        Check.eq("describeSorted() кілттерді сұрыптайды", "a=1, b=2, c=3", MapLab.describeSorted(unordered));
        Check.eq("describeSorted(бос map)", "", MapLab.describeSorted(Map.of()));
        Check.eq("describeSorted(бір элемент)", "x=9", MapLab.describeSorted(Map.of("x", 9)));

        Check.eq("totalOf()", 6, MapLab.totalOf(unordered));
        Check.eq("totalOf(бос map)", 0, MapLab.totalOf(Map.of()));

        Check.eq("keyWithMaxValue()", "c", MapLab.keyWithMaxValue(unordered));
        Check.eq("keyWithMaxValue() — тең болса әліпби кішісі", "a",
                MapLab.keyWithMaxValue(new LinkedHashMap<>(Map.of("b", 5, "a", 5))));
        Check.eq("keyWithMaxValue(бос map) -> null", null, MapLab.keyWithMaxValue(Map.of()));

        Map<String, Integer> toInvert = new LinkedHashMap<>();
        toInvert.put("b", 1);
        toInvert.put("a", 1);
        toInvert.put("c", 2);
        Map<Integer, String> inverted = MapLab.invert(toInvert);
        Check.eq("invert() — қайталанған мәнде әліпби кішісі жеңеді", "a",
                inverted == null ? null : inverted.get(1));
        Check.eq("invert(2)", "c", inverted == null ? null : inverted.get(2));
        Check.eq("invert() кілттері сұрыпталған", List.of(1, 2),
                inverted == null ? null : new ArrayList<>(inverted.keySet()));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
