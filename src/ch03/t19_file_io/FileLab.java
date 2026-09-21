package ch03.t19_file_io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Тапсырма 19 — Кеңейтілген енгізу-шығару.
 * Кітап: "Обзор потоков ввода-вывода Java", "Ввод-вывод файлов с помощью java.io",
 *        "Работа с java.nio (New I/O)" (149–154 б.)
 *
 * ЕКІ БУЫН:
 *   java.io   (ескі)  File, FileReader, BufferedReader — ағындық, көп бойлер-код.
 *   java.nio.file (жаңа, Java 7+)  Path, Files — бір жолдық әдістер, жақсы қателер.
 *
 * ЖАҢА КОДТА ӘРҚАШАН java.nio.file-ді таңда:
 *   Files.writeString(path, text)      Files.readString(path)
 *   Files.write(path, lines)           Files.readAllLines(path)
 *   Files.exists / size / copy / delete / createDirectories
 *   Files.lines(path)                  <- Stream<String>, ЖАБУ КЕРЕК!
 *
 * ЕҢ БАСТЫ ЕРЕЖЕ: файл дескрипторын ашсаң, оны ЖАБУ керек.
 * Сондықтан BufferedReader-ді де, Files.lines()-ті де try-with-resources ішінде аш:
 *
 *     try (var reader = Files.newBufferedReader(path)) { ... }   // өздігінен жабылады
 *
 * Кодировка: java.nio әдістері әдепкіде UTF-8 қолданады — қазақша мәтін дұрыс сақталады.
 */
public class FileLab {

    /** Жолдарды файлға жазады (файл бар болса — үстінен жазады). */
    public static void writeLines(Path file, List<String> lines) throws IOException {
        // TODO
    }

    /** Файлдағы барлық жолды оқиды. Кеңес: Files.readAllLines. */
    public static List<String> readLines(Path file) throws IOException {
        return null; // TODO
    }

    /**
     * Файлдың БҮКІЛ мазмұнын java.io.BufferedReader арқылы оқиды,
     * жолдарды "\n" арқылы біріктіріп қайтарады (соңында "\n" болмасын).
     * Files.readString қолданба — мұнда ескі стильді қолмен жазып көру керек.
     * Кеңес: Files.newBufferedReader(file) + try-with-resources + readLine() циклі.
     */
    public static String readWithBufferedReader(Path file) throws IOException {
        return null; // TODO
    }

    /**
     * Файлдың СОҢЫНА бір жол қосады (бұрынғысын өшірмейді).
     * Кеңес: Files.writeString(..., StandardOpenOption.CREATE, StandardOpenOption.APPEND).
     */
    public static void appendLine(Path file, String line) throws IOException {
        // TODO
    }

    /**
     * Жолдардың саны. Кеңес: Files.lines() — бұл Stream, сондықтан
     * try-with-resources ІШІНДЕ қолдану МІНДЕТТІ, әйтпесе файл ашық қалады.
     */
    public static long countLines(Path file) throws IOException {
        return 0; // TODO
    }

    /**
     * Каталогтағы берілген кеңейтіммен аяқталатын файлдардың аттары, СҰРЫПТАЛҒАН.
     * Ішкі каталогтарға кірмейді. Кеңес: Files.list() — бұл да жабылатын Stream.
     * extension — нүктесімен беріледі: ".txt".
     */
    public static List<String> listFileNames(Path dir, String extension) throws IOException {
        return null; // TODO
    }

    /**
     * src-ті dst-ке көшіреді, БІРАҚ dst бұрыннан бар болса — тимейді де, false қайтарады.
     * Көшірілсе true.
     */
    public static boolean copyIfAbsent(Path src, Path dst) throws IOException {
        return false; // TODO
    }

    /** Файлдың байтпен өлшемі. Кеңес: Files.size. */
    public static long sizeOf(Path file) throws IOException {
        return 0; // TODO
    }
}
