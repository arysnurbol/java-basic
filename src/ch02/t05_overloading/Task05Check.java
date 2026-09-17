package ch02.t05_overloading;

import ch02.check.Check;

public class Task05Check {

    public static void run() {
        Check.task("Тапсырма 05 — Методтарды жүктеу (MathUtils.max)");

        Check.eq("max(3, 7) == 7", 7, MathUtils.max(3, 7));
        Check.eq("max(-5, -9) == -5", -5, MathUtils.max(-5, -9));

        Check.eq("max(2.5, 2.75) == 2.75", 2.75, MathUtils.max(2.5, 2.75), 1e-9);

        Check.eq("max(4, 9, 2) == 9", 9, MathUtils.max(4, 9, 2));
        Check.eq("max(10, 1, 3) == 10", 10, MathUtils.max(10, 1, 3));

        Check.eq("max(new int[]{1, 8, 3}) == 8", 8, MathUtils.max(new int[]{1, 8, 3}));
        Check.eq("max(new int[]{-4}) == -4", -4, MathUtils.max(new int[]{-4}));
        Check.throwsEx("бос массив -> IllegalArgumentException",
                IllegalArgumentException.class, () -> MathUtils.max(new int[0]));
        Check.throwsEx("null массив -> IllegalArgumentException",
                IllegalArgumentException.class, () -> MathUtils.max((int[]) null));

        Check.eq("max(\"abc\", \"ab\") == \"abc\"", "abc", MathUtils.max("abc", "ab"));
        Check.eq("ұзындығы тең -> бірінші қайтарылады", "aa", MathUtils.max("aa", "bb"));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
