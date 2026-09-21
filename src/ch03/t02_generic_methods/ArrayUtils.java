package ch03.t02_generic_methods;

import java.util.List;
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
        return null; // TODO
    }

    /** Бірінші элемент. Массив null немесе бос болса — fallback. */
    public static <T> T firstOrDefault(T[] items, T fallback) {
        return null; // TODO
    }

    /** i мен j орындарын алмастырады (массивтің ӨЗІН өзгертеді). */
    public static <T> void swap(T[] items, int i, int j) {
        // TODO
    }

    /**
     * target-қа тең элементтердің саны.
     * null-мен де жұмыс істеуі керек, сондықтан == емес, {@link Objects#equals} қолдан.
     */
    public static <T> int countEquals(T[] items, T target) {
        return 0; // TODO
    }

    /**
     * Массивті ӨЗГЕРТУГЕ БОЛАТЫН тізімге айналдырады (ArrayList).
     * Ескерту: Arrays.asList() тіркелген өлшемді тізім қайтарады — оған add жасалмайды.
     */
    public static <T> List<T> toList(T[] items) {
        return null; // TODO
    }

    /**
     * Екі массивтің элементтері бірдей ме (реті де маңызды).
     * Екі бөлек тип параметрін қолдану қажет емес — екеуі де T[].
     */
    public static <T> boolean sameElements(T[] a, T[] b) {
        return false; // TODO
    }
}
