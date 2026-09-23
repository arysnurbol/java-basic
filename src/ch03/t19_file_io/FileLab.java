package ch03.t19_file_io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // try-with-resources автоматты түрде ресурсты жабады
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Жолдар бірігіп кетпеуі үшін келесі жолға көшіру
            }
        }
    }

    /** Файлдағы барлық жолды оқиды. Кеңес: Files.readAllLines. */
    public static List<String> readLines(Path file) throws IOException {
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }

    /**
     * Файлдың БҮКІЛ мазмұнын java.io.BufferedReader арқылы оқиды,
     * жолдарды "\n" арқылы біріктіріп қайтарады (соңында "\n" болмасын).
     * Files.readString қолданба — мұнда ескі стильді қолмен жазып көру керек.
     * Кеңес: Files.newBufferedReader(file) + try-with-resources + readLine() циклі.
     */
    public static String readWithBufferedReader(Path file) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Ескі стильде BufferedReader қолдану және try-with-resources арқылы жабу
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (!isFirst) {
                    sb.append("\n"); // Жолдардың арасына ғана "\n" қосу (соңында қалмайды)
                }
                sb.append(line);
                isFirst = false;
            }
        }
        return sb.toString();
    }

    /**
     * Файлдың СОҢЫНА бір жол қосады (бұрынғысын өшірмейді).
     * Кеңес: Files.writeString(..., StandardOpenOption.CREATE, StandardOpenOption.APPEND).
     */
    public static void appendLine(Path file, String line) throws IOException {
        // CREATE — файл жоқ болса құрады, APPEND — соңына жалғайды
        Files.writeString(
                file,
                line + "\n",
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    /**
     * Жолдардың саны. Кеңес: Files.lines() — бұл Stream, сондықтан
     * try-with-resources ІШІНДЕ қолдану МІНДЕТТІ, әйтпесе файл ашық қалады.
     */
    public static long countLines(Path file) throws IOException {
        // Файл ашық қалмауы үшін Stream-ді try-мен ораймыз
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            return lines.count();
        }
    }

    /**
     * Каталогтағы берілген кеңейтіммен аяқталатын файлдардың аттары, СҰРЫПТАЛҒАН.
     * Ішкі каталогтарға кірмейді. Кеңес: Files.list() — бұл да жабылатын Stream.
     * extension — нүктесімен беріледі: ".txt".
     */
    public static List<String> listFileNames(Path dir, String extension) throws IOException {
        // Files.list() каталогы ашық қалмау үшін try-with-resources ішінде өңделеді
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile) // Тек файлдарды аламыз (папкаларды емес)
                    .map(path -> path.getFileName().toString()) // Тек атын аламыз
                    .filter(name -> name.endsWith(extension)) // Кеңейтім бойынша сүзгілеу
                    .sorted() // Сұрыптау
                    .collect(Collectors.toList());
        }
    }

    /**
     * src-ті dst-ке көшіреді, БІРАҚ dst бұрыннан бар болса — тимейді де, false қайтарады.
     * Көшірілсе true.
     */
    public static boolean copyIfAbsent(Path src, Path dst) throws IOException {
        // Егер мақсатты файл бұрыннан бар болса, тимейміз
        if (Files.exists(dst)) {
            return false;
        }
        // Көшіру
        Files.copy(src, dst);
        return true;
    }

    /** Файлдың байтпен өлшемі. Кеңес: Files.size. */
    public static long sizeOf(Path file) throws IOException {
        return  Files.size(file);
    }
}
