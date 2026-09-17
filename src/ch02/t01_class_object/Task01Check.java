package ch02.t01_class_object;

import ch02.check.Check;

public class Task01Check {

    public static void run() {
        Check.task("Тапсырма 01 — Класс және объект (Book)");

        Book java = new Book("Java", "Bachina", 2027);
        Book old = new Book("Grokking Algorithms", "Bhargava", 2016);

        Check.eq("describe() дұрыс пішімде", "Java (Bachina, 2027)", java.describe());
        Check.eq("екінші объектінің describe()", "Grokking Algorithms (Bhargava, 2016)", old.describe());

        Check.isTrue("2016 < 2027 -> isOlderThan(2027) == true", old.isOlderThan(2027));
        Check.isFalse("2027 < 2016 емес -> isOlderThan(2016) == false", java.isOlderThan(2016));
        Check.isFalse("дәл сол жыл -> isOlderThan(2027) == false", java.isOlderThan(2027));

        Check.eq("ageIn(2030) == 3", 3, java.ageIn(2030));
        Check.eq("ageIn(2016) == 0", 0, old.ageIn(2016));

        Check.isTrue("өрістер private", Task01Check.allFieldsPrivate());
    }

    private static boolean allFieldsPrivate() {
        java.lang.reflect.Field[] fields = Book.class.getDeclaredFields();
        if (fields.length == 0) {
            return false;
        }
        for (java.lang.reflect.Field f : fields) {
            if (!java.lang.reflect.Modifier.isPrivate(f.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
