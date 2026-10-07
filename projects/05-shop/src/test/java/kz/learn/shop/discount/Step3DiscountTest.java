package kz.learn.shop.discount;

import kz.learn.shop.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Қадам 3 — Strategy: DiscountPolicy, PercentDiscount, FixedDiscount")
class Step3DiscountTest {

    @Test
    @DisplayName("none(): әрқашан нөл")
    void none() {
        assertEquals(Money.ZERO, DiscountPolicy.none().discountFor(Money.of(50_000)));
        assertEquals(Money.ZERO, DiscountPolicy.none().discountFor(Money.ZERO));
    }

    @Test
    @DisplayName("PercentDiscount: сомадан пайыз, HALF_EVEN-мен дөңгелектенеді")
    void percent() {
        assertEquals(Money.of(5_000), new PercentDiscount(10).discountFor(Money.of(50_000)));
        assertEquals(Money.of(25_000), new PercentDiscount(50).discountFor(Money.of(50_000)), "50 — шекара кіреді");
        assertEquals(Money.of("0.12"), new PercentDiscount(1).discountFor(Money.of("12.50")), "0.125 -> 0.12 (ең жақын жұп)");
        assertEquals(Money.ZERO, new PercentDiscount(15).discountFor(Money.ZERO));
    }

    @Test
    @DisplayName("PercentDiscount: 1..50 ғана")
    void percentValidation() {
        assertEquals("percent must be between 1 and 50",
                assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(0)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(51));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-10));
    }

    @Test
    @DisplayName("FixedDiscount: белгілі сома, бірақ тауар сомасынан аспайды")
    void fixed() {
        FixedDiscount minus5000 = new FixedDiscount(Money.of(5_000));

        assertEquals(Money.of(5_000), minus5000.discountFor(Money.of(30_000)));
        assertEquals(Money.of(5_000), minus5000.discountFor(Money.of(5_000)), "тең — толық жеңілдік");
        assertEquals(Money.of(3_000), minus5000.discountFor(Money.of(3_000)), "тапсырыс теріс болмайды");
        assertEquals(Money.ZERO, minus5000.discountFor(Money.ZERO));
    }

    @Test
    @DisplayName("FixedDiscount: сома null емес және оң")
    void fixedValidation() {
        assertEquals("amount", assertThrows(NullPointerException.class, () -> new FixedDiscount(null)).getMessage());
        assertEquals("amount must be positive",
                assertThrows(IllegalArgumentException.class, () -> new FixedDiscount(Money.ZERO)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new FixedDiscount(Money.of(-100)));
    }

    @Test
    @DisplayName("функционалдық интерфейс: кез келген лямбда да стратегия бола алады")
    void lambda() {
        DiscountPolicy thousandOff = subtotal -> Money.of(1_000);
        assertEquals(Money.of(1_000), thousandOff.discountFor(Money.of(10_000)));
    }
}
