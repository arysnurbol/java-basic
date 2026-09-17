package ch02.t02_static;

/**
 * Тапсырма 02 — Статикалық мүшелер мен методтар.
 * Кітап: "Статические члены и методы" (75-б.)
 */
public class Student {

    // TODO: private static int count — жасалған студенттердің ортақ санағышы.

    // TODO: private final int id;  (әр объектінің өз нөмірі)
    // TODO: private final String name;

    public Student(String name) {
        // TODO: count-ты 1-ге өсір, жаңа мәнді осы объектінің id-іне бер.
        //       Яғни бірінші студент id = 1, екіншісі id = 2, ...
    }

    public int getId() {
        // TODO
        return 0;
    }

    public String getName() {
        // TODO
        return null;
    }

    /** Барлығы неше Student объектісі жасалды. */
    public static int getCount() {
        // TODO
        return 0;
    }

    /** Санағышты нөлдеу (тек тест үшін керек). */
    public static void resetCount() {
        // TODO
    }
}
