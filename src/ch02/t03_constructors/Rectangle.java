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
        this.width = 1;
        this.height = 1;
    }

    /** Бір параметрлі: шаршы (width == height == side). */
    public Rectangle(double side) {
        this.width = side;
        this.height = side;
    }

    /** Екі параметрлі. */
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /** Көшірме конструкторы (copy constructor): басқа объектінің көшірмесі. */
    public Rectangle(Rectangle other) {
        this.width = other.width;
        this.height = other.height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double area() {
        return width * height;
    }

    public double perimeter() {
        return (width * 2) + (height * 2);
    }
}
