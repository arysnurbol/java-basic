package ch03.t15_synchronization;

/**
 * Тапсырма 15 — ҚАУІПСІЗ ЕМЕС санағыш. ӘДЕЙІ ДҰРЫС ЕМЕС.
 *
 * Мұнда қарапайым `count++` жаз — ешқандай synchronized ҚОСПА.
 *
 * Неге бұл бұзылады: count++ — бір әрекет емес, ҮШЕУІ:
 *     1) count-ты оқу   2) бірді қосу   3) кері жазу
 * Екі ағын 1-қадамды бір мезгілде жасаса, екеуі де бірдей ескі мәнді оқиды
 * да, екі арттыру бір ғана арттыруға айналады. Бұл — race condition.
 */
public class UnsafeCounter implements Counter {

    // TODO: private int count;
    private int count = 0;

    @Override
    public void increment() {
        // TODO: count++  (synchronized ҚОСПА!)
        count++;
    }

    @Override
    public int get() {
        return count;
    }
}
