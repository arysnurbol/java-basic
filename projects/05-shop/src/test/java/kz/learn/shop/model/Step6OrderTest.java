package kz.learn.shop.model;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.discount.FixedDiscount;
import kz.learn.shop.discount.PercentDiscount;
import kz.learn.shop.exception.OrderStateException;
import kz.learn.shop.exception.ProductNotFoundException;
import kz.learn.shop.state.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Қадам 6 — Order: State үлгісінің context-і")
class Step6OrderTest {

    private static final Product LAPTOP = new Product("P1", "Ноутбук", Category.ELECTRONICS, Money.of(350_000));
    private static final Product BOOK = new Product("B1", "Абай жолы", Category.BOOKS, Money.of(6_500));
    private static final Product KURT = new Product("F1", "Құрт", Category.FOOD, Money.of(1_200));

    private static Order order(DeliveryType delivery) {
        return new Order("ORD-0001", "Aru", delivery);
    }

    @Test
    @DisplayName("жаңа тапсырыс: NEW, бос, өрістер strip етіледі")
    void created() {
        Order order = new Order(" ORD-0001 ", " Aru ", DeliveryType.PICKUP);

        assertEquals("ORD-0001", order.getId());
        assertEquals("Aru", order.getCustomer());
        assertEquals(DeliveryType.PICKUP, order.getDelivery());
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertEquals(List.of(), order.getLines());
        assertEquals(0, order.units());
        assertEquals(Money.ZERO, order.subtotal());
        assertEquals(Money.ZERO, order.total());
    }

    @Test
    @DisplayName("конструктор тексерістері")
    void validation() {
        assertEquals("id must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Order(" ", "Aru", DeliveryType.POST)).getMessage());
        assertEquals("customer must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Order("ORD-0001", null, DeliveryType.POST)).getMessage());
        assertEquals("delivery", assertThrows(NullPointerException.class,
                () -> new Order("ORD-0001", "Aru", null)).getMessage());
    }

    @Test
    @DisplayName("addItem: жолдар қосылған ретімен; сол тауар қайта қосылса — саны қосылады")
    void addItem() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(BOOK, 2);
        order.addItem(KURT, 3);
        order.addItem(BOOK, 1);

        assertEquals(List.of(new OrderLine(BOOK, 3), new OrderLine(KURT, 3)), order.getLines());
        assertEquals(6, order.units());
        assertEquals(Money.of(23_100), order.subtotal(), "3*6500 + 3*1200");
    }

    @Test
    @DisplayName("addItem: қате сан ескі жолды бұзбайды (0 да қате, 99-дан асса да қате)")
    void addItemValidation() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(BOOK, 98);

        assertThrows(IllegalArgumentException.class, () -> order.addItem(BOOK, 0), "ескі 98 + 0 жарамды, бірақ 0 қосу — қате");
        assertThrows(IllegalArgumentException.class, () -> order.addItem(BOOK, -1));
        assertThrows(IllegalArgumentException.class, () -> order.addItem(BOOK, 2), "98 + 2 = 100 > 99");
        assertThrows(NullPointerException.class, () -> order.addItem(null, 1));
        assertEquals(List.of(new OrderLine(BOOK, 98)), order.getLines());

        order.addItem(BOOK, 1);
        assertEquals(99, order.units());
    }

    @Test
    @DisplayName("getLines: көшірме қайтарады")
    void linesAreCopy() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(BOOK, 1);

