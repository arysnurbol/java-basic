package ch03;

import ch03.check.Check;

/**
 * Глава 3 бойынша барлық тапсырманы тексереді.
 *
 *   java -cp out ch03.Ch03Runner        -> барлығы
 *   java -cp out ch03.Ch03Runner 3      -> тек 3-тапсырма
 *   java -cp out ch03.Ch03Runner 3 4 5  -> бірнешеуі
 */
public class Ch03Runner {

    private static final int TASK_COUNT = 20;

    public static void main(String[] args) {
        System.out.println("Глава 3 — Java-ның кеңейтілген мүмкіндіктері");

        for (int i = 1; i <= TASK_COUNT; i++) {
            if (!selected(args, i)) {
                continue;
            }
            try {
                runTask(i);
            } catch (Throwable t) {
                Check.crashed("Тапсырма " + i, t);
            }
        }

        System.exit(Check.summary());
    }

    private static boolean selected(String[] args, int task) {
        if (args.length == 0) {
            return true;
        }
        for (String arg : args) {
            try {
                if (Integer.parseInt(arg.trim()) == task) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                // сан емес аргументті елемейміз
            }
        }
        return false;
    }

    private static void runTask(int n) throws Exception {
        switch (n) {
            case 1 -> ch03.t01_generic_class.Task01Check.run();
            case 2 -> ch03.t02_generic_methods.Task02Check.run();
            case 3 -> ch03.t03_bounded_wildcards.Task03Check.run();
            case 4 -> ch03.t04_generic_interface.Task04Check.run();
            case 5 -> ch03.t05_lambda_basics.Task05Check.run();
            case 6 -> ch03.t06_functional_interfaces.Task06Check.run();
            case 7 -> ch03.t07_method_refs.Task07Check.run();
            case 8 -> ch03.t08_list_set.Task08Check.run();
            case 9 -> ch03.t09_map.Task09Check.run();
            case 10 -> ch03.t10_queue_deque.Task10Check.run();
            case 11 -> ch03.t11_stream_ops.Task11Check.run();
            case 12 -> ch03.t12_collectors.Task12Check.run();
            case 13 -> ch03.t13_streams_advanced.Task13Check.run();
            case 14 -> ch03.t14_threads.Task14Check.run();
            case 15 -> ch03.t15_synchronization.Task15Check.run();
            case 16 -> ch03.t16_executors.Task16Check.run();
            case 17 -> ch03.t17_reflection.Task17Check.run();
            case 18 -> ch03.t18_annotations.Task18Check.run();
            case 19 -> ch03.t19_file_io.Task19Check.run();
            case 20 -> ch03.t20_exceptions.Task20Check.run();
            default -> throw new IllegalArgumentException("Белгісіз тапсырма: " + n);
        }
    }
}
