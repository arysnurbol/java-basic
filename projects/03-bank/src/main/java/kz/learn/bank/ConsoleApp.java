package kz.learn.bank;

import kz.learn.bank.event.LargeTransactionAlert;
import kz.learn.bank.exception.BankException;
import kz.learn.bank.fee.FeePolicy;
import kz.learn.bank.fee.FixedFee;
import kz.learn.bank.fee.PercentFee;
import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.repository.InMemoryRepository;
import kz.learn.bank.service.BankService;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Консольдік мәзір. ДАЙЫН — барлық тест жасыл болған соң іске қосып, ойнап көр.
 *
 * Wiring: нақты уақыт — Clock.systemDefaultZone() (тестте — TestClock).
 * Бақылаушылар: ірі операция туралы ескерту және әр транзакцияны экранға шығаратын лямбда.
 *
 * Тарифтер — Strategy-дің үш түрі:
 *   Тегін    — FeePolicy.none(), овердрафт жоқ
 *   Ағымдағы — FixedFee 100 ₸, овердрафт жоқ
 *   Кредиттік — PercentFee 1.5% (кемі 200 ₸), овердрафт 50 000 ₸
 */
public class ConsoleApp {

    private final BankService bank;
    private final Scanner in = new Scanner(System.in);

    public ConsoleApp(BankService bank) {
        this.bank = bank;
    }

    public static void main(String[] args) {
        BankService bank = new BankService(new InMemoryRepository<>(), Clock.systemDefaultZone());
        seed(bank);
        bank.subscribe(new LargeTransactionAlert(Money.of(500_000), msg -> System.out.println("  !!! " + msg)));
        bank.subscribe((account, tx) -> System.out.println("  -> " + account.getId() + " #" + tx.id() + " "
                + tx.type() + " " + tx.amount() + ", баланс " + tx.balanceAfter()));
        new ConsoleApp(bank).run();
    }

    private static void seed(BankService bank) {
        bank.openAccount("Aru", FeePolicy.none(), Money.ZERO);
        bank.openAccount("Daniyar", new FixedFee(Money.of(100)), Money.ZERO);
        bank.deposit("KZ0001", Money.of(250_000));
        bank.deposit("KZ0002", Money.of(80_000));
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
            } catch (BankException e) {
                System.out.println("Ереже бұзылды: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Қате дерек: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Банк ===");
        System.out.println(" 1. Шот ашу");
        System.out.println(" 2. Барлық шоттар");
        System.out.println(" 3. Салу");
        System.out.println(" 4. Шешу");
        System.out.println(" 5. Аудару");
        System.out.println(" 6. Шот тарихы");
        System.out.println(" 7. Көшірме (кезең бойынша)");
        System.out.println(" 8. Түрлер бойынша сомалар");
        System.out.println(" 9. Ең бай шоттар");
        System.out.println("10. Банктегі жалпы ақша");
        System.out.println(" 0. Шығу");
        System.out.print("> ");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> openAccount();
            case "2" -> print(bank.topByBalance(Integer.MAX_VALUE));
            case "3" -> bank.deposit(ask("Шот нөмірі"), askMoney());
            case "4" -> bank.withdraw(ask("Шот нөмірі"), askMoney());
            case "5" -> bank.transfer(ask("Қайдан"), ask("Қайда"), askMoney());
            case "6" -> print(bank.getAccount(ask("Шот нөмірі")).history());
            case "7" -> print(bank.statement(ask("Шот нөмірі"), askDate("Басы"), askDate("Соңы")));
            case "8" -> bank.totalsByType(ask("Шот нөмірі")).forEach((type, sum) -> System.out.println(type + ": " + sum));
            case "9" -> print(bank.topByBalance(askInt("Нешеуі")));
            case "10" -> System.out.println(bank.totalBalance());
            default -> System.out.println("Мұндай пункт жоқ");
        }
    }

    private void openAccount() {
        String owner = ask("Иесі");
        System.out.println("Тариф: 1 — Тегін, 2 — Ағымдағы (100 ₸), 3 — Кредиттік (1.5%, кемі 200 ₸, овердрафт 50 000 ₸)");
        Account account = switch (ask("Тариф").trim()) {
            case "1" -> bank.openAccount(owner, FeePolicy.none(), Money.ZERO);
            case "2" -> bank.openAccount(owner, new FixedFee(Money.of(100)), Money.ZERO);
            case "3" -> bank.openAccount(owner, new PercentFee(new BigDecimal("1.5"), Money.of(200)), Money.of(50_000));
            default -> throw new IllegalArgumentException("Тариф 1, 2 немесе 3");
        };
        System.out.println("Ашылды: " + account);
    }

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine().trim();
    }

    private Money askMoney() {
        return Money.of(ask("Сома (мысалы 1500.50)"));
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
            throw new IllegalArgumentException("Күн пішімі: 2026-10-05");
        }
    }

    private void print(List<?> rows) {
        if (rows.isEmpty()) {
            System.out.println("(бос)");
        }
        rows.forEach(System.out::println);
    }
}
