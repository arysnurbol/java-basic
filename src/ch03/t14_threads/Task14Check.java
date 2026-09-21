package ch03.t14_threads;

import ch03.check.Check;

import java.util.concurrent.atomic.AtomicInteger;

public class Task14Check {

    public static void run() throws Exception {
        Check.task("Тапсырма 14 — Ағындар: құру, іске қосу, күту (ThreadLab)");

        AtomicInteger sink = new AtomicInteger();
        Thread thread = ThreadLab.counterThread(sink, 5);
        Check.notNull("counterThread() null емес", thread);
        if (thread != null) {
            Check.eq("әлі іске қосылмаған -> күйі NEW", "NEW", ThreadLab.stateOf(thread));
            Check.eq("әлі орындалмады -> sink == 0", 0, sink.get());

            thread.start();
            thread.join();

            Check.eq("join() кейін sink == 5", 5, sink.get());
            Check.eq("аяқталған ағынның күйі", "TERMINATED", ThreadLab.stateOf(thread));
        }

        Check.eq("runAndJoin(4, 1000) — AtomicInteger, сондықтан ӘРҚАШАН дәл",
                4000, ThreadLab.runAndJoin(4, 1000));
        Check.eq("runAndJoin(1, 10)", 10, ThreadLab.runAndJoin(1, 10));
        Check.eq("runAndJoin(0, 100) — ағынсыз", 0, ThreadLab.runAndJoin(0, 100));

        Check.eq("nameInsideThread(\"worker-1\")", "worker-1", ThreadLab.nameInsideThread("worker-1"));
        Check.eq("nameInsideThread(\"esepteuish\")", "esepteuish", ThreadLab.nameInsideThread("esepteuish"));

        Check.isTrue("interruptSleepingThread() -> InterruptedException ұсталды",
                ThreadLab.interruptSleepingThread());

        Check.eq("run() ЖАҢА АҒЫН ЖАСАМАЙДЫ — код main ағында орындалады",
                Thread.currentThread().getName(), ThreadLab.runInsteadOfStart());
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
