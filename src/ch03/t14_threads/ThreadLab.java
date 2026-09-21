package ch03.t14_threads;

import java.util.concurrent.atomic.AtomicInteger;

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
        return null; // TODO
    }

    /** Ағынның күйі, жол түрінде: "NEW", "TERMINATED", ... */
    public static String stateOf(Thread thread) {
        return null; // TODO
    }

    /**
     * threadCount ағын жасайды, әрқайсысы ортақ санағышты incrementsPerThread рет арттырады.
     * БАРЛЫҒЫН күтіп (join), соңғы мәнді қайтарады.
     * AtomicInteger қолданылғандықтан нәтиже ӘРҚАШАН threadCount * incrementsPerThread.
     */
    public static int runAndJoin(int threadCount, int incrementsPerThread) throws InterruptedException {
        return 0; // TODO
    }

    /**
     * Берілген атпен ағын жасап, оның ІШІНДЕ Thread.currentThread().getName() оқиды
     * да, сол мәнді қайтарады (ағын аяқталғанша күт).
     * Кеңес: нәтижені лямбдадан шығару үшін бір элементті массив не AtomicReference керек —
     * лямбда жай жергілікті айнымалыға жаза алмайды (effectively final ережесі).
     */
    public static String nameInsideThread(String threadName) throws InterruptedException {
        return null; // TODO
    }

    /**
     * Ұзақ ұйықтайтын ағынды үзеді (interrupt).
     * InterruptedException ұсталса true қайтар.
     *
     * Қадамдар: sleep(5000) жасайтын ағын жаса -> start -> қысқа Thread.sleep(50)
     * (ұйқыға кіруін күту үшін) -> interrupt() -> join() -> жалаушаны қайтар.
     */
    public static boolean interruptSleepingThread() throws InterruptedException {
        return false; // TODO
    }

    /**
     * ЖАҢЫЛЫСТЫҢ КӨРСЕТІЛІМІ: start() емес, run() шақырады.
     * Бұл жаңа ағын ЖАСАМАЙДЫ — код сол ағында орындалады.
     * Ішінде орындалған ағынның атын қайтар (main ағында шақырылса "main" болады).
     */
    public static String runInsteadOfStart() {
        return null; // TODO
    }
}
