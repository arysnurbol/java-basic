package ch02.t07_varargs;

/**
 * Тапсырма 07 — Varargs (айнымалы ұзындықтағы аргументтер).
 * Кітап: "Varargs (аргументы переменной длины)" (86-б.)
 */
public class TextUtils {

    /** Барлық санның қосындысы. Аргумент берілмесе — 0. */
    public static int sum(int... numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    /**
     * Бөлгіш арқылы жолдарды біріктіреді: join("-", "a", "b", "c") -> "a-b-c".
     * Бір де бөлік берілмесе — "" қайтар.
     */
    public static String join(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int  i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * Орташа мән. Кемінде бір сан МІНДЕТТІ болуы үшін бірінші параметр бөлек тұр —
     * бұл varargs-пен жиі қолданылатын әдіс.
     */
    public static double average(double first, double... rest) {
        double sum = first;
        for (double number : rest) {
            sum += number;
        }
        return sum / (rest.length + 1);
    }

    /** Ең ұзын жолды қайтарады. Ештеңе берілмесе — null. */
    public static String longest(String... values) {
        String longest = null;
        for (String value : values) {
            if (longest == null || value.length() > longest.length()) {
                longest = value;
            }
        }
        return longest;
    }
}
