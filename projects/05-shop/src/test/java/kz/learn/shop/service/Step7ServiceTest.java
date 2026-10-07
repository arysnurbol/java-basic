package kz.learn.shop.service;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.discount.PercentDiscount;
import kz.learn.shop.exception.InvalidPromoCodeException;
import kz.learn.shop.exception.OrderNotFoundException;
import kz.learn.shop.exception.OrderStateException;
import kz.learn.shop.exception.OutOfStockException;
import kz.learn.shop.exception.ProductNotFoundException;
import kz.learn.shop.exception.ShopException;
import kz.learn.shop.model.Category;
import kz.learn.shop.model.Money;
import kz.learn.shop.model.Order;
import kz.learn.shop.model.OrderLine;
import kz.learn.shop.model.Product;
import kz.learn.shop.repository.InMemoryRepository;
import kz.learn.shop.repository.Repository;
import kz.learn.shop.state.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Қадам 7 — ShopService: тауарлар, қойма, тапсырыс өмірлік циклі")
class Step7ServiceTest {

    private Repository<Product, String> products;
    private Repository<Order, String> orders;
    private ShopService shop;

    @BeforeEach
    void setUp() {
        products = new InMemoryRepository<>();
        orders = new InMemoryRepository<>();
        shop = new ShopService(products, orders);
        shop.addProduct("P1", "Ноутбук", Category.ELECTRONICS, 350_000);
        shop.addProduct("B1", "Абай жолы", Category.BOOKS, 6_500);
        shop.addProduct("F1", "Құрт", Category.FOOD, 1_200);
        shop.restock("P1", 2);
        shop.restock("B1", 5);
        shop.restock("F1", 50);
    }

    private static List<String> ids(List<Product> list) {
        return list.stream().map(Product::id).toList();
    }

    @Test
    @DisplayName("addProduct: сақталады, қалдығы 0; қайталанған артикул -> ShopException")
    void addProduct() {
        Product p = shop.addProduct(" C1 ", "Футболка", Category.CLOTHING, 4_000);

        assertEquals("C1", p.id());
        assertSame(p, products.findById("C1").orElseThrow());
        assertEquals(Money.of(4_000), p.price());
        assertEquals(0, shop.stockOf("C1"));

        ShopException e = assertThrows(ShopException.class,
                () -> shop.addProduct(" P1 ", "Басқа", Category.FOOD, 1));
        assertEquals("Product already exists: P1", e.getMessage());
        assertEquals("Ноутбук", shop.getProduct("P1").name(), "бар тауар ауыстырылмады");
        assertThrows(IllegalArgumentException.class, () -> shop.addProduct("X1", "Тегін", Category.FOOD, 0));
    }

    @Test
    @DisplayName("getProduct / getOrder: жоқ болса — өз exception-ы")
    void notFound() {
        assertEquals("P9", assertThrows(ProductNotFoundException.class, () -> shop.getProduct("P9")).getId());
        assertEquals("ORD-0404", assertThrows(OrderNotFoundException.class, () -> shop.getOrder("ORD-0404")).getId());
    }

    @Test
    @DisplayName("catalog: санат (enum реті), сосын артикул")
    void catalog() {
        shop.addProduct("P0", "Тінтуір", Category.ELECTRONICS, 5_000);
        shop.addProduct("A1", "Алма", Category.FOOD, 500);

        assertEquals(List.of("P0", "P1", "B1", "A1", "F1"), ids(shop.catalog()));
    }

