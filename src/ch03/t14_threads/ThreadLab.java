package ch03.t14_threads;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Тапсырма 14 — Ағындар (потоки): құру, іске қосу, күту.
 * Кітап: "Изучение параллелизма и многопоточности", "Создание и управление потоками в Java",
 *        "Состояния и жизненный цикл потока" (137–139 б.)
 *
 * АҒЫН ЖАСАУДЫҢ ЕКІ ЖОЛЫ:
 *   new Thread(() -> { ... })        <- Runnable беру. ӘРҚАШАН ОСЫНЫ ҚОЛДАН.
 *   class My extends Thread { ... }  <- мұрагерлік. Жалғыз мұрагерлік орныңды жоғалтасың.
 *
 * ӨМІРЛІК ЦИКЛ (Thread.State):
 *   NEW -> RUNNABLE -> (BLOCKED / WAITING / TIMED_WAITING) -> TERMINATED
 *
 * ЕКІ НЕГІЗГІ ҚАТЕ:
 *   1. start() орнына run() шақыру. run() — жай метод шақыруы, ЖАҢА АҒЫН ЖАСАЛМАЙДЫ.
 *   2. join() шақырмау. Онда main ағын нәтиже дайын болмай тұрып әрі қарай кетеді.
 */
public class ThreadLab {

    /**
     * sink-ті times рет арттыратын, БІРАҚ ӘЛІ ІСКЕ ҚОСЫЛМАҒАН ағын қайтарады.
     * start() шақырма — тексеріс оны өзі жасайды.
     */
    public static Thread counterThread(AtomicInteger sink, int times) {
        return new Thread(() -> {
            for (int i = 0; i < times; i++) {
                sink.incrementAndGet();
            }
        });
    }

    /** Ағынның күйі, жол түрінде: "NEW", "TERMINATED", ... */
    public static String stateOf(Thread thread) {
        return thread.getState().toString();
    }

    /**
     * threadCount ағын жасайды, әрқайсысы ортақ санағышты incrementsPerThread рет арттырады.
     * БАРЛЫҒЫН күтіп (join), соңғы мәнді қайтарады.
     * AtomicInteger қолданылғандықтан нәтиже ӘРҚАШАН threadCount * incrementsPerThread.
     */
    public static int runAndJoin(int threadCount, int incrementsPerThread) throws InterruptedException {
        AtomicInteger sharedCounter = new AtomicInteger(0);
        Thread[] threads = new Thread[threadCount];

        // 1. Ағындарды құру және іске қосу
        for (int i = 0; i < threadCount; i++) {
            threads[i] = counterThread(sharedCounter, incrementsPerThread);
            threads[i].start(); // Әр ағынды параллель іске қосамыз
        }

        // 2. Барлық ағынның жұмысын аяқтағанын күту
        for (Thread thread : threads) {
            thread.join(); // main ағын осы жерде ағын біткенше күтеді
        }

        return sharedCounter.get();
    }

    /**
     * Берілген атпен ағын жасап, оның ІШІНДЕ Thread.currentThread().getName() оқиды
     * да, сол мәнді қайтарады (ағын аяқталғанша күт).
     * Кеңес: нәтижені лямбдадан шығару үшін бір элементті массив не AtomicReference керек —
     * лямбда жай жергілікті айнымалыға жаза алмайды (effectively final ережесі).
     */
    public static String nameInsideThread(String threadName) throws InterruptedException {
        // Лямбда ішінен мәнді сыртқа шығару үшін AtomicReference қолданамыз
        AtomicReference<String> internalName = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            // Ағынның ішінде өз атын оқып, айнымалыға жазамыз
            internalName.set(Thread.currentThread().getName());
        }, threadName); // Ағынға ат береміз

        thread.start();
        thread.join(); // Ағын жұмысын бітіріп, атын жазып үлгеруі керек

        return internalName.get();
    }

    /**
     * Ұзақ ұйықтайтын ағынды үзеді (interrupt).
     * InterruptedException ұсталса true қайтар.
     *
     * Қадамдар: sleep(5000) жасайтын ағын жаса -> start -> қысқа Thread.sleep(50)
     * (ұйқыға кіруін күту үшін) -> interrupt() -> join() -> жалаушаны қайтар.
     */
    public static boolean interruptSleepingThread() throws InterruptedException {
        // InterruptedException орын алғанын жазу үшін бір элементті массив қолданамыз
        final boolean[] interruptedCaught = new boolean[1];

        Thread sleepingThread = new Thread(() -> {
            try {
                Thread.sleep(5000); // 5 секунд ұйықтату
            } catch (InterruptedException e) {
                interruptedCaught[0] = true; // Егер үзілсе (interrupt), жалаушаны true қыламыз
            }
        });

        sleepingThread.start();

        Thread.sleep(50); // sleepingThread ағынының ұйқыға кетіп үлгеруі үшін аздап күтеміз

        sleepingThread.interrupt(); // Ұйықтап жатқан ағынды үземіз (оятамыз)
        sleepingThread.join();      // Оның толық тоқтағанын күтеміз

        return interruptedCaught[0];
    }

    /**
     * ЖАҢЫЛЫСТЫҢ КӨРСЕТІЛІМІ: start() емес, run() шақырады.
     * Бұл жаңа ағын ЖАСАМАЙДЫ — код сол ағында орындалады.
     * Ішінде орындалған ағынның атын қайтар (main ағында шақырылса "main" болады).
     */
    public static String runInsteadOfStart() {
        AtomicReference<String> executedThreadName = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            executedThreadName.set(Thread.currentThread().getName());
        });

        // МАНЫЗДЫ: start() емес, run() шақырамыз!
        // Бұл жаңа ағын ашпайды, код ағымдағы ағында (мысалы, "main") орындалады.
        thread.run();

        return executedThreadName.get();
    }
}
