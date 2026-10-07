package kz.learn.shop.model;

import kz.learn.shop.exception.InvalidPromoCodeException;
import kz.learn.shop.exception.OrderNotFoundException;
import kz.learn.shop.exception.OrderStateException;
import kz.learn.shop.exception.OutOfStockException;
import kz.learn.shop.exception.ProductNotFoundException;
import kz.learn.shop.exception.ShopException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Қадам 1 — Product, OrderLine және exception-дар")
class Step1ModelTest {

    private static Product laptop() {
        return new Product("P1", "Ноутбук", Category.ELECTRONICS, Money.of(350_000));
    }

    @Test
    @DisplayName("Product: өрістер strip етіледі, record өзі equals береді")
    void product() {
        Product p = new Product(" P1 ", " Ноутбук ", Category.ELECTRONICS, Money.of(350_000));

        assertEquals("P1", p.id());
        assertEquals("P1", p.getId(), "Identifiable арқылы да сол id");
        assertEquals("Ноутбук", p.name());
        assertEquals(laptop(), p, "record: барлық өріс тең болса — объектілер тең");
        assertEquals("P1 Ноутбук (ELECTRONICS) 350000.00 ₸", p.toString());
    }

    @Test
    @DisplayName("Product: тексерістер мен хабарламалар")
    void productValidation() {
        Money price = Money.of(100);
        assertEquals("id must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Product("  ", "x", Category.FOOD, price)).getMessage());
        assertEquals("name must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Product("F1", null, Category.FOOD, price)).getMessage());
        assertEquals("category", assertThrows(NullPointerException.class,
                () -> new Product("F1", "Құрт", null, price)).getMessage());
        assertEquals("price", assertThrows(NullPointerException.class,
                () -> new Product("F1", "Құрт", Category.FOOD, null)).getMessage());
        assertEquals("price must be positive", assertThrows(IllegalArgumentException.class,
                () -> new Product("F1", "Құрт", Category.FOOD, Money.ZERO)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new Product("F1", "Құрт", Category.FOOD, Money.of(-5)));
    }

    @Test
    @DisplayName("OrderLine: сомасы = баға * саны")
    void orderLine() {
        OrderLine line = new OrderLine(laptop(), 2);

        assertEquals(Money.of(700_000), line.total());
        assertEquals(Money.of("3.75"), new OrderLine(new Product("F1", "Құрт", Category.FOOD, Money.of("1.25")), 3).total());
        assertEquals("P1 Ноутбук x2 = 700000.00 ₸", line.toString());
    }

    @Test
    @DisplayName("OrderLine: саны 1..99, тауар null емес")
    void orderLineValidation() {
        assertEquals("quantity must be between 1 and 99", assertThrows(IllegalArgumentException.class,
                () -> new OrderLine(laptop(), 0)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new OrderLine(laptop(), -1));
        assertThrows(IllegalArgumentException.class, () -> new OrderLine(laptop(), 100));
        assertEquals(99, new OrderLine(laptop(), 99).quantity(), "шекара кіреді");
        assertEquals("product", assertThrows(NullPointerException.class, () -> new OrderLine(null, 1)).getMessage());
    }

    @Test
    @DisplayName("exception-дар: хабарлама, өрістер және ортақ ата-класс")
    void exceptions() {
        ProductNotFoundException pnf = new ProductNotFoundException("P9");
        assertEquals("Product not found: P9", pnf.getMessage());
        assertEquals("P9", pnf.getId());

        OrderNotFoundException onf = new OrderNotFoundException("ORD-0009");
        assertEquals("Order not found: ORD-0009", onf.getMessage());
        assertEquals("ORD-0009", onf.getId());

        OutOfStockException oos = new OutOfStockException("P1", 3, 2);
        assertEquals("Not enough P1: requested 3, available 2", oos.getMessage());
        assertEquals("P1", oos.getProductId());
        assertEquals(3, oos.getRequested());
        assertEquals(2, oos.getAvailable());

        assertEquals("Cannot ship order in status NEW", new OrderStateException("Cannot ship order in status NEW").getMessage());

        InvalidPromoCodeException ipc = new InvalidPromoCodeException("SALE99");
        assertEquals("Invalid promo code: SALE99", ipc.getMessage());
        assertEquals("SALE99", ipc.getCode());

        assertEquals("boom", new ShopException("boom").getMessage());
        for (RuntimeException e : new RuntimeException[]{pnf, onf, oos, ipc, new OrderStateException("x")}) {
            assertInstanceOf(ShopException.class, e, "барлығы ShopException-нан тарайды: " + e.getClass());
        }
    }
}
