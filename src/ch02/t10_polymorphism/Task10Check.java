package ch02.t10_polymorphism;

import ch02.check.Check;

public class Task10Check {

    public static void run() {
        Check.task("Тапсырма 10 — Полиморфизм (Shape)");

        Shape circle = new Circle(5);
        Shape square = new Square(4);
        Shape triangle = new Triangle(6, 3);

        Check.eq("Circle.area()", Math.PI * 25, circle.area(), 1e-6);
        Check.eq("Square.area()", 16.0, square.area(), 1e-9);
        Check.eq("Triangle.area()", 9.0, triangle.area(), 1e-9);

        Check.eq("describe() пішімі", "Square: 16.00", square.describe());
        Check.eq("describe() базалық класста бір рет жазылған", "Triangle: 9.00", triangle.describe());

        Shape[] shapes = {circle, square, triangle};
        Check.eq("totalArea()", Math.PI * 25 + 16 + 9, ShapeUtils.totalArea(shapes), 1e-6);
        Check.eq("бос массив -> 0", 0.0, ShapeUtils.totalArea(new Shape[0]), 1e-9);
        Check.eq("null -> 0", 0.0, ShapeUtils.totalArea(null), 1e-9);

        Check.same("largest() -> шеңбер", circle, ShapeUtils.largest(shapes));
        Check.eq("largest(бос) -> null", null, ShapeUtils.largest(new Shape[0]));

        Check.isTrue("describe() ұрпақ кластарда қайта жазылмаған",
                !declares(Circle.class, "describe")
                        && !declares(Square.class, "describe")
                        && !declares(Triangle.class, "describe"));
    }

    private static boolean declares(Class<?> type, String methodName) {
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