    @Test
    @DisplayName("restock / stockOf: қалдық қосылады; тексерістер")
    void restock() {
        shop.restock("B1", 3);
        assertEquals(8, shop.stockOf("B1"));

        assertEquals("quantity must be positive",
                assertThrows(IllegalArgumentException.class, () -> shop.restock("B1", 0)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> shop.restock("B1", -2));
        assertEquals(8, shop.stockOf("B1"));
        assertThrows(ProductNotFoundException.class, () -> shop.restock("P9", 1));
        assertThrows(ProductNotFoundException.class, () -> shop.stockOf("P9"));
    }

    @Test
    @DisplayName("createOrder: ORD-0001, ORD-0002...; сәтсіз жасау нөмірді жемейді")
    void createOrder() {
        Order first = shop.createOrder("Aru", DeliveryType.COURIER);
        assertEquals("ORD-0001", first.getId());
        assertEquals(OrderStatus.NEW, first.getStatus());
        assertSame(first, orders.findById("ORD-0001").orElseThrow());

        assertThrows(IllegalArgumentException.class, () -> shop.createOrder(" ", DeliveryType.POST));
        assertThrows(NullPointerException.class, () -> shop.createOrder("Aru", null));
        assertEquals("ORD-0002", shop.createOrder("Daniyar", DeliveryType.POST).getId());
        assertEquals(2, orders.findAll().size());
    }

    @Test
    @DisplayName("addItem / removeItem / applyPromo сервис арқылы; қалдық себетте тексерілмейді")
    void cart() {
        String id = shop.createOrder("Aru", DeliveryType.PICKUP).getId();

        Order order = shop.addItem(id, "P1", 5);
        assertEquals(List.of(new OrderLine(shop.getProduct("P1"), 5)), order.getLines(), "қоймада 2, бірақ себетке 5 салуға болады");
        assertEquals(2, shop.stockOf("P1"), "себет қойманы өзгертпейді");

        shop.removeItem(id, "P1");
        shop.addItem(id, "B1", 2);
        shop.applyPromo(id, "sale10");
        assertEquals(Money.of(1_300), shop.getOrder(id).discount());
        shop.applyPromo(id, "");
        assertEquals(Money.ZERO, shop.getOrder(id).discount(), "бос код жеңілдікті алып тастайды");

        assertThrows(ProductNotFoundException.class, () -> shop.addItem(id, "P9", 1));
        assertThrows(OrderNotFoundException.class, () -> shop.addItem("ORD-0404", "B1", 1));
        assertThrows(InvalidPromoCodeException.class, () -> shop.applyPromo(id, "FREE"));
    }

    @Test
    @DisplayName("pay: қалдық азаяды, сома қайтарылады")
    void pay() {
        String id = shop.createOrder("Aru", DeliveryType.COURIER).getId();
        shop.addItem(id, "B1", 2);   // 13 000
        shop.addItem(id, "F1", 3);   //  3 600

        assertEquals(Money.of(18_100), shop.pay(id), "16 600 + курьер 1 500");
        assertEquals(OrderStatus.PAID, shop.getOrder(id).getStatus());
        assertEquals(3, shop.stockOf("B1"));
        assertEquals(47, shop.stockOf("F1"));
    }

    @Test
    @DisplayName("pay: қалдық жетпесе — ештеңе өзгермейді (бірінші жетпеген жол хабарланады)")
    void payOutOfStock() {
        String id = shop.createOrder("Aru", DeliveryType.PICKUP).getId();
        shop.addItem(id, "F1", 10);
        shop.addItem(id, "P1", 3);
        shop.addItem(id, "B1", 9);

        OutOfStockException e = assertThrows(OutOfStockException.class, () -> shop.pay(id));
        assertEquals("Not enough P1: requested 3, available 2", e.getMessage());
        assertEquals(OrderStatus.NEW, shop.getOrder(id).getStatus());
        assertEquals(50, shop.stockOf("F1"), "F1 жеткілікті еді, бірақ оның қалдығы да өзгермеуі керек");
        assertEquals(2, shop.stockOf("P1"));
    }

    @Test
    @DisplayName("pay: күй рұқсат бермесе немесе тапсырыс бос болса — қойма өзгермейді")
    void payNotAllowed() {
        String id = shop.createOrder("Aru", DeliveryType.PICKUP).getId();
        assertEquals("Cannot pay empty order", assertThrows(OrderStateException.class, () -> shop.pay(id)).getMessage());

        shop.addItem(id, "B1", 1);
        shop.pay(id);
        assertEquals(4, shop.stockOf("B1"));
        assertEquals("Cannot pay order in status PAID",
                assertThrows(OrderStateException.class, () -> shop.pay(id)).getMessage());
        assertEquals(4, shop.stockOf("B1"), "екінші рет есептен шығарылмады");
    }

    @Test
    @DisplayName("ship / deliver: күйлер ретімен")
    void shipAndDeliver() {
        String id = shop.createOrder("Aru", DeliveryType.POST).getId();
        shop.addItem(id, "B1", 1);

        assertThrows(OrderStateException.class, () -> shop.ship(id), "төленбеген тапсырыс жіберілмейді");
        shop.pay(id);
        shop.ship(id);
        assertEquals(OrderStatus.SHIPPED, shop.getOrder(id).getStatus());
        shop.deliver(id);
        assertEquals(OrderStatus.DELIVERED, shop.getOrder(id).getStatus());
        assertThrows(OrderNotFoundException.class, () -> shop.ship("ORD-0404"));
    }

    @Test
    @DisplayName("cancel: NEW — 0 қайтарылады; PAID — толық сома, тауар қоймаға оралады")
    void cancel() {
        String fresh = shop.createOrder("Aru", DeliveryType.PICKUP).getId();
        shop.addItem(fresh, "B1", 2);
        assertEquals(Money.ZERO, shop.cancel(fresh));
        assertEquals(5, shop.stockOf("B1"));
        assertEquals(OrderStatus.CANCELLED, shop.getOrder(fresh).getStatus());

        String paid = shop.createOrder("Daniyar", DeliveryType.POST).getId();
        shop.addItem(paid, "B1", 2);
        shop.applyPromo(paid, "MINUS1000");
        Money total = shop.pay(paid);
        assertEquals(Money.of(13_400), total, "13 000 - 1 000 + почта 1 400");
        assertEquals(3, shop.stockOf("B1"));

        assertEquals(total, shop.cancel(paid));
        assertEquals(5, shop.stockOf("B1"));
    }

    @Test
    @DisplayName("cancel: жіберілген тапсырысты болдыруға болмайды — қойма өзгермейді")
    void cancelShipped() {
        String id = shop.createOrder("Aru", DeliveryType.PICKUP).getId();
        shop.addItem(id, "P1", 1);
        shop.pay(id);
        shop.ship(id);

        assertEquals("Cannot cancel order in status SHIPPED",
                assertThrows(OrderStateException.class, () -> shop.cancel(id)).getMessage());
        assertEquals(1, shop.stockOf("P1"));
        assertEquals(OrderStatus.SHIPPED, shop.getOrder(id).getStatus());
    }

    @Test
    @DisplayName("сервис жеңілдікті тек фабрика арқылы жасайды — нәтиже record-пен тең")
    void promoUsesFactory() {
        String id = shop.createOrder("Aru", DeliveryType.PICKUP).getId();
        shop.addItem(id, "P1", 1);
        shop.applyPromo(id, " SALE20 ");

        assertEquals(new PercentDiscount(20).discountFor(Money.of(350_000)), shop.getOrder(id).discount());
    }
}
