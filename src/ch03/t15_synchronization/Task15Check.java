package ch03.t15_synchronization;

import ch03.check.Check;

public class Task15Check {

    private static final int THREADS = 8;
    private static final int PER_THREAD = 50_000;
    private static final int EXPECTED = THREADS * PER_THREAD;

    public static void run() throws Exception {
        Check.task("Тапсырма 15 — Синхронизация және race condition");

        // Бір ағында үшеуі де бірдей жұмыс істейді
        Check.eq("UnsafeCounter бір ағында дұрыс", 1000, SyncLab.race(new UnsafeCounter(), 1, 1000));
        Check.eq("SyncCounter бір ағында дұрыс", 1000, SyncLab.race(new SyncCounter(), 1, 1000));
        Check.eq("AtomicCounter бір ағында дұрыс", 1000, SyncLab.race(new AtomicCounter(), 1, 1000));

        // Көп ағында тек қорғалғандары дұрыс
        Check.eq("SyncCounter " + THREADS + " ағында ДӘЛ " + EXPECTED,
                EXPECTED, SyncLab.race(new SyncCounter(), THREADS, PER_THREAD));
        Check.eq("AtomicCounter " + THREADS + " ағында ДӘЛ " + EXPECTED,
                EXPECTED, SyncLab.race(new AtomicCounter(), THREADS, PER_THREAD));

        int unsafe = SyncLab.race(new UnsafeCounter(), THREADS, PER_THREAD);
        Check.isTrue("UnsafeCounter нәтижесі күтілгеннен АСПАЙДЫ (артық санамайды)", unsafe <= EXPECTED);
        Check.info("UnsafeCounter берген мән: " + unsafe + " (күтілген " + EXPECTED + ")");
        if (unsafe == EXPECTED) {
            Check.info("Бұл жолы жоғалту болмады — race condition ӘРҚАШАН көрінбейді.");
            Check.info("Қайта жүгіртіп көр: дәл сол код басқа нәтиже беруі мүмкін.");
        } else {
            Check.info("Дәл осы — " + (EXPECTED - unsafe) + " арттыру ЖОҒАЛДЫ. Себебі: count++ атомарлы емес.");
        }

        // Әр іске қосуда жаңа объект — күйі бұрынғыдан қалмауы керек
        Check.eq("жаңа SyncCounter нөлден басталады", 0, new SyncCounter().get());
        Check.eq("жаңа AtomicCounter нөлден басталады", 0, new AtomicCounter().get());
        Check.eq("ағынсыз race -> 0", 0, SyncLab.race(new AtomicCounter(), 0, 100));
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
