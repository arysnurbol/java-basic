package kz.learn.todo;

import kz.learn.todo.model.Priority;
import kz.learn.todo.model.Status;
import kz.learn.todo.model.Task;
import kz.learn.todo.repository.InMemoryTaskRepository;
import kz.learn.todo.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Консольдік мәзір. ДАЙЫН — барлық тест жасыл болған соң іске қосып, ойнап көр.
 *
 * Назар аудар: объектілерді "жинау" (wiring) тек осында —
 * new InMemoryTaskRepository() -> new TaskService(repo). Spring-те мұны контейнер істейді.
 */
public class ConsoleApp {

    private final TaskService service;
    private final Scanner in = new Scanner(System.in);

    public ConsoleApp(TaskService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        new ConsoleApp(new TaskService(new InMemoryTaskRepository())).run();
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
            } catch (RuntimeException e) {
                System.out.println("Қате: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== ToDo ===");
        System.out.println("1. Қарапайым тапсырма қосу");
        System.out.println("2. Мерзімі бар тапсырма қосу");
        System.out.println("3. Барлығын көрсету");
        System.out.println("4. Бастау (IN_PROGRESS)");
        System.out.println("5. Аяқтау (DONE)");
        System.out.println("6. Өшіру");
        System.out.println("7. Күйі бойынша сүзу");
        System.out.println("8. Кешіккендер");
        System.out.println("9. Маңыздылығы бойынша сұрыптау");
        System.out.println("0. Шығу");
        System.out.print("> ");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> print(List.of(service.addSimple(ask("Атауы"), askPriority())));
            case "2" -> print(List.of(service.addWithDeadline(ask("Атауы"), askPriority(), askDate())));
            case "3" -> print(service.getAll());
            case "4" -> service.start(askId());
            case "5" -> service.complete(askId());
            case "6" -> service.delete(askId());
            case "7" -> print(service.findByStatus(Status.valueOf(ask("Күйі (TODO/IN_PROGRESS/DONE)").toUpperCase())));
            case "8" -> print(service.findOverdue(LocalDate.now()));
            case "9" -> print(service.sortedByPriority());
            default -> System.out.println("Мұндай пункт жоқ");
        }
    }

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine();
    }

    private long askId() {
        try {
            return Long.parseLong(ask("id").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id сан болуы керек");
        }
    }

    private Priority askPriority() {
        return Priority.valueOf(ask("Маңыздылығы (LOW/MEDIUM/HIGH)").trim().toUpperCase());
    }

    private LocalDate askDate() {
        try {
            return LocalDate.parse(ask("Мерзімі (yyyy-MM-dd)").trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Күн пішімі: 2026-10-05");
        }
    }

    private void print(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("(бос)");
        }
        tasks.forEach(System.out::println);
    }
}
