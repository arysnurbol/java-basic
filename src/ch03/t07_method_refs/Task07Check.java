package ch03.t07_method_refs;

import ch03.check.Check;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Task07Check {

    public static void run() {
        Check.task("Тапсырма 07 — Ссылки на методы (MethodRefLab)");

        Function<String, Integer> parser = MethodRefLab.parser();
        Check.eq("parser().apply(\"42\")", 42, parser == null ? null : parser.apply("42"));
        Check.eq("parser().apply(\"-7\")", -7, parser == null ? null : parser.apply("-7"));
        Check.throwsEx("parser().apply(\"abc\") -> NumberFormatException",
                NumberFormatException.class, () -> MethodRefLab.parser().apply("abc"));

        Function<String, String> upper = MethodRefLab.upperCaser();
        Check.eq("upperCaser()", "SALEM", upper == null ? null : upper.apply("salem"));

        Supplier<Integer> len = MethodRefLab.lengthOf("Kazakhstan");
        Check.eq("lengthOf(\"Kazakhstan\").get()", 10, len == null ? null : len.get());
        Check.eq("lengthOf(\"\").get()", 0,
                MethodRefLab.lengthOf("") == null ? null : MethodRefLab.lengthOf("").get());

        Supplier<List<String>> factory = MethodRefLab.listFactory();
        Check.notNull("listFactory() null емес", factory);
        if (factory != null) {
            List<String> a = factory.get();
            List<String> b = factory.get();
            Check.eq("ArrayList::new -> бос тізім", 0, a.size());
            Check.notSame("шақырған сайын ЖАҢА тізім", a, b);
        }

        Function<String, StringBuilder> builders = MethodRefLab.builderFactory();
        Check.eq("builderFactory().apply(\"hi\")", "hi",
                builders == null ? null : builders.apply("hi").toString());

        BiFunction<String, String, Boolean> sw = MethodRefLab.startsWith();
        Check.eq("startsWith(\"Salem\", \"Sa\")", true, sw == null ? null : sw.apply("Salem", "Sa"));
        Check.eq("startsWith(\"Salem\", \"le\")", false, sw == null ? null : sw.apply("Salem", "le"));

        Comparator<String> byLength = MethodRefLab.byLength();
        Check.notNull("byLength() null емес", byLength);
        if (byLength != null) {
            Check.isTrue("byLength: \"ab\" < \"abc\"", byLength.compare("ab", "abc") < 0);
            Check.eq("byLength: тең -> 0", 0, byLength.compare("xy", "ab"));
        }

        Check.eq("parseAll([\"1\",\"2\",\"30\"])", List.of(1, 2, 30),
                MethodRefLab.parseAll(List.of("1", "2", "30")));
        Check.eq("parseAll(бос тізім)", List.of(), MethodRefLab.parseAll(List.of()));
        Check.throwsEx("parseAll([\"1\",\"x\"]) -> NumberFormatException",
                NumberFormatException.class, () -> MethodRefLab.parseAll(List.of("1", "x")));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
