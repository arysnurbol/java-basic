package ch03.t15_synchronization;

/**
 * Тапсырма 15 — жарыс жағдайын (race condition) жасайтын көмекші.
 */
public class SyncLab {

    /**
     * threadCount ағын жасайды, әрқайсысы counter-ді incrementsPerThread рет арттырады.
     * Барлық ағынды күтіп (join), counter.get() мәнін қайтарады.
     *
     * Назар аудар: ағындарды БІРДЕН бәрін start жаса, сосын ғана join жаса.
     * Егер әр ағынды start-тан кейін бірден join жасасаң, олар кезекпен орындалып,
     * ешқандай жарыс болмайды — тәжірибенің мәні кетеді.
     */
    public static int race(Counter counter, int threadCount, int incrementsPerThread)
            throws InterruptedException {
        // Ағындарды сақтайтын массив жасаймыз
        Thread[] threads = new Thread[threadCount];

        // 1. Ағындарды құрамыз және олардың ішінде не істейтінін жазамыз
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        // 2. БАРЛЫҚ ағынды БІРДЕН іске қосамыз (start)
        for (Thread thread : threads) {
            thread.start();
        }

        // 3. БАРЛЫҚ ағын жұмысын аяқтағанша негізгі ағынды күттіреміз (join)
        for (Thread thread : threads) {
            thread.join();
        }

        // Санағыштың соңғы мәнін қайтарамыз
        return counter.get();
    }
}
