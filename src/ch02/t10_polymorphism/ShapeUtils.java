package ch02.t10_polymorphism;

import java.util.Arrays;
import java.util.Objects;

/**
 * Тапсырма 10 — полиморфизмнің басты пайдасы:
 * бұл методтар нақты қандай фигура келгенін БІЛУДІҢ ҚАЖЕТІ ЖОҚ.
 * Ешқандай if / instanceof қолданба.
 */
public class ShapeUtils {

    /** Барлық фигураның жалпы ауданы. Массив null болса — 0. */
    public static double totalArea(Shape[] shapes) {
        /*return Arrays.stream(shapes == null ? new Shape[0] : shapes)
                .filter(Objects::nonNull)
                .mapToDouble(Shape::area) // Нақты қандай фигура екенін білу маңызды емес!
                .sum();*/

        if (shapes == null) {
            return 0;
        }

        double sum = 0;
        for (Shape shape : shapes) {
            if (shape != null) { // Массив ішіндегі элемент null емес екеніне көз жеткіземіз
                sum += shape.area(); // Полиморфизм: әр фигура өз ауданын есептеп береді
            }
        }
        return sum;
    }

    /** Ауданы ең үлкен фигура. Массив бос немесе null болса — null. */
    public static Shape largest(Shape[] shapes) {
//        return Arrays.stream(shapes == null ? new Shape[0] : shapes)
//                .filter(Objects::nonNull)
//                .max((s1, s2) -> Double.compare(s1.area(), s2.area())) // Тағы да полиморфизм
//                .orElse(null);
        
        if (shapes == null || shapes.length == 0) {
            return null;
        }

        Shape largestShape = null;
        double maxArea = -1; // Аудандар әрдайым 0-ден үлкен не тең болады

        for (Shape shape : shapes) {
            if (shape != null) {
                double currentArea = shape.area(); // Тағы да полиморфизм қолданылуда
                if (currentArea > maxArea) {
                    maxArea = currentArea;
                    largestShape = shape;
                }
            }
        }
        return largestShape;
    }
}
