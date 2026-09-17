package ch02.t03_constructors;

/**
 * Тапсырма 03 — Конструктор түрлері.
 * Кітап: "Типы конструкторов" (77-б.)
 */
public class Rectangle {

    private double width;
    private double height;

    /** Конструктор жоқ (no-arg): 1 x 1 тіктөртбұрыш. */
    public Rectangle() {
        // TODO
    }

    /** Бір параметрлі: шаршы (width == height == side). */
    public Rectangle(double side) {
        // TODO
    }

    /** Екі параметрлі. */
    public Rectangle(double width, double height) {
        // TODO
    }

    /** Көшірме конструкторы (copy constructor): басқа объектінің көшірмесі. */
    public Rectangle(Rectangle other) {
        // TODO
    }

    public double getWidth() {
        // TODO
        return 0;
    }

    public double getHeight() {
        // TODO
        return 0;
    }

    public double area() {
        // TODO
        return 0;
    }

    public double perimeter() {
        // TODO
        return 0;
    }
}
