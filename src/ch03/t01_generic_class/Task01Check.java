package ch03.t01_generic_class;

import ch03.check.Check;

public class Task01Check {

    public static void run() {
        Check.task("Тапсырма 01 — Обобщенный класс (Box, Pair)");

        Box<String> empty = new Box<>();
        Check.isTrue("new Box<>() -> isEmpty()", empty.isEmpty());
        Check.eq("бос жәшіктің get() -> null", null, empty.get());
        Check.eq("getOrDefault(\"—\")", "—", empty.getOrDefault("—"));
        Check.eq("бос жәшіктің describe()", "Box[empty]", empty.describe());

        empty.set("hello");
        Check.isFalse("set() -> енді бос емес", empty.isEmpty());
        // Мына жолда (String) cast ЖОҚ — генериктің басты пайдасы осы:
        String value = empty.get();
        Check.eq("set() -> get()", "hello", value);
        Check.eq("describe()", "Box[hello]", empty.describe());

        Box<Integer> numeric = Box.of(42);
        Check.notNull("Box.of(42) null емес", numeric);
        if (numeric != null) {
            int unboxed = numeric.get();   // cast-сыз int-ке шешіледі
            Check.eq("Box.of(42).get()", 42, unboxed);
            Check.eq("Box<Integer>.describe()", "Box[42]", numeric.describe());
            Check.eq("getOrDefault бос емес жәшікте fallback-ті елемейді", 42, numeric.getOrDefault(0));

            // Типтерді өшіру (стирание типов): орындалу кезінде екеуі де жай Box
            Check.same("Box<String> мен Box<Integer> — орындалуда БІР класс (type erasure)",
                    Box.of("x").getClass(), numeric.getClass());
        }

        Pair<String, Integer> age = Pair.of("Aisha", 25);
        Check.notNull("Pair.of(...) null емес", age);
        if (age != null) {
            Check.eq("Pair.getKey()", "Aisha", age.getKey());
            Check.eq("Pair.getValue()", 25, age.getValue());
            Check.eq("Pair.toString()", "(Aisha, 25)", age.toString());

            Pair<Integer, String> swapped = age.swap();
            Check.notNull("swap() null емес", swapped);
            if (swapped != null) {
                Check.eq("swap() кілті", 25, swapped.getKey());
                Check.eq("swap() мәні", "Aisha", swapped.getValue());
                Check.eq("swap().toString()", "(25, Aisha)", swapped.toString());
                Check.notSame("swap() ЖАҢА объект қайтарады", age, age.swap().swap());
                Check.eq("екі рет swap -> бастапқы пішім", "(Aisha, 25)", age.swap().swap().toString());
            }
        }
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
