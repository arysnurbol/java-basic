package ch03.t13_streams_advanced;

import java.util.List;
import java.util.Optional;

/**
 * Тапсырма 13 — Примитивті ағындар, flatMap, жалқаулық және параллель ағындар.
 * Кітап: "Создание потоков", "Параллельные потоки", "Рекомендации по использованию потоков"
 *        (132, 136 б.)
 *
 * ПРИМИТИВТІ АҒЫНДАР (IntStream, LongStream, DoubleStream):
 *   Stream<Integer>-де әр элемент қорапталады (boxing) — бұл жады мен уақыт шығыны.
 *   IntStream оны болдырмайды әрі sum(), average(), max() дайын береді.
 *   Өтулері: list.stream().mapToInt(...) / intStream.boxed()
 *
 * ЖАЛҚАУЛЫҚ (ленивость): аралық операциялар терминалдық операция келгенше
 * МҮЛДЕ орындалмайды. Әрі элементтер бірінен соң бірі толық құбырдан өтеді
 * (барлық элемент filter-ден өтіп, сосын map-қа барады ДЕГЕН ЕМЕС).
 * Осының арқасында findFirst/anyMatch тапқан бойда тоқтайды (short-circuit).
 *
 * ПАРАЛЛЕЛЬ: parallelStream() жұмысты бірнеше ядроға бөледі. Тек ассоциативті
 * операцияларда дұрыс (қосу — иә, азайту — жоқ). Кіші тізімде тек баяулатады.
 */
public class StreamAdvanced {

    /** from-дан to-ға дейінгі (екеуін қоса) сандардың қосындысы. Кеңес: IntStream.rangeClosed. */
    public static int sumRange(int from, int toInclusive) {
        return 0; // TODO
    }

    /** Орташа мән. Сан берілмесе -> 0.0. Кеңес: IntStream.of(...).average().orElse(0). */
    public static double average(int... values) {
        return 0; // TODO
    }

    /** Тізімдер тізімін бір тізімге жаяды. Кеңес: flatMap(List::stream). */
    public static List<String> flatten(List<List<String>> nested) {
        return null; // TODO
    }

    /**
     * Барлық сөйлемді сөздерге бөліп, бір тізімге жинайды.
     * "java go", "rust" -> ["java", "go", "rust"]. Бөлгіш — бір бос орын.
     */
    public static List<String> allWords(List<String> sentences) {
        return null; // TODO
    }

    /** 1, 2, 4, 8, ... — алғашқы n дәреже. Кеңес: Stream.iterate + limit. */
    public static List<Integer> firstPowersOfTwo(int n) {
        return null; // TODO
    }

    /** Тізімді int[] массивіне. Кеңес: mapToInt(Integer::intValue).toArray(). */
    public static int[] toIntArray(List<Integer> values) {
        return null; // TODO
    }

    /** Параллель ағынмен қосынды. Нәтиже реттік ағынмен БІРДЕЙ болуы керек. */
    public static long parallelSum(List<Integer> values) {
        return 0; // TODO
    }

    /**
     * ЖАЛҚАУЛЫҚ ТӘЖІРИБЕСІ №1.
     * Ағын құр: items.stream().peek(log::add).filter(...)  — бірақ терминалдық
     * операцияны ШАҚЫРМА (нәтижені қайтарудың да қажеті жоқ).
     * Дұрыс жазсаң log БОС қалады: аралық операциялар ештеңе істемеген.
     */
    public static void lazyNoTerminal(List<String> items, List<String> log) {
        // TODO
    }

    /**
     * ЖАЛҚАУЛЫҚ ТӘЖІРИБЕСІ №2 (short-circuit).
     * Сол құбыр, бірақ соңында findFirst(): ұзындығы 3-тен кем емес БІРІНШІ элемент.
     * peek(log::add) арқылы ҚАРАЛҒАН элементтерді log-қа жаз.
     * Нәтиже табылған соң ағын тоқтайды — сондықтан log-та бүкіл тізім болмайды.
     */
    public static Optional<String> firstLongEnough(List<String> items, List<String> log) {
        return null; // TODO
    }
}
