package ch02.t10_polymorphism;

public class Square extends Shape {

    private final double side;

    public Square(double side) {
        super("Square");
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }
}
