package ch03.t04_generic_interface;

import ch03.check.Check;

import java.util.List;
import java.util.Optional;

public class Task04Check {

    public static void run() {
        Check.task("Тапсырма 04 — Обобщенный интерфейс (Repository)");

        Repository<String, Integer> users = new InMemoryRepository<>();

        Check.eq("бос репозиторий -> count() == 0", 0, users.count());
        Check.eq("бос репозиторий -> findAll()", List.of(), users.findAll());
        Check.eq("табылмаған кілт -> Optional.empty()", Optional.empty(), users.findById(1));

        Check.eq("save() сақталған нәрсені қайтарады", "Aisha", users.save(1, "Aisha"));
        users.save(2, "Bolat");
        users.save(3, "Chingiz");

        Check.eq("count() == 3", 3, users.count());
        Check.eq("findById(2)", Optional.of("Bolat"), users.findById(2));
        Check.eq("findAll() қосылу ретін сақтайды (LinkedHashMap)",
                List.of("Aisha", "Bolat", "Chingiz"), users.findAll());

        users.save(2, "Bolat Jr.");
        Check.eq("сол кілтке save() -> үстінен жазады", 3, users.count());
        Check.eq("жаңартылған мән", Optional.of("Bolat Jr."), users.findById(2));
        Check.eq("жаңарту орынды өзгертпейді",
                List.of("Aisha", "Bolat Jr.", "Chingiz"), users.findAll());

        Check.isTrue("deleteById(1) -> true", users.deleteById(1));
        Check.isFalse("deleteById(1) екінші рет -> false", users.deleteById(1));
        Check.eq("өшіргеннен кейінгі count()", 2, users.count());

        // Басқа типтермен де сол класс жұмыс істейді
        Repository<Integer, String> scores = new InMemoryRepository<>();
        scores.save("math", 95);
        Check.eq("Repository<Integer, String> де жұмыс істейді", Optional.of(95), scores.findById("math"));

        // Типтерді өшіру: екі бөлек параметрленуі — орындалуда бір класс
        Check.same("InMemoryRepository<String,Integer> мен <Integer,String> — бір класс (стирание типов)",
                users.getClass(), scores.getClass());
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
