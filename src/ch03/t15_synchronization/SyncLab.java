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
        return 0; // TODO
    }
}
