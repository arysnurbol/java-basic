package ch02.t10_polymorphism;

public class Triangle extends Shape {

    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        super("Triangle");
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        // TODO
        return 0;
    }
}
