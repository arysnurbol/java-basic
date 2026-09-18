package ch02.t05_overloading;

/**
 * Тапсырма 05 — Методтарды шамадан тыс жүктеу (overloading).
 * Кітап: "Перегрузка методов" (84-б.)
 *
 * Барлық метод бір атпен — max — бірақ параметрлері әртүрлі.
 */
public class MathUtils {

    /** Екі бүтін санның үлкенін қайтарады. */
    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    /** Екі бөлшек санның үлкенін қайтарады. */
    public static double max(double a, double b) {
        return a > b ? a : b;
    }

    /** Үш бүтін санның үлкенін қайтарады (жоғарыдағы max(int,int)-ті қайта қолдан). */
    public static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    /**
     * Массивтегі ең үлкен санды қайтарады.
     * Массив null немесе бос болса — IllegalArgumentException лақтыр.
     */
    public static int max(int[] numbers) {
        if  (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }

        int max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }

        return max;
    }

    /**
     * Ұзындығы бойынша ұзын жолды қайтарады.
     * Ұзындығы тең болса — бірінші аргументті (a) қайтар.
     */
    public static String max(String a, String b) {
        return a.length() >= b.length() ? a : b;
    }
}
