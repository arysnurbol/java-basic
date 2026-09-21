package ch03.t16_executors;

import ch03.check.Check;

import java.util.List;
import java.util.concurrent.Callable;

public class Task16Check {

    public static void run() throws Exception {
        Check.task("Тапсырма 16 — Executors, Callable, Future (ExecutorLab)");

        List<Callable<String>> tasks = List.of(
                () -> "bir",
                () -> { Thread.sleep(40); return "eki"; },   // әдейі баяу — реті бұзылмауы керек
                () -> "ush");
        Check.eq("invokeAll() нәтижелердің РЕТІН сақтайды (баяу тапсырма ортада тұрса да)",
                List.of("bir", "eki", "ush"), ExecutorLab.runAll(tasks));
        Check.eq("runAll(бос тізім)", List.of(), ExecutorLab.runAll(List.of()));

        Check.eq("sumConcurrently([1..10], 4)", 55,
                ExecutorLab.sumConcurrently(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4));
        Check.eq("sumConcurrently(бос, 2)", 0, ExecutorLab.sumConcurrently(List.of(), 2));
        Check.eq("sumConcurrently([-5, 5], 2)", 0, ExecutorLab.sumConcurrently(List.of(-5, 5), 2));

        Check.eq("slowGreeting(\"Aisha\")", "Salem, Aisha!", ExecutorLab.slowGreeting("Aisha"));

        Check.eq("awaitWorkers(5)", 5, ExecutorLab.awaitWorkers(5));
        Check.eq("awaitWorkers(1)", 1, ExecutorLab.awaitWorkers(1));
        Check.eq("awaitWorkers(0) — бірден өтеді", 0, ExecutorLab.awaitWorkers(0));

        long start = System.currentTimeMillis();
        Check.isTrue("timesOut() -> TimeoutException ұсталды", ExecutorLab.timesOut());
        long elapsed = System.currentTimeMillis() - start;
        Check.isTrue("get(100 мс) шынымен 2 секунд КҮТПЕЙДІ (өткен уақыт: " + elapsed + " мс)",
                elapsed < 1500);
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