        assertThrows(UnsupportedOperationException.class, () -> order.getLines().clear());
        assertEquals(1, order.getLines().size());
    }

    @Test
    @DisplayName("removeItem: жолды толық өшіреді; жоқ тауар -> ProductNotFoundException")
    void removeItem() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(BOOK, 2);
        order.addItem(KURT, 1);

        order.removeItem("B1");
        assertEquals(List.of(new OrderLine(KURT, 1)), order.getLines());
        assertEquals("P1", assertThrows(ProductNotFoundException.class, () -> order.removeItem("P1")).getId());
    }

    @Test
    @DisplayName("сомалар: жеңілдік тауарға, жеткізу жеңілдіктен КЕЙІНГІ сомаға қарайды")
    void totals() {
        Order order = order(DeliveryType.COURIER);
        order.addItem(BOOK, 3);   // 19 500
        order.addItem(KURT, 1);   //  1 200

        assertEquals(Money.of(20_700), order.subtotal());
        assertEquals(Money.ZERO, order.discount());
        assertEquals(Money.ZERO, order.deliveryCost(), ">= 20 000 — курьер тегін");
        assertEquals(Money.of(20_700), order.total());

        order.applyDiscount(new PercentDiscount(10));
        assertEquals(Money.of(2_070), order.discount());
        assertEquals(Money.of(1_500), order.deliveryCost(), "жеңілдіктен кейін 18 630 < 20 000 — курьер ақылы");
        assertEquals(Money.of(20_130), order.total(), "20 700 - 2 070 + 1 500");
    }

    @Test
    @DisplayName("жеңілдік ауыстырылады; FixedDiscount тауар сомасынан аспайды; POST дана санына қарайды")
    void discountAndPost() {
        Order order = order(DeliveryType.POST);
        order.addItem(KURT, 2);   // 2 400
        order.applyDiscount(new PercentDiscount(50));
        order.applyDiscount(new FixedDiscount(Money.of(5_000)));

        assertEquals(Money.of(2_400), order.discount(), "соңғы жеңілдік жарайды, бірақ 2 400-ден аспайды");
        assertEquals(Money.of(1_400), order.deliveryCost(), "1 000 + 2*200");
        assertEquals(Money.of(1_400), order.total(), "тауар тегін, тек жеткізу");
        assertThrows(NullPointerException.class, () -> order.applyDiscount(null));
    }

    @Test
    @DisplayName("күйлер: Order әрекетті күйге тапсырады")
    void lifecycle() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(LAPTOP, 1);

        order.pay();
        assertEquals(OrderStatus.PAID, order.getStatus());
        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        assertEquals("Cannot cancel order in status SHIPPED",
                assertThrows(OrderStateException.class, order::cancel).getMessage());
        assertEquals(OrderStatus.SHIPPED, order.getStatus(), "сәтсіз әрекет күйді өзгертпейді");
        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("pay: бос тапсырысты төлеуге болмайды, бірақ алдымен күй тексеріледі")
    void payEmpty() {
        Order order = order(DeliveryType.PICKUP);

        assertEquals("Cannot pay empty order", assertThrows(OrderStateException.class, order::pay).getMessage());
        assertEquals(OrderStatus.NEW, order.getStatus());

        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals("Cannot pay order in status CANCELLED",
                assertThrows(OrderStateException.class, order::pay).getMessage());
    }

    @Test
    @DisplayName("NEW-нан кейін құрам өзгермейді")
    void frozenAfterPay() {
        Order order = order(DeliveryType.PICKUP);
        order.addItem(BOOK, 1);
        order.pay();

        String expected = "Order ORD-0001 cannot be changed in status PAID";
        assertEquals(expected, assertThrows(OrderStateException.class, () -> order.addItem(KURT, 1)).getMessage());
        assertEquals(expected, assertThrows(OrderStateException.class, () -> order.removeItem("B1")).getMessage());
        assertEquals(expected, assertThrows(OrderStateException.class,
                () -> order.applyDiscount(new PercentDiscount(10))).getMessage());
        assertEquals(List.of(new OrderLine(BOOK, 1)), order.getLines());
        assertEquals(Money.of(6_500), order.total());
    }

    @Test
    @DisplayName("equals/hashCode — тек id бойынша; toString")
    void identity() {
        Order a = new Order("ORD-0001", "Aru", DeliveryType.PICKUP);
        Order b = new Order("ORD-0001", "Daniyar", DeliveryType.POST);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, new Order("ORD-0002", "Aru", DeliveryType.PICKUP));

        a.addItem(BOOK, 2);
        assertEquals("ORD-0001: Aru, PICKUP, 2 дана, 13000.00 ₸, NEW", a.toString());
    }
}
