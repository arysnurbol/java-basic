package ch03.t09_map;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Тапсырма 09 — Map интерфейсі.
 * Кітап: "Основные интерфейсы фреймворка коллекций" (126–129 б.)
 *
 *   HashMap        әдепкі таңдау. Реті ЖОҚ (әрі оны болжауға болмайды).
 *   LinkedHashMap  қосылу реті сақталады.
 *   TreeMap        кілттер бойынша ӘРҚАШАН сұрыпталған.
 *
 * ЖИІ КЕРЕК БОЛАТЫН ТӨРТ МЕТОД (null-ді тексеретін if-терден құтқарады):
 *
 *   map.getOrDefault(key, 0)                         табылмаса әдепкі мән
 *   map.putIfAbsent(key, value)                      бар болса тимейді
 *   map.computeIfAbsent(key, k -> new ArrayList<>()) жоқ болса жасап қояды
 *   map.merge(key, 1, Integer::sum)                  жоқ болса 1, бар болса қосады
 *
 * merge() — сөз санағыш жазудың ең қысқа жолы. Соны қолданып көр.
 */
public class MapLab {

    /**
     * Әр сөздің қанша рет кездескенін санайды.
     * Реті маңызды емес — HashMap жеткілікті.
     */
    public static Map<String, Integer> wordCount(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            map.merge(word, 1, Integer::sum);
        }
        return map;
    }

    /**
     * Сөздерді бірінші әрпі бойынша топтайды: "java" -> кілт "j".
     * Кілттер СҰРЫПТАЛҒАН болуы керек (TreeMap), әр топтың ішінде — қосылу реті.
     * Кеңес: computeIfAbsent.
     */
    public static Map<String, List<String>> groupByFirstLetter(List<String> words) {
        TreeMap<String, List<String>> map = new TreeMap<>();
        for (String word : words) {
            if (word == null || word.isEmpty()) {
                continue;
            }

            String firstLetter = String.valueOf(word.charAt(0));
            map.computeIfAbsent(firstLetter, k -> new ArrayList<>()).add(word);
        }
        return map;
    }

    /**
     * Map-ты "a=1, b=2" түрінде жазады, кілттер СҰРЫПТАЛҒАН күйде.
     * Бос map -> "".
     * Кеңес: new TreeMap<>(map) + entrySet() бойынша цикл.
     */
    public static String describeSorted(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        // Кілттер сұрыпталуы үшін TreeMap-ке көшіреміз
        Map<String, Integer> sortedMap = new TreeMap<>(map);

        List<String> pairs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            pairs.add(entry.getKey() + "=" + entry.getValue());
        }

        // Элементтерді үтір және бос орын арқылы біріктіру
        return String.join(", ", pairs);
    }

    /** Барлық мәннің қосындысы. */
    public static int totalOf(Map<String, Integer> map) {
        if (map == null) return 0;
        return map.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Ең үлкен мәні бар кілт. Тең болса — әліпби бойынша кішісі.
     * Map бос болса — null.
     */
    public static String keyWithMaxValue(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        String maxKey = null;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String currentKey = entry.getKey();
            int currentValue = entry.getValue();

            if (currentValue > maxValue) {
                maxValue = currentValue;
                maxKey = currentKey;
            } else if (currentValue == maxValue) {
                // Мәндері тең болса, әліпби бойынша кішісін таңдаймыз (жаңа кілт кіші болса, compareTo теріс мән береді)
                if (maxKey == null || currentKey.compareTo(maxKey) < 0) {
                    maxKey = currentKey;
                }
            }
        }
        return maxKey;
    }

    /**
     * Кілттер мен мәндердің орнын алмастырады.
     * Бір мән бірнеше кілтте кездессе — ӘЛІПБИ БОЙЫНША БІРІНШІ кілт жеңеді.
     * Нәтиже кілттері бойынша сұрыпталған болуы керек.
     */
    public static Map<Integer, String> invert(Map<String, Integer> map) {
        // Нәтиже кілттері (сандар) бойынша сұрыпталуы керек, сондықтан TreeMap
        Map<Integer, String> result = new TreeMap<>();

        if (map == null || map.isEmpty()) {
            return result;
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String strKey = entry.getKey();
            Integer intValue = entry.getValue();

            // Егер бұл мән (жаңа кілт) бұған дейін кездеспесе, бірден қосамыз
            if (!result.containsKey(intValue)) {
                result.put(intValue, strKey);
            } else {
                // Егер бұрын қосылған болса, әліпби бойынша кішісін (біріншісін) алып қаламыз
                String existingStr = result.get(intValue);
                if (strKey.compareTo(existingStr) < 0) {
                    result.put(intValue, strKey);
                }
            }
        }
        return result;
    }
}
