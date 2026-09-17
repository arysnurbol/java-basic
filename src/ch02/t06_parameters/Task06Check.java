package ch02.t06_parameters;

import ch02.check.Check;

public class Task06Check {

    public static void run() {
        Check.task("Тапсырма 06 — Параметрлерді беру (ParamLab)");

        int primitive = 5;
        ParamLab.increment(primitive);
        Check.eq("примитив ӨЗГЕРМЕЙДІ (by value)", 5, primitive);

        Counter counter = new Counter(5);
        ParamLab.incrementInside(counter);
        Check.eq("объектінің ІШІ өзгереді", 6, counter.getValue());

        Counter original = new Counter(1);
        ParamLab.reassign(original);
        Check.eq("параметрге жаңа объект беру шақырушыға әсер ЕТПЕЙДІ", 1, original.getValue());

        int[] numbers = {1, 2, 3};
        ParamLab.doubleAll(numbers);
        Check.eq("массив элементтері өзгереді: [0]", 2, numbers[0]);
        Check.eq("массив элементтері өзгереді: [2]", 6, numbers[2]);

        int[] same = {1, 2, 3};
        ParamLab.replaceArray(same);
        Check.eq("массивті ауыстыру шақырушыға әсер ЕТПЕЙДІ", 1, same[0]);

        int[] source = {1, 2, 3};
        int[] copy = ParamLab.doubledCopy(source);
        Check.eq("doubledCopy түпнұсқаға тимейді", 1, source[0]);
        Check.isTrue("doubledCopy null емес", copy != null);
        if (copy != null && copy.length == 3) {
            Check.eq("көшірмедегі [0] == 2", 2, copy[0]);
            Check.eq("көшірмедегі [2] == 6", 6, copy[2]);
        } else {
            Check.fail("doubledCopy ұзындығы 3 массив қайтаруы керек");
        }
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
