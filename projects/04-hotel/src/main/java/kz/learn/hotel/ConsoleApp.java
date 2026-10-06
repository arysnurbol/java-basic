package kz.learn.hotel;

import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.model.Booking;
import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.repository.InMemoryRepository;
import kz.learn.hotel.room.Room;
import kz.learn.hotel.room.RoomType;
import kz.learn.hotel.service.HotelService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Консольдік мәзір. ДАЙЫН — барлық тест жасыл болған соң іске қосып, ойнап көр.
 *
 * Wiring: нақты уақыт — Clock.systemDefaultZone() (тестте — TestClock).
 * Бөлмелер тек RoomType арқылы қосылады — ConsoleApp StandardRoom/DeluxeRoom/Suite-ты білмейді.
 */
public class ConsoleApp {

    private final HotelService hotel;
    private final Scanner in = new Scanner(System.in);

    public ConsoleApp(HotelService hotel) {
        this.hotel = hotel;
    }

    public static void main(String[] args) {
        HotelService hotel = new HotelService(new InMemoryRepository<>(), new InMemoryRepository<>(),
                Clock.systemDefaultZone());
        seed(hotel);
        new ConsoleApp(hotel).run();
    }

    private static void seed(HotelService hotel) {
        hotel.addRoom(RoomType.STANDARD, "101");
        hotel.addRoom(RoomType.STANDARD, "102");
        hotel.addRoom(RoomType.DELUXE, "201");
        hotel.addRoom(RoomType.SUITE, "301");
        LocalDate today = LocalDate.now();
        hotel.book("101", "Aru", 2, DateRange.of(today.plusDays(1), 3));
        hotel.book("201", "Daniyar", 3, DateRange.of(today.plusDays(2), 2));
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
            } catch (HotelException e) {
                System.out.println("Ереже бұзылды: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Қате дерек: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Қонақүй ===");
        System.out.println(" 1. Бос бөлмелерді іздеу");
        System.out.println(" 2. Брондау");
        System.out.println(" 3. Броньды болдырмау");
        System.out.println(" 4. Қонақтың брондары");
        System.out.println(" 5. Түнгі толу пайызы");
        System.out.println(" 6. Түрлер бойынша табыс");
        System.out.println(" 7. Осы күні келетіндер");
        System.out.println(" 8. Бөлме қосу");
        System.out.println(" 0. Шығу");
        System.out.print("> ");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> findRooms();
            case "2" -> System.out.println("Брондалды: " + hotel.book(ask("Бөлме"), ask("Қонақ"),
                    askInt("Қонақ саны"), askStay()));
            case "3" -> System.out.println("Қайтарылады: " + hotel.cancel(ask("Бронь нөмірі")));
            case "4" -> print(hotel.bookingsOf(ask("Қонақ")));
            case "5" -> System.out.println(hotel.occupancyPercent(askDate("Түн")) + "%");
            case "6" -> hotel.revenueByType().forEach((type, sum) -> System.out.println(type + ": " + sum));
            case "7" -> print(hotel.checkInsOn(askDate("Күн")));
            case "8" -> addRoom();
            default -> System.out.println("Мұндай пункт жоқ");
        }
    }

    private void findRooms() {
        DateRange stay = askStay();
        List<Room> found = hotel.findAvailable(stay, askInt("Қонақ саны"));
        if (found.isEmpty()) {
            System.out.println("(бос бөлме жоқ)");
        }
        found.forEach(room -> System.out.println(room + " — " + stay.nights() + " түн: " + room.priceFor(stay)));
    }

    private void addRoom() {
        System.out.println("Түрі: STANDARD, DELUXE, SUITE");
        RoomType type;
        try {
            type = RoomType.valueOf(ask("Түрі").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Түрі: STANDARD, DELUXE немесе SUITE");
        }
        System.out.println("Қосылды: " + hotel.addRoom(type, ask("Нөмірі")));
    }

    private DateRange askStay() {
        return DateRange.of(askDate("Келу күні"), askInt("Түн саны"));
    }

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine().trim();
    }

    private int askInt(String label) {
        try {
            return Integer.parseInt(ask(label));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " сан болуы керек");
        }
    }

    private LocalDate askDate(String label) {
        String text = ask(label + " (yyyy-MM-dd, бос = бүгін)");
        if (text.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Күн пішімі: 2026-10-10");
        }
    }

    private void print(List<Booking> rows) {
        if (rows.isEmpty()) {
            System.out.println("(бос)");
        }
        rows.forEach(System.out::println);
    }
}
