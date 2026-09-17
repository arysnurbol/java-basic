package ch02.t10_polymorphism;

public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }

    @Override
    public double area() {
        // TODO: Math.PI * radius * radius
        return 0;
    }
}
