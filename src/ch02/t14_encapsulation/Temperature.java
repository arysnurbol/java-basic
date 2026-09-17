package ch02.t14_encapsulation;

/**
 * Тапсырма 14 — тек оқуға арналған (immutable / read-only) класс.
 * Кітап: "Классы только для чтения и только для записи" (106-б.)
 *
 * Мұнда бірде-бір setter жоқ: өзгерту керек болса — ЖАҢА объект қайтарылады.
 */
public final class Temperature {

    private final double celsius;

    public Temperature(double celsius) {
        this.celsius = celsius;
    }

    public double getCelsius() {
        // TODO
        return 0;
    }

    /** Фаренгейтке айналдыру: c * 9 / 5 + 32. */
    public double getFahrenheit() {
        // TODO
        return 0;
    }

    /** Осы объектіні ӨЗГЕРТПЕЙ, delta градусқа жылы ЖАҢА объект қайтарады. */
    public Temperature plus(double delta) {
        // TODO
        return null;
    }
}
