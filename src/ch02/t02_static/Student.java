package ch02.t02_static;

/**
 * Тапсырма 02 — Статикалық мүшелер мен методтар.
 * Кітап: "Статические члены и методы" (75-б.)
 */
public class Student {

    private static int count; // — жасалған студенттердің ортақ санағышы.
    private final int id; //  (әр объектінің өз нөмірі)
    private final String name;

    public Student(String name) {
        this.name = name;
        this.id = ++count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** Барлығы неше Student объектісі жасалды. */
    public static int getCount() {
        return Student.count;
    }

    /** Санағышты нөлдеу (тек тест үшін керек). */
    public static void resetCount() {
        Student.count = 0;
    }
}
