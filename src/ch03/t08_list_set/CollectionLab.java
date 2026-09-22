package ch03.t08_list_set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Тапсырма 08 — Java Collections Framework: List және Set.
 * Кітап: "Обзор/Основные интерфейсы фреймворка коллекций", "Распространенные реализации" (125–129 б.)
 *
 * ҚАЙСЫСЫН ҚАШАН АЛУ КЕРЕК:
 *
 *   ArrayList      индекс бойынша жылдам оқу, соңына қосу жылдам. ӘДЕПКІ таңдау.
 *   LinkedList     ортасынан жиі қосып-өшіресең. Іс жүзінде сирек қажет.
 *   HashSet        қайталанбайтын элементтер, РЕТІ ЖОҚ, O(1) contains.
 *   LinkedHashSet  қайталанбайды + ҚОСЫЛУ реті сақталады.
 *   TreeSet        қайталанбайды + ӘРҚАШАН сұрыпталған (Comparable керек).
 *
 * ТҰЗАҚ: List.of(...) / Set.of(...) — ӨЗГЕРМЕЙТІН (immutable) коллекциялар.
 * Оларға add() жасасаң UnsupportedOperationException аласың.
 * Өзгертетін көшірме керек болса: new ArrayList<>(List.of(...)).
 */
public class CollectionLab {

    /** Массивтен ӨЗГЕРТУГЕ БОЛАТЫН ArrayList жасайды. */
    public static List<String> mutableList(String... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    /** Қайталанбайтын элементтер, ҚОСЫЛУ реті сақталады. Кеңес: LinkedHashSet. */
    public static Set<String> uniqueKeepingOrder(List<String> items) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String item : items) {
            set.add(item);
        }
        return set;
    }

    /** Қайталанбайтын элементтер, ӘРҚАШАН сұрыпталған. Кеңес: TreeSet. */
    public static Set<String> sortedUnique(List<String> items) {
        TreeSet<String> set = new TreeSet<>(Comparator.naturalOrder());
        for (String item : items) {
            set.add(item);
        }
        return set;
    }

    /**
     * Ұзындығы min-нен КІШІ жолдарды тізімнің ӨЗІНЕН өшіреді, өшірілген санын қайтарады.
     *
     * НАЗАР АУДАР: for-each ішінде list.remove() шақырсаң ConcurrentModificationException
     * аласың. Дұрыс екі жол бар: Iterator.remove() немесе List.removeIf(предикат).
     * Екеуін де біліп ал — бұл сұхбатта жиі сұралады.
     */
    public static int removeShorterThan(List<String> items, int min) {
        int originalSize = items.size();
        items.removeIf(item -> item.length() < min);
        return originalSize -  items.size();
    }

    /** ӨЗГЕРТУГЕ БОЛМАЙТЫН көшірме. Кеңес: List.copyOf. */
    public static List<String> readOnlyCopy(List<String> items) {
        return List.copyOf(items);
    }

    /** Екі тізімнің ОРТАҚ элементтері, сұрыпталған күйде. Кеңес: Set.retainAll. */
    public static Set<String> intersection(List<String> a, List<String> b) {
        Set<String> result = new TreeSet<>(a);
        // Тек "b" тізімінде де бар элементтерді ғана алып қалады
        result.retainAll(b);
        return result;
    }

    /** a-да бар, бірақ b-да жоқ элементтер, сұрыпталған күйде. Кеңес: Set.removeAll. */
    public static Set<String> difference(List<String> a, List<String> b) {
        Set<String> result = new TreeSet<>(a);
        // Тек "b" тізімінде де бар элементтерді ғана алып қалады
        result.removeAll(b);
        return result;
    }
}
