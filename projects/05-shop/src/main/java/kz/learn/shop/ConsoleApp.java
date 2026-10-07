package kz.learn.shop;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.exception.ShopException;
import kz.learn.shop.model.Category;
import kz.learn.shop.model.Order;
import kz.learn.shop.repository.InMemoryRepository;
import kz.learn.shop.service.ShopService;

import java.util.List;
import java.util.Scanner;

/**
 * Консольдік мәзір. ДАЙЫН — барлық тест жасыл болған соң іске қосып, ойнап көр.
 *
 * ConsoleApp NewState/PaidState-ті де, PercentDiscount/FixedDiscount-ты да білмейді:
 * күйді тапсырыс өзі ауыстырады, ал жеңілдікті промокод арқылы фабрика жасайды.
 */
public class ConsoleApp {

    private final ShopService shop;
    private final Scanner in = new Scanner(System.in);

    public ConsoleApp(ShopService shop) {
        this.shop = shop;
    }

    public static void main(String[] args) {
        ShopService shop = new ShopService(new InMemoryRepository<>(), new InMemoryRepository<>());
        seed(shop);
        new ConsoleApp(shop).run();
    }

    private static void seed(ShopService shop) {
        shop.addProduct("P1", "Ноутбук", Category.ELECTRONICS, 350_000);
        shop.addProduct("P2", "Құлаққап", Category.ELECTRONICS, 12_000);
        shop.addProduct("B1", "Абай жолы", Category.BOOKS, 6_500);
        shop.addProduct("C1", "Футболка", Category.CLOTHING, 4_000);
        shop.addProduct("F1", "Құрт", Category.FOOD, 1_200);
        shop.restock("P1", 2);
        shop.restock("P2", 10);
        shop.restock("B1", 5);
        shop.restock("C1", 20);
        shop.restock("F1", 50);

        Order aru = shop.createOrder("Aru", DeliveryType.COURIER);
        shop.addItem(aru.getId(), "B1", 2);
        shop.addItem(aru.getId(), "F1", 3);
        shop.pay(aru.getId());
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
            } catch (ShopException e) {
                System.out.println("Ереже бұзылды: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Қате дерек: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Интернет-дүкен ===");
        System.out.println(" 1. Каталог және қалдық");
        System.out.println(" 2. Жаңа тапсырыс");
        System.out.println(" 3. Тауар қосу");
        System.out.println(" 4. Тауарды өшіру");
        System.out.println(" 5. Промокод");
        System.out.println(" 6. Тапсырысты көру");
        System.out.println(" 7. Төлеу");
        System.out.println(" 8. Жіберу");
        System.out.println(" 9. Жеткізілді");
        System.out.println("10. Болдырмау");
        System.out.println("11. Клиенттің тапсырыстары");
        System.out.println("12. Есептер");
        System.out.println("13. Қоймаға түсіру");
        System.out.println(" 0. Шығу");
        System.out.print("> ");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> shop.catalog().forEach(p -> System.out.println(p + " — қоймада " + shop.stockOf(p.id())));
            case "2" -> System.out.println("Жасалды: " + shop.createOrder(ask("Клиент"), askDelivery()));
            case "3" -> show(shop.addItem(ask("Тапсырыс"), ask("Артикул"), askInt("Саны")));
            case "4" -> show(shop.removeItem(ask("Тапсырыс"), ask("Артикул")));
            case "5" -> show(shop.applyPromo(ask("Тапсырыс"), ask("Промокод (SALE10, MINUS5000, бос = жоқ)")));
            case "6" -> show(shop.getOrder(ask("Тапсырыс")));
            case "7" -> System.out.println("Төленді: " + shop.pay(ask("Тапсырыс")));
            case "8" -> shop.ship(ask("Тапсырыс"));
            case "9" -> shop.deliver(ask("Тапсырыс"));
            case "10" -> System.out.println("Қайтарылады: " + shop.cancel(ask("Тапсырыс")));
            case "11" -> print(shop.ordersOf(ask("Клиент")));
            case "12" -> reports();
            case "13" -> shop.restock(ask("Артикул"), askInt("Саны"));
            default -> System.out.println("Мұндай пункт жоқ");
        }
    }

    private void show(Order order) {
        System.out.println(order);
        order.getLines().forEach(line -> System.out.println("  " + line));
        System.out.println("  Тауарлар:  " + order.subtotal());
        System.out.println("  Жеңілдік: -" + order.discount());
        System.out.println("  Жеткізу:   " + order.deliveryCost());
        System.out.println("  Барлығы:   " + order.total());
    }

    private void reports() {
        System.out.println("Күйлер: " + shop.countByStatus());
        System.out.println("Табыс: " + shop.revenue());
        System.out.println("Санаттар бойынша:");
        shop.salesByCategory().forEach((category, sum) -> System.out.println("  " + category + ": " + sum));
        System.out.println("Ең көп сатылған 3 тауар:");
        shop.topProducts(3).forEach(p -> System.out.println("  " + p));
    }

    private DeliveryType askDelivery() {
        System.out.println("Жеткізу: PICKUP, COURIER, POST");
        try {
            return DeliveryType.valueOf(ask("Жеткізу").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Жеткізу: PICKUP, COURIER немесе POST");
        }
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

    private void print(List<Order> rows) {
        if (rows.isEmpty()) {
            System.out.println("(бос)");
        }
        rows.forEach(System.out::println);
    }
}
