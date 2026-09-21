package ch03.t02_generic_methods;

import ch03.check.Check;

import java.util.List;

public class Task02Check {

    public static void run() {
        Check.task("Тапсырма 02 — Обобщенные методы (ArrayUtils)");

        String[] names = {"Aisha", "Bolat", "Chingiz"};
        Integer[] numbers = {3, 1, 3, 7};

        Check.eq("join(String[], \"-\")", "Aisha-Bolat-Chingiz", ArrayUtils.join(names, "-"));
        Check.eq("join(Integer[], \", \")", "3, 1, 3, 7", ArrayUtils.join(numbers, ", "));
        Check.eq("бос массив -> \"\"", "", ArrayUtils.join(new String[0], "-"));
        Check.eq("null массив -> \"\"", "", ArrayUtils.join(null, "-"));
        Check.eq("ішіндегі null -> \"null\"", "a|null", ArrayUtils.join(new String[]{"a", null}, "|"));

        Check.eq("firstOrDefault толы массивте", "Aisha", ArrayUtils.firstOrDefault(names, "—"));
        Check.eq("firstOrDefault бос массивте", "—", ArrayUtils.firstOrDefault(new String[0], "—"));
        Check.eq("firstOrDefault null массивте", 0, ArrayUtils.firstOrDefault(null, 0));

        String[] toSwap = {"a", "b", "c"};
        ArrayUtils.swap(toSwap, 0, 2);
        Check.eq("swap(0, 2) массивтің ӨЗІН өзгертеді", "c-b-a", ArrayUtils.join(toSwap, "-"));

        Check.eq("countEquals(3)", 2, ArrayUtils.countEquals(numbers, 3));
        Check.eq("countEquals(9)", 0, ArrayUtils.countEquals(numbers, 9));
        Check.eq("countEquals(null) — Objects.equals керек", 2,
                ArrayUtils.countEquals(new String[]{null, "x", null}, null));

        List<String> list = ArrayUtils.toList(names);
        Check.eq("toList() өлшемі", 3, list == null ? -1 : list.size());
        Check.noThrow("toList() нәтижесіне add жасауға болады (Arrays.asList ЕМЕС)",
                () -> list.add("Dana"));

        Check.isTrue("sameElements — бірдей", ArrayUtils.sameElements(names, new String[]{"Aisha", "Bolat", "Chingiz"}));
        Check.isFalse("sameElements — реті басқа", ArrayUtils.sameElements(names, new String[]{"Bolat", "Aisha", "Chingiz"}));
        Check.isFalse("sameElements — ұзындығы басқа", ArrayUtils.sameElements(names, new String[]{"Aisha"}));
        Check.isTrue("sameElements — екеуі де null", ArrayUtils.sameElements((String[]) null, null));
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
