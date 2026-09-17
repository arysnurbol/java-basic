package ch02.t02_static;

import ch02.check.Check;

public class Task02Check {

    public static void run() {
        Check.task("Тапсырма 02 — Статикалық мүшелер (Student)");

        Student.resetCount();
        Check.eq("resetCount() кейін getCount() == 0", 0, Student.getCount());

        Student a = new Student("Aisha");
        Student b = new Student("Bolat");
        Student c = new Student("Chingiz");

        Check.eq("бірінші студенттің id-і 1", 1, a.getId());
        Check.eq("екінші студенттің id-і 2", 2, b.getId());
        Check.eq("үшінші студенттің id-і 3", 3, c.getId());

        Check.eq("аты сақталды", "Bolat", b.getName());
        Check.eq("getCount() == 3", 3, Student.getCount());

        Check.isTrue("count өрісі static", isStaticField("count"));
        Check.isTrue("id өрісі static ЕМЕС (әр объектінің өзінікі)", isInstanceField("id"));
    }

    private static java.lang.reflect.Field field(String name) {
        try {
            return Student.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private static boolean isStaticField(String name) {
        java.lang.reflect.Field f = field(name);
        return f != null && java.lang.reflect.Modifier.isStatic(f.getModifiers());
    }

    private static boolean isInstanceField(String name) {
        java.lang.reflect.Field f = field(name);
        return f != null && !java.lang.reflect.Modifier.isStatic(f.getModifiers());
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
