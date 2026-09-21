package ch03.t16_executors;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Тапсырма 16 — Executors, Callable, Future, CountDownLatch.
 * Кітап: "Фреймворк Executors", "Future и Callable",
 *        "Синхронизаторы: CountDownLatch и CyclicBarrier" (141–142 б.)
 *
 * НЕГЕ ExecutorService: `new Thread` әр тапсырмаға жаңа ағын жасайды — қымбат.
 * Пул ағындарды ҚАЙТА ПАЙДАЛАНАДЫ.
 *
 *   ExecutorService pool = Executors.newFixedThreadPool(4);
 *   Future<Integer> f = pool.submit(() -> 42);   // Callable — нәтиже ҚАЙТАРАДЫ
 *   int result = f.get();                        // БЛОКТАЙДЫ — нәтиже дайын болғанша күтеді
 *   pool.shutdown();                             // МІНДЕТТІ!
 *
 * Runnable vs Callable: Runnable ештеңе қайтармайды әрі checked exception лақтыра алмайды,
 * Callable<T> — екеуін де істейді.
 *
 * ЕҢ ЖИІ ҚАТЕ: shutdown() шақырмау. Пул ағындары daemon емес, сондықтан
 * бағдарлама аяқталғанда да JVM ТОҚТАМАЙДЫ — қолмен үзуге тура келеді.
 * Сондықтан try/finally ішінде немесе try-with-resources-пен жаз.
 */
public class ExecutorLab {

    /**
     * Барлық тапсырманы пулда орындап, нәтижелерді СОЛ РЕТПЕН қайтарады.
     * Кеңес: pool.invokeAll(tasks) — бәрі біткенше күтеді әрі ретін сақтайды.
     * Пулды МІНДЕТТІ түрде shutdown жаса.
     */
    public static List<String> runAll(List<Callable<String>> tasks) throws Exception {
        return null; // TODO
    }

    /**
     * Тізімнің қосындысын threadCount ағынмен есептейді.
     * Әр элементті бөлек Callable ретінде submit жасап, Future.get() арқылы жина.
     * Нәтиже реттік қосындымен бірдей болуы керек.
     */
    public static int sumConcurrently(List<Integer> values, int threadCount) throws Exception {
        return 0; // TODO
    }

    /**
     * Пулға 30 мс "ойланатын" Callable береді де, нәтижесін күтіп қайтарады:
     * "Salem, <name>!". get() блоктайтынын өз көзіңмен көр.
     */
    public static String slowGreeting(String name) throws Exception {
        return null; // TODO
    }

    /**
     * CountDownLatch тәжірибесі: workers ағын іске қосады, әрқайсысы ортақ санағышты
     * арттырып, latch.countDown() жасайды. Негізгі ағын latch.await() арқылы
     * БӘРІН күтеді де, санағыштың мәнін қайтарады (әрқашан workers болуы керек).
     */
    public static int awaitWorkers(int workers) throws Exception {
        return 0; // TODO
    }

    /**
     * 2 секунд орындалатын тапсырма береді де, future.get(100, MILLISECONDS) шақырады.
     * TimeoutException ұсталса true қайтар (әрі пулды shutdownNow жаса).
     * Мағынасы: Future.get()-тің уақыт шектеуі бар нұсқасы мәңгі тұрып қалудан сақтайды.
     */
    public static boolean timesOut() throws Exception {
        return false; // TODO
    }
}
