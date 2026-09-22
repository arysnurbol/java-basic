package ch03.t16_executors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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
        // Тапсырмалар санына қарай пул құрамыз
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, tasks.size()));
        try {
            // invokeAll барлық тапсырма біткенше ағынды блоктайды және Future тізімін қайтарады
            List<Future<String>> futures = pool.invokeAll(tasks);

            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                // get() арқылы нәтижелерді жинаймыз (олар дайын болып тұр)
                results.add(future.get());
            }
            return results;
        } finally {
            // Пулды міндетті түрде жабамыз
            pool.shutdown();
        }
    }

    /**
     * Тізімнің қосындысын threadCount ағынмен есептейді.
     * Әр элементті бөлек Callable ретінде submit жасап, Future.get() арқылы жина.
     * Нәтиже реттік қосындымен бірдей болуы керек.
     */
    public static int sumConcurrently(List<Integer> values, int threadCount) throws Exception {
        if (values == null || values.isEmpty()) {
            return 0;
        }

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        try {
            // Әрбір санды бөлек Callable ретінде пулға жібереміз (submit)
            for (Integer value : values) {
                futures.add(pool.submit(() -> value));
            }

            // Нәтижелерді Future.get() арқылы жинап, қосамыз
            int sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get(); // get() ағынды блоктайды
            }
            return sum;
        } finally {
            pool.shutdown();
        }
    }

    /**
     * Пулға 30 мс "ойланатын" Callable береді де, нәтижесін күтіп қайтарады:
     * "Salem, <name>!". get() блоктайтынын өз көзіңмен көр.
     */
    public static String slowGreeting(String name) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = pool.submit(() -> {
                Thread.sleep(30); // 30 миллисекунд "ойлану"
                return "Salem, " + name + "!";
            });

            return future.get(); // Нәтиже дайын болғанша күтіп, блоктайды
        } finally {
            pool.shutdown();
        }
    }

    /**
     * CountDownLatch тәжірибесі: workers ағын іске қосады, әрқайсысы ортақ санағышты
     * арттырып, latch.countDown() жасайды. Негізгі ағын latch.await() арқылы
     * БӘРІН күтеді де, санағыштың мәнін қайтарады (әрқашан workers болуы керек).
     */
    public static int awaitWorkers(int workers) throws Exception {

        if (workers <= 0) {
            return 0;
        }

        ExecutorService pool = Executors.newFixedThreadPool(workers);
        CountDownLatch latch = new CountDownLatch(workers);
        // Ағындар арасында қауіпсіз (thread-safe) санағыш
        AtomicInteger counter = new AtomicInteger(0);

        try {
            for (int i = 0; i < workers; i++) {
                pool.submit(() -> {
                    try {
                        counter.incrementAndGet(); // Санағышты 1-ге арттыру
                    } finally {
                        latch.countDown(); // Негізгі ағынға "біттім" деп белгі беру
                    }
                });
            }

            // Негізгі ағын барлық worker біткенше (latch 0-ге жеткенше) күтеді
            latch.await();
            return counter.get();
        } finally {
            pool.shutdown();
        }
    }

    /**
     * 2 секунд орындалатын тапсырма береді де, future.get(100, MILLISECONDS) шақырады.
     * TimeoutException ұсталса true қайтар (әрі пулды shutdownNow жаса).
     * Мағынасы: Future.get()-тің уақыт шектеуі бар нұсқасы мәңгі тұрып қалудан сақтайды.
     */
    public static boolean timesOut() throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = pool.submit(() -> {
                Thread.sleep(2000); // 2 секундтық ұзақ тапсырма
                return "Done";
            });

            // Тек 100 миллисекунд қана күтеміз
            future.get(100, TimeUnit.MILLISECONDS);
            return false;
        } catch (TimeoutException e) {
            // Уақыт бітіп қалса, осы жерге түседі
            pool.shutdownNow(); // Орындалып жатқан тапсырманы күштеп тоқтатамыз
            return true;
        } finally {
            pool.shutdown(); // Кез келген жағдайда пулды жабамыз
        }
    }
}
