package ch03.t06_functional_interfaces;

import ch03.check.Check;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Task06Check {

    public static void run() {
        Check.task("Тапсырма 06 — Функционалдық интерфейстер (FunctionalLab)");

        // Өз интерфейсің лямбда қабылдайды
        Check.eq("transform() лямбдамен", "SALEM", FunctionalLab.transform("salem", s -> s.toUpperCase()));

        Transformer upper = s -> s.toUpperCase();
        Transformer exclaim = s -> s + "!";
        Transformer both = upper.andThen(exclaim);
        Check.notNull("andThen() null емес", both);
        if (both != null) {
            Check.eq("upper.andThen(exclaim) — АЛДЫМЕН upper", "SALEM!", both.apply("salem"));
        }
        // Реті шынымен маңызды екенін көрсететін жұп:
        Transformer trim = s -> s.trim();
        if (trim.andThen(exclaim) != null && exclaim.andThen(trim) != null) {
            Check.eq("trim.andThen(exclaim)", "salem!", trim.andThen(exclaim).apply("  salem  "));
            Check.eq("exclaim.andThen(trim) — басқа нәтиже!", "salem  !", exclaim.andThen(trim).apply("  salem  "));
        }
        Check.eq("Transformer.identity()", "salem", Transformer.identity() == null
                ? null : Transformer.identity().apply("salem"));

        Predicate<String> notBlank = FunctionalLab.notBlank();
        Check.notNull("notBlank() null емес", notBlank);
        if (notBlank != null) {
            Check.isTrue("notBlank(\"java\")", notBlank.test("java"));
            Check.isFalse("notBlank(\"\")", notBlank.test(""));
            Check.isFalse("notBlank(\"   \")", notBlank.test("   "));
            Check.isFalse("notBlank(null)", notBlank.test(null));
            Check.isTrue("negate() — дайын default метод", notBlank.negate().test(""));
        }

        Predicate<Integer> teen = FunctionalLab.inRange(13, 19);
        Check.notNull("inRange() null емес", teen);
        if (teen != null) {
            Check.isTrue("inRange(13,19).test(13) — шекара қосылады", teen.test(13));
            Check.isTrue("inRange(13,19).test(19)", teen.test(19));
            Check.isFalse("inRange(13,19).test(20)", teen.test(20));
            Check.isTrue("and() тізбегі", teen.and(n -> n % 2 == 0).test(14));
            Check.isFalse("and() тізбегі — тақ сан өтпейді", teen.and(n -> n % 2 == 0).test(15));
        }

        Function<String, Integer> dl = FunctionalLab.doubledLength();
        Check.eq("doubledLength(\"java\")", 8, dl == null ? null : dl.apply("java"));
        Check.eq("doubledLength(\"\")", 0, dl == null ? null : dl.apply(""));

        Supplier<List<String>> supplier = FunctionalLab.newList();
        Check.notNull("newList() null емес", supplier);
        if (supplier != null) {
            List<String> a = supplier.get();
            List<String> b = supplier.get();
            Check.eq("Supplier бос тізім береді", 0, a.size());
            Check.notSame("шақырған сайын ЖАҢА тізім", a, b);
            a.add("x");
            Check.eq("біріншісіне қосу екіншісіне әсер етпейді", 0, b.size());
        }

        Consumer<StringBuilder> add = FunctionalLab.appender(" world");
        StringBuilder sb = new StringBuilder("hello");
        if (add != null) {
            add.accept(sb);
            Check.eq("Consumer объектіні ӨЗГЕРТЕДІ", "hello world", sb.toString());
        } else {
            Check.fail("appender() null қайтарды");
        }

        BiFunction<Integer, Integer, String> sum = FunctionalLab.sumAsText();
        Check.eq("sumAsText(2, 3)", "5", sum == null ? null : sum.apply(2, 3));
        Check.eq("sumAsText(-1, 1)", "0", sum == null ? null : sum.apply(-1, 1));

        Check.eq("filter(сандар, жұп)", List.of(2, 4),
                FunctionalLab.filter(List.of(1, 2, 3, 4), n -> n % 2 == 0));
        Check.eq("filter(бос тізім)", List.of(), FunctionalLab.filter(List.<Integer>of(), n -> true));
        Check.eq("filter(notBlank)", List.of("a"),
                FunctionalLab.filter(List.of("a", "  "), FunctionalLab.notBlank()));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
