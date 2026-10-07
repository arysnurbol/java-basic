package kz.learn.shop.service;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.model.Category;
import kz.learn.shop.model.Money;
import kz.learn.shop.model.Order;
import kz.learn.shop.model.Product;
import kz.learn.shop.repository.InMemoryRepository;
import kz.learn.shop.state.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Қадам 8 — Есептер")
class Step8ReportsTest {

    private ShopService shop;

    /**
     * ORD-0001 Aru      PICKUP   B1 x2, F1 x5          -> DELIVERED   13 000 + 6 000 = 19 000
     * ORD-0002 Daniyar  COURIER  P1 x1, SALE10         -> SHIPPED     350 000 - 35 000 + 0 = 315 000
     * ORD-0003 aru      POST     F1 x10                -> PAID        12 000 + 1 000 + 10*200 = 15 000
     * ORD-0004 Aru      PICKUP   P2 x1                 -> CANCELLED (төленген соң)
     * ORD-0005 Dana     PICKUP   B1 x1                 -> NEW
     */
    @BeforeEach
    void setUp() {
        shop = new ShopService(new InMemoryRepository<>(), new InMemoryRepository<>());
        shop.addProduct("P1", "Ноутбук", Category.ELECTRONICS, 350_000);
        shop.addProduct("P2", "Құлаққап", Category.ELECTRONICS, 12_000);
        shop.addProduct("B1", "Абай жолы", Category.BOOKS, 6_500);
        shop.addProduct("C1", "Футболка", Category.CLOTHING, 4_000);
        shop.addProduct("F1", "Құрт", Category.FOOD, 1_200);
        for (String id : List.of("P1", "P2", "B1", "C1", "F1")) {
            shop.restock(id, 20);
        }

        String o1 = order("Aru", DeliveryType.PICKUP, "B1", 2, "F1", 5);
        shop.pay(o1);
        shop.ship(o1);
        shop.deliver(o1);

        String o2 = order("Daniyar", DeliveryType.COURIER, "P1", 1);
        shop.applyPromo(o2, "SALE10");
        shop.pay(o2);
        shop.ship(o2);

        shop.pay(order("aru", DeliveryType.POST, "F1", 10));

        String o4 = order("Aru", DeliveryType.PICKUP, "P2", 1);
        shop.pay(o4);
        shop.cancel(o4);

        order("Dana", DeliveryType.PICKUP, "B1", 1);
    }

    /** Аргументтер: артикул, саны, артикул, саны, ... */
    private String order(String customer, DeliveryType delivery, Object... items) {
        String id = shop.createOrder(customer, delivery).getId();
        for (int i = 0; i < items.length; i += 2) {
            shop.addItem(id, (String) items[i], (Integer) items[i + 1]);
        }
        return id;
    }

    @Test
    @DisplayName("ordersOf: регистрге қарамай, барлық күйде, нөмір бойынша")
    void ordersOf() {
        assertEquals(List.of("ORD-0001", "ORD-0003", "ORD-0004"),
                shop.ordersOf(" ARU ").stream().map(Order::getId).toList());
        assertEquals(List.of(), shop.ordersOf("Белгісіз"));
    }

    @Test
    @DisplayName("countByStatus: тек кездесетін күйлер, enum ретімен")
    void countByStatus() {
        Map<OrderStatus, Long> counts = shop.countByStatus();

        assertInstanceOf(EnumMap.class, counts);
        assertEquals(List.of(OrderStatus.NEW, OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.DELIVERED,
                OrderStatus.CANCELLED), List.copyOf(counts.keySet()));
        assertEquals(List.of(1L, 1L, 1L, 1L, 1L), List.copyOf(counts.values()));
    }

    @Test
    @DisplayName("revenue: PAID + SHIPPED + DELIVERED, жеткізу мен жеңілдікті ескереді")
    void revenue() {
        assertEquals(Money.of(349_000), shop.revenue(), "19 000 + 315 000 + 15 000");
    }

    @Test
    @DisplayName("salesByCategory: төленген тапсырыстардың жолдары, жеңілдікке дейін, enum ретімен")
    void salesByCategory() {
        Map<Category, Money> sales = shop.salesByCategory();

        assertInstanceOf(EnumMap.class, sales);
        assertEquals(List.of(Category.ELECTRONICS, Category.BOOKS, Category.FOOD), List.copyOf(sales.keySet()),
                "CLOTHING сатылмады, болдырылған P2 мен NEW тапсырыс есептелмейді");
        assertEquals(Money.of(350_000), sales.get(Category.ELECTRONICS), "SALE10 мұнда шегерілмейді");
        assertEquals(Money.of(13_000), sales.get(Category.BOOKS));
        assertEquals(Money.of(18_000), sales.get(Category.FOOD), "F1: 5 + 10 дана");
    }

    @Test
    @DisplayName("topProducts: сатылған дана бойынша, тең болса — артикул бойынша")
    void topProducts() {
        assertEquals(List.of("F1", "B1", "P1"), shop.topProducts(5).stream().map(Product::id).toList());
        assertEquals(List.of("F1"), shop.topProducts(1).stream().map(Product::id).toList());

        String extra = order("Dana", DeliveryType.PICKUP, "C1", 2);
        shop.pay(extra);
        assertEquals(List.of("F1", "B1", "C1"), shop.topProducts(3).stream().map(Product::id).toList(),
                "B1 мен C1 — 2 данадан: артикул бойынша B1 бірінші");
    }

    @Test
    @DisplayName("бос дүкен: есептер бос, табыс нөл")
    void empty() {
        ShopService empty = new ShopService(new InMemoryRepository<>(), new InMemoryRepository<>());

        assertEquals(Map.of(), empty.countByStatus());
        assertEquals(Money.ZERO, empty.revenue());
        assertEquals(Map.of(), empty.salesByCategory());
        assertEquals(List.of(), empty.topProducts(3));
    }
}
