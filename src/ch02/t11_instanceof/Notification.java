package ch02.t11_instanceof;

/**
 * Тапсырма 11 — instanceof және ковариантты қайтару типтері.
 * Кітап: "Оператор instanceof", "Ковариантные возвращаемые типы" (97-98-б.)
 */
public class Notification {

    private final String message;

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Осы хабарламаның көшірмесін қайтарады.
     * Ұрпақ кластарда қайтару типі НАҚТЫЛАНАДЫ — бұл ковариантты қайтару типі.
     */
    public Notification copy() {
        return new Notification(message);
    }
}
