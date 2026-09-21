package ch03.t19_file_io;

import ch03.check.Check;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class Task19Check {

    public static void run() throws Exception {
        Check.task("Тапсырма 19 — Файлдармен жұмыс (FileLab)");

        Path dir = Files.createTempDirectory("ch03-io-");
        try {
            Path file = dir.resolve("notes.txt");

            FileLab.writeLines(file, List.of("bir", "eki", "сәлем"));
            Check.isTrue("writeLines() файл жасады", Files.exists(file));
            Check.eq("readLines()", List.of("bir", "eki", "сәлем"), FileLab.readLines(file));
            Check.eq("readWithBufferedReader()", "bir\neki\nсәлем", FileLab.readWithBufferedReader(file));
            Check.eq("countLines()", 3L, FileLab.countLines(file));

            FileLab.writeLines(file, List.of("qayta"));
            Check.eq("writeLines() үстінен жазады", List.of("qayta"), FileLab.readLines(file));

            FileLab.appendLine(file, "jalganghan");
            Check.eq("appendLine() соңына қосады", List.of("qayta", "jalganghan"), FileLab.readLines(file));
            Check.eq("appendLine() кейін жол саны", 2L, FileLab.countLines(file));

            Path fresh = dir.resolve("fresh.txt");
            FileLab.appendLine(fresh, "birinshi");
            Check.eq("appendLine() жоқ файлды ЖАСАЙДЫ", List.of("birinshi"), FileLab.readLines(fresh));

            Check.eq("sizeOf() — \"abc\" үш байт", 3L,
                    sizeAfterWrite(dir.resolve("size.txt"), "abc"));

            Path empty = dir.resolve("empty.txt");
            FileLab.writeLines(empty, List.of());
            Check.eq("бос файлда countLines() == 0", 0L, FileLab.countLines(empty));
            Check.eq("бос файлда readWithBufferedReader()", "", FileLab.readWithBufferedReader(empty));

            Files.createFile(dir.resolve("a.log"));
            Files.createDirectory(dir.resolve("subdir"));
            Check.eq("listFileNames(\".txt\")",
                    List.of("empty.txt", "fresh.txt", "notes.txt", "size.txt"),
                    FileLab.listFileNames(dir, ".txt"));
            Check.eq("listFileNames(\".log\")", List.of("a.log"), FileLab.listFileNames(dir, ".log"));
            Check.eq("listFileNames(\".md\") — сәйкес жоқ", List.of(), FileLab.listFileNames(dir, ".md"));

            Path copy = dir.resolve("copy.txt");
            Check.isTrue("copyIfAbsent() — жаңа файлға көшіреді", FileLab.copyIfAbsent(file, copy));
            Check.eq("көшірменің мазмұны", FileLab.readLines(file), FileLab.readLines(copy));
            Check.isFalse("copyIfAbsent() — бар файлға тимейді", FileLab.copyIfAbsent(fresh, copy));
            Check.eq("көшірме ӨЗГЕРМЕДІ", List.of("qayta", "jalganghan"), FileLab.readLines(copy));

            Check.throwsEx("жоқ файлды оқу -> NoSuchFileException",
                    NoSuchFileException.class, () -> FileLab.readLines(dir.resolve("joq.txt")));
        } finally {
            deleteRecursively(dir);
        }

        Check.isFalse("уақытша каталог тазаланды", Files.exists(dir));
    }

    private static long sizeAfterWrite(Path file, String content) throws IOException {
        Files.writeString(file, content);
        return FileLab.sizeOf(file);
    }

    private static void deleteRecursively(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        try (var paths = Files.walk(root)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
