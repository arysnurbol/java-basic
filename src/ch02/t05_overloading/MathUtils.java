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
        // TODO
        return 0;
    }

    /** Екі бөлшек санның үлкенін қайтарады. */
    public static double max(double a, double b) {
        // TODO
        return 0;
    }

    /** Үш бүтін санның үлкенін қайтарады (жоғарыдағы max(int,int)-ті қайта қолдан). */
    public static int max(int a, int b, int c) {
        // TODO
        return 0;
    }

    /**
     * Массивтегі ең үлкен санды қайтарады.
     * Массив null немесе бос болса — IllegalArgumentException лақтыр.
     */
    public static int max(int[] numbers) {
        // TODO
        return 0;
    }

    /**
     * Ұзындығы бойынша ұзын жолды қайтарады.
     * Ұзындығы тең болса — бірінші аргументті (a) қайтар.
     */
    public static String max(String a, String b) {
        // TODO
        return null;
    }
}
