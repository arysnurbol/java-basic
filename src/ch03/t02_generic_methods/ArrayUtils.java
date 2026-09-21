package ch03.t02_generic_methods;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Тапсырма 02 — Обобщенные методы.
 * Кітап: "Обобщенные методы", "Определение/использование обобщенного метода" (117–118 б.)
 *
 * СИНТАКСИС: тип параметрі ҚАЙТАРЫЛАТЫН ТИПТІҢ АЛДЫНА жазылады:
 *
 *     public static <T> int countEquals(T[] items, T target)
 *                    ^^^ осы жерде жарияланады
 *
 * Класс генерик болмаса да, жеке метод генерик бола алады.
 * Шақырғанда типті жазудың қажеті жоқ — компилятор аргументтерден шығарады
 * (вывод типов / type inference): ArrayUtils.countEquals(names, "Aisha").
 */
public class ArrayUtils {

    /**
     * Элементтерді бөлгішпен біріктіреді: join(new String[]{"a","b"}, "-") -> "a-b".
     * Массив null немесе бос болса — "" қайтар.
     * Элемент null болса — "null" деп жазылсын (String.valueOf көмектеседі).
     */
    public static <T> String join(T[] items, String separator) {
        if  (items == null || items.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (T item : items) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(String.valueOf(item));
        }
        return sb.toString();
    }

    /** Бірінші элемент. Массив null немесе бос болса — fallback. */
    public static <T> T firstOrDefault(T[] items, T fallback) {
        if   (items == null || items.length == 0) {
            return fallback;
        }
        return items[0];
    }

    /** i мен j орындарын алмастырады (массивтің ӨЗІН өзгертеді). */
    public static <T> void swap(T[] items, int i, int j) {
        T tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    /**
     * target-қа тең элементтердің саны.
     * null-мен де жұмыс істеуі керек, сондықтан == емес, {@link Objects#equals} қолдан.
     */
    public static <T> int countEquals(T[] items, T target) {
        if   (items == null || items.length == 0) {
            return 0;
        }
        int count = 0;
        for (T item : items) {
            if (Objects.equals(item, target)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Массивті ӨЗГЕРТУГЕ БОЛАТЫН тізімге айналдырады (ArrayList).
     * Ескерту: Arrays.asList() тіркелген өлшемді тізім қайтарады — оған add жасалмайды.
     */
    public static <T> List<T> toList(T[] items) {
        if   (items == null || items.length == 0) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    /**
     * Екі массивтің элементтері бірдей ме (реті де маңызды).
     * Екі бөлек тип параметрін қолдану қажет емес — екеуі де T[].
     */
    public static <T> boolean sameElements(T[] a, T[] b) {
        if   (a == null && b == null) {
            return true;
        }
        if   (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
}
