package kz.learn.library;

import kz.learn.library.exception.LibraryException;
import kz.learn.library.model.Loan;
import kz.learn.library.model.Member;
import kz.learn.library.repository.InMemoryRepository;
import kz.learn.library.service.LibraryService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Консольдік мәзір. ДАЙЫН — барлық тест жасыл болған соң іске қосып, ойнап көр.
 *
 * Wiring: бір generic класс — үш түрлі репозиторий. Diamond (<>) типтерді конструктордан шығарады.
 * Іске қосқанда бірнеше демо-дерек қосылады (seed).
 *
 * Күн сұралғанда бос қалдырсаң — бүгінгі күн. Кешігуді байқау үшін болашақ күнді енгіз.
 */
public class ConsoleApp {

    private final LibraryService service;
    private final Scanner in = new Scanner(System.in);

    public ConsoleApp(LibraryService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        LibraryService service = new LibraryService(
                new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>());
        seed(service);
        new ConsoleApp(service).run();
    }

    private static void seed(LibraryService service) {
        service.addBook("Clean Code", "Robert Martin");
        service.addBook("Effective Java", "Joshua Bloch");
        service.addMagazine("National Geographic", 12);
        service.addDvd("Interstellar", 169);
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");
    }

    private void run() {
        while (true) {
            printMenu();
            String choice = in.nextLine().trim();
            if (choice.equals("0")) {
                System.out.println("Сау бол!");
                return;
            }
            try {
                handle(choice);
            } catch (LibraryException e) {
                System.out.println("Ереже бұзылды: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Қате дерек: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Кітапхана ===");
        System.out.println(" 1. Кітап қосу");
        System.out.println(" 2. Журнал қосу");
        System.out.println(" 3. DVD қосу");
        System.out.println(" 4. Студентті тіркеу");
        System.out.println(" 5. Кәдімгі оқырманды тіркеу");
        System.out.println(" 6. Бос экземплярлар");
        System.out.println(" 7. Іздеу (атауы бойынша)");
        System.out.println(" 8. Беру");
        System.out.println(" 9. Қайтару");
        System.out.println("10. Оқырманның қолындағылар");
        System.out.println("11. Кешіккендер");
        System.out.println("12. Айыппұлдар");
        System.out.println("13. Ең танымалдар");
        System.out.println(" 0. Шығу");
        System.out.print("> ");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> System.out.println(service.addBook(ask("Атауы"), ask("Авторы")));
            case "2" -> System.out.println(service.addMagazine(ask("Атауы"), askInt("Нөмірі")));
            case "3" -> System.out.println(service.addDvd(ask("Атауы"), askInt("Ұзақтығы (мин)")));
            case "4" -> System.out.println(service.registerStudent(ask("Билет нөмірі"), ask("Аты")));
            case "5" -> System.out.println(service.registerRegular(ask("Билет нөмірі"), ask("Аты")));
            case "6" -> print(service.availableItems());
            case "7" -> print(service.search(ask("Іздеу")));
            case "8" -> System.out.println(service.lend(askInt("Экземпляр id"), ask("Билет нөмірі"), askDate()));
            case "9" -> System.out.println("Айыппұл: " + service.returnItem(askInt("Экземпляр id"), askDate()) + " ₸");
            case "10" -> print(service.activeLoans(ask("Билет нөмірі")));
            case "11" -> printOverdue(askDate());
            case "12" -> printFines(service.finesByMember(askDate()));
            case "13" -> print(service.mostPopular(askInt("Нешеуі")));
            default -> System.out.println("Мұндай пункт жоқ");
        }
    }

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine();
    }

    private int askInt(String label) {
        try {
            return Integer.parseInt(ask(label).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " сан болуы керек");
        }
    }

    private LocalDate askDate() {
        String text = ask("Күн (yyyy-MM-dd, бос = бүгін)").trim();
        if (text.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Күн пішімі: 2026-10-05");
        }
    }

    private void printOverdue(LocalDate today) {
        List<Loan> overdue = service.overdueLoans(today);
        if (overdue.isEmpty()) {
            System.out.println("(бос)");
        }
        for (Loan loan : overdue) {
            System.out.println(loan + " | " + loan.overdueDays(today) + " күн, " + loan.fine(today) + " ₸");
        }
    }

    private void printFines(Map<String, Long> fines) {
        if (fines.isEmpty()) {
            System.out.println("(бос)");
        }
        fines.forEach((card, fine) -> {
            Member member = service.getMember(card);
            System.out.println(member + ": " + fine + " ₸");
        });
    }

    private void print(List<?> rows) {
        if (rows.isEmpty()) {
            System.out.println("(бос)");
        }
        rows.forEach(System.out::println);
    }
}
