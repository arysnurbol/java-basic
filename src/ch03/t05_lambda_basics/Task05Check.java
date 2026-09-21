package ch03.t05_lambda_basics;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Task05Check {

    public static void run() {
        Check.task("Тапсырма 05 — Лямбда-выражения (LambdaBasics)");

        Comparator<String> byLength = LambdaBasics.byLength();
        Check.notNull("byLength() null емес", byLength);
        if (byLength != null) {
            Check.isTrue("byLength: \"ab\" < \"abc\"", byLength.compare("ab", "abc") < 0);
            Check.isTrue("byLength: \"abc\" > \"ab\"", byLength.compare("abc", "ab") > 0);
            Check.eq("byLength: тең ұзындық -> 0", 0, byLength.compare("ab", "cd"));
            Check.eq("лямбда мен анонимді класс БІРДЕЙ нәтиже береді",
                    LambdaBasics.LEGACY_BY_LENGTH.compare("aaaa", "b"),
                    byLength.compare("aaaa", "b"));
        }

        Comparator<String> full = LambdaBasics.byLengthThenAlpha();
        Check.notNull("byLengthThenAlpha() null емес", full);
        if (full != null) {
            Check.isTrue("тең ұзындықта әліпби шешеді: \"ab\" < \"cd\"", full.compare("ab", "cd") < 0);
            Check.isTrue("ұзындық бірінші: \"zz\" < \"aaa\"", full.compare("zz", "aaa") < 0);
        }

        List<String> source = List.of("Chingiz", "Dana", "Aida", "Bo");
        Check.eq("sortedByLength()",
                List.of("Bo", "Aida", "Dana", "Chingiz"), LambdaBasics.sortedByLength(source));
        Check.eq("sortedByLength() кірісті ӨЗГЕРТПЕЙДІ",
                List.of("Chingiz", "Dana", "Aida", "Bo"), source);

        Check.eq("sortedDescending()",
                List.of("Dana", "Chingiz", "Bo", "Aida"), LambdaBasics.sortedDescending(source));

        List<String> mutable = new ArrayList<>(List.of("Chingiz", "Dana", "Bo"));
        LambdaBasics.sortInPlaceByLength(mutable);
        Check.eq("sortInPlaceByLength() тізімнің ӨЗІН реттейді",
                List.of("Bo", "Dana", "Chingiz"), mutable);

        StringBuilder sink = new StringBuilder();
        Runnable hello = LambdaBasics.greeter(sink, "Aisha");
        Check.eq("greeter() әлі ОРЫНДАЛМАДЫ — sink бос", "", sink.toString());
        if (hello != null) {
            hello.run();
            Check.eq("run() кейін", "Salem, Aisha!", sink.toString());
            hello.run();
            Check.eq("екі рет run() — ұсталған sink сол бір объект", "Salem, Aisha!Salem, Aisha!", sink.toString());
        } else {
            Check.fail("greeter() null қайтарды");
        }
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
