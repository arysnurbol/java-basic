package ch02.t07_varargs;

import ch02.check.Check;

public class Task07Check {

    public static void run() {
        Check.task("Тапсырма 07 — Varargs (TextUtils)");

        Check.eq("sum() == 0", 0, TextUtils.sum());
        Check.eq("sum(5) == 5", 5, TextUtils.sum(5));
        Check.eq("sum(1, 2, 3, 4) == 10", 10, TextUtils.sum(1, 2, 3, 4));
        Check.eq("массивті де қабылдайды", 6, TextUtils.sum(new int[]{1, 2, 3}));

        Check.eq("join(\"-\", \"a\", \"b\", \"c\")", "a-b-c", TextUtils.join("-", "a", "b", "c"));
        Check.eq("бір бөлік -> бөлгіш жоқ", "a", TextUtils.join("-", "a"));
        Check.eq("бөлік жоқ -> бос жол", "", TextUtils.join("-"));
        Check.eq("бөлгіш ретінде \", \"", "Aisha, Bolat", TextUtils.join(", ", "Aisha", "Bolat"));

        Check.eq("average(4) == 4", 4.0, TextUtils.average(4), 1e-9);
        Check.eq("average(1, 2, 3) == 2", 2.0, TextUtils.average(1, 2, 3), 1e-9);

        Check.eq("longest() == null", null, TextUtils.longest());
        Check.eq("longest(\"aa\", \"b\", \"cccc\")", "cccc", TextUtils.longest("aa", "b", "cccc"));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
