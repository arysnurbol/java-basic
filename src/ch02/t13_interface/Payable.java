package ch02.t13_interface;

/**
 * Тапсырма 13 — Интерфейс: тұрақты, abstract метод, default метод, static метод.
 * Кітап: "Интерфейсы" (101-б.)
 */
public interface Payable {

    /** Интерфейстегі өріс автоматты түрде public static final. */
    double DEFAULT_TAX_RATE = 0.10;

    /** Салық шегерілмеген жалақы. Әр класс өзінше есептейді. */
    double grossPay();

    /**
     * Салық шегерілген жалақы — ортақ әдепкі іске асыру.
     * Ұрпақ кластар қаласа қайта жазады.
     */
    default double netPay() {
        return grossPay() * (1 - DEFAULT_TAX_RATE);
    }

    /** Интерфейстегі статикалық метод — көмекші функция. */
    static String currency() {
        return "KZT";
    }
}
