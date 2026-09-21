package ch03.t15_synchronization;

/**
 * Тапсырма 15 — AtomicInteger арқылы санағыш.
 * Кітап: "Утилиты высокоуровневого параллелизма" (141 б.)
 *
 * AtomicInteger құлып қолданбайды — процессордың CAS (compare-and-swap)
 * нұсқауына сүйенеді. Сондықтан жай санағыш үшін synchronized-тен ЖЫЛДАМЫРАҚ.
 *
 * Ереже: жай санағыш/жалауша керек болса — Atomic*.
 *        Бірнеше өрісті бірге өзгерту керек болса — synchronized.
 */
public class AtomicCounter implements Counter {

    // TODO: private final AtomicInteger count = new AtomicInteger();

    @Override
    public void increment() {
        // TODO: incrementAndGet()
    }

    @Override
    public int get() {
        return 0; // TODO
    }
}
