package ch02;

import ch02.check.Check;

/**
 * Глава 2 бойынша барлық тапсырманы тексереді.
 *
 *   java -cp out ch02.Ch02Runner        -> барлығы
 *   java -cp out ch02.Ch02Runner 3      -> тек 3-тапсырма
 *   java -cp out ch02.Ch02Runner 3 4 5  -> бірнешеуі
 */
public class Ch02Runner {

    private static final int TASK_COUNT = 16;

    public static void main(String[] args) {
        System.out.println("Глава 2 — ООП практикасы");

        for (int i = 1; i <= TASK_COUNT; i++) {
            if (!selected(args, i)) {
                continue;
            }
            try {
                runTask(i);
            } catch (Throwable t) {
                Check.crashed("Тапсырма " + i, t);
            }
        }

        System.exit(Check.summary());
    }

    private static boolean selected(String[] args, int task) {
        if (args.length == 0) {
            return true;
        }
        for (String arg : args) {
            try {
                if (Integer.parseInt(arg.trim()) == task) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                // сан емес аргументті елемейміз
            }
        }
        return false;
    }

    private static void runTask(int n) {
        switch (n) {
            case 1 -> ch02.t01_class_object.Task01Check.run();
            case 2 -> ch02.t02_static.Task02Check.run();
            case 3 -> ch02.t03_constructors.Task03Check.run();
            case 4 -> ch02.t04_chaining.Task04Check.run();
            case 5 -> ch02.t05_overloading.Task05Check.run();
            case 6 -> ch02.t06_parameters.Task06Check.run();
            case 7 -> ch02.t07_varargs.Task07Check.run();
            case 8 -> ch02.t08_inheritance.Task08Check.run();
            case 9 -> ch02.t09_object_methods.Task09Check.run();
            case 10 -> ch02.t10_polymorphism.Task10Check.run();
            case 11 -> ch02.t11_instanceof.Task11Check.run();
            case 12 -> ch02.t12_abstract.Task12Check.run();
            case 13 -> ch02.t13_interface.Task13Check.run();
            case 14 -> ch02.t14_encapsulation.Task14Check.run();
            case 15 -> ch02.t15_composition.Task15Check.run();
            case 16 -> ch02.t16_patterns.Task16Check.run();
            default -> throw new IllegalArgumentException("Белгісіз тапсырма: " + n);
        }
    }
}
