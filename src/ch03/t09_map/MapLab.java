package ch03.t09_map;

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
        return null; // TODO
    }

    /**
     * Сөздерді бірінші әрпі бойынша топтайды: "java" -> кілт "j".
     * Кілттер СҰРЫПТАЛҒАН болуы керек (TreeMap), әр топтың ішінде — қосылу реті.
     * Кеңес: computeIfAbsent.
     */
    public static Map<String, List<String>> groupByFirstLetter(List<String> words) {
        return null; // TODO
    }

    /**
     * Map-ты "a=1, b=2" түрінде жазады, кілттер СҰРЫПТАЛҒАН күйде.
     * Бос map -> "".
     * Кеңес: new TreeMap<>(map) + entrySet() бойынша цикл.
     */
    public static String describeSorted(Map<String, Integer> map) {
        return null; // TODO
    }

    /** Барлық мәннің қосындысы. */
    public static int totalOf(Map<String, Integer> map) {
        return 0; // TODO
    }

    /**
     * Ең үлкен мәні бар кілт. Тең болса — әліпби бойынша кішісі.
     * Map бос болса — null.
     */
    public static String keyWithMaxValue(Map<String, Integer> map) {
        return null; // TODO
    }

    /**
     * Кілттер мен мәндердің орнын алмастырады.
     * Бір мән бірнеше кілтте кездессе — ӘЛІПБИ БОЙЫНША БІРІНШІ кілт жеңеді.
     * Нәтиже кілттері бойынша сұрыпталған болуы керек.
     */
    public static Map<Integer, String> invert(Map<String, Integer> map) {
        return null; // TODO
    }
}
