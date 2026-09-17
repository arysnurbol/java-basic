package ch02.t07_varargs;

/**
 * Тапсырма 07 — Varargs (айнымалы ұзындықтағы аргументтер).
 * Кітап: "Varargs (аргументы переменной длины)" (86-б.)
 */
public class TextUtils {

    /** Барлық санның қосындысы. Аргумент берілмесе — 0. */
    public static int sum(int... numbers) {
        // TODO
        return 0;
    }

    /**
     * Бөлгіш арқылы жолдарды біріктіреді: join("-", "a", "b", "c") -> "a-b-c".
     * Бір де бөлік берілмесе — "" қайтар.
     */
    public static String join(String separator, String... parts) {
        // TODO
        return null;
    }

    /**
     * Орташа мән. Кемінде бір сан МІНДЕТТІ болуы үшін бірінші параметр бөлек тұр —
     * бұл varargs-пен жиі қолданылатын әдіс.
     */
    public static double average(double first, double... rest) {
        // TODO
        return 0;
    }

    /** Ең ұзын жолды қайтарады. Ештеңе берілмесе — null. */
    public static String longest(String... values) {
        // TODO
        return null;
    }
}
