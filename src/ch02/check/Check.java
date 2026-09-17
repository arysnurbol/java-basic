package ch02.check;

import java.util.Objects;

/**
 * Кішкентай тексеру харнесі (JUnit-тің орнына, сыртқы кітапханасыз).
 *
 * НАЗАР АУДАР: бұл файлды өзгертудің қажеті жоқ — тек оқы.
 * Мұндағы жалғыз таныс емес нәрсе — throwsEx() ішіндегі лямбда (`() -> ...`),
 * ол 3-главада өтеді. Әзірге "мына кодты орындап көр" дегенді білдіреді деп есте.
 */
public final class Check {

    private static int passed = 0;
    private static int failed = 0;

    private Check() {
    }

    /** Жаңа тапсырманың тақырыбын басып шығарады. */
    public static void task(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    /** Күтілген мән мен нақты мәнді салыстырады (equals арқылы). */
    public static void eq(String label, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            pass(label);
        } else {
            fail(label + "  (күтілген: " + show(expected) + ", келген: " + show(actual) + ")");
        }
    }

    /** Бөлшек сандарды дәлдік (delta) шегінде салыстырады. */
    public static void eq(String label, double expected, double actual, double delta) {
        if (Math.abs(expected - actual) <= delta) {
            pass(label);
        } else {
            fail(label + "  (күтілген: " + expected + " ±" + delta + ", келген: " + actual + ")");
        }
    }

    public static void isTrue(String label, boolean condition) {
        if (condition) {
            pass(label);
        } else {
            fail(label + "  (күтілген: true, келген: false)");
        }
    }

    public static void isFalse(String label, boolean condition) {
        if (!condition) {
            pass(label);
        } else {
            fail(label + "  (күтілген: false, келген: true)");
        }
    }

    /** Екеуі де ДӘЛ сол бір объект пе (==, equals емес). */
    public static void same(String label, Object a, Object b) {
        if (a == b) {
            pass(label);
        } else {
            fail(label + "  (күтілген: бір объект, келген: екі бөлек объект)");
        }
    }

    /** Екеуі бөлек объект пе (жаңа объект қайтарылды ма). */
    public static void notSame(String label, Object a, Object b) {
        if (a != b) {
            pass(label);
        } else {
            fail(label + "  (күтілген: жаңа объект, келген: сол бір объект)");
        }
    }

    /** Код көрсетілген түрдегі exception лақтыра ма. */
    public static void throwsEx(String label, Class<? extends Throwable> expected, Runnable body) {
        try {
            body.run();
            fail(label + "  (ешқандай exception лақтырылмады)");
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                pass(label);
            } else {
                fail(label + "  (күтілген: " + expected.getSimpleName()
                        + ", келген: " + t.getClass().getSimpleName() + ")");
            }
        }
    }

    public static void pass(String label) {
        passed++;
        System.out.println("  [ OK ] " + label);
    }

    public static void fail(String label) {
        failed++;
        System.out.println("  [FAIL] " + label);
    }

    /** Тапсырма орындалу кезінде мүлде құлап қалса. */
    public static void crashed(String taskName, Throwable t) {
        failed++;
        System.out.println("  [CRASH] " + taskName + " -> " + t);
    }

    /** Қорытынды. Барлығы өтсе 0, әйтпесе 1 қайтарады. */
    public static int summary() {
        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("  Өтті: " + passed + "   Өтпеді: " + failed);
        System.out.println(failed == 0
                ? "  Бәрі дұрыс! Келесі тақырыпқа өтуге болады."
                : "  Әлі жұмыс бар. [FAIL] жазуларын жоғарыдан қара.");
        System.out.println("---------------------------------------------");
        return failed == 0 ? 0 : 1;
    }

    private static String show(Object value) {
        if (value == null) {
            return "null";
        }
        return value instanceof String ? "\"" + value + "\"" : value.toString();
    }
}
