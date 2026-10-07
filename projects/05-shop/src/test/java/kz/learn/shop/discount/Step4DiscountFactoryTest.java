package kz.learn.shop.discount;

import kz.learn.shop.exception.InvalidPromoCodeException;
import kz.learn.shop.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 4 — Factory: промокодтан жеңілдік")
class Step4DiscountFactoryTest {

    private static final Money HUNDRED_K = Money.of(100_000);

    @Test
    @DisplayName("SALE<n> -> PercentDiscount(n)")
    void sale() {
        DiscountPolicy policy = DiscountFactory.fromCode("SALE10");

        assertEquals(new PercentDiscount(10), policy, "record: equals өрістер бойынша");
        assertEquals(Money.of(10_000), policy.discountFor(HUNDRED_K));
    }

    @Test
    @DisplayName("MINUS<n> -> FixedDiscount(n)")
    void minus() {
        DiscountPolicy policy = DiscountFactory.fromCode("MINUS5000");

        assertEquals(new FixedDiscount(Money.of(5_000)), policy);
        assertEquals(Money.of(5_000), policy.discountFor(HUNDRED_K));
    }

    @Test
    @DisplayName("код қалыпқа келтіріледі: бос орындар мен регистр маңызды емес")
    void normalized() {
        assertEquals(new PercentDiscount(25), DiscountFactory.fromCode("  sale25 "));
        assertEquals(new FixedDiscount(Money.of(700)), DiscountFactory.fromCode("Minus700"));
    }

    @Test
    @DisplayName("null немесе бос код -> жеңілдік жоқ")
    void empty() {
        assertEquals(Money.ZERO, DiscountFactory.fromCode(null).discountFor(HUNDRED_K));
        assertEquals(Money.ZERO, DiscountFactory.fromCode("").discountFor(HUNDRED_K));
        assertEquals(Money.ZERO, DiscountFactory.fromCode("   ").discountFor(HUNDRED_K));
    }

    @Test
    @DisplayName("белгісіз код -> InvalidPromoCodeException, хабарламада қалыпқа келген код")
    void unknown() {
        InvalidPromoCodeException e = assertThrows(InvalidPromoCodeException.class,
                () -> DiscountFactory.fromCode(" hello "));
        assertEquals("HELLO", e.getCode());
        assertEquals("Invalid promo code: HELLO", e.getMessage());
        assertThrows(InvalidPromoCodeException.class, () -> DiscountFactory.fromCode("10SALE"));
    }

    @Test
    @DisplayName("тұзақ: сан қате немесе record қабылдамайды -> бәрі InvalidPromoCodeException")
    void badNumbers() {
        // NumberFormatException — IllegalArgumentException-ның ұрпағы. Біреуін ұстап, екіншісін ұмытпа.
        for (String code : new String[]{"SALE", "SALEABC", "SALE10.5", "SALE99999999999", "MINUS", "MINUSX"}) {
            assertEquals(code, assertThrows(InvalidPromoCodeException.class,
                    () -> DiscountFactory.fromCode(code), "сан емес: " + code).getCode());
        }
        for (String code : new String[]{"SALE0", "SALE51", "SALE-5", "MINUS0", "MINUS-5"}) {
            assertEquals(code, assertThrows(InvalidPromoCodeException.class,
                    () -> DiscountFactory.fromCode(code), "record қабылдамайды: " + code).getCode());
        }
    }

    @Test
    @DisplayName("утилита-класс: final, конструкторы private")
    void utilityClass() {
        assertTrue(Modifier.isFinal(DiscountFactory.class.getModifiers()));
        for (Constructor<?> c : DiscountFactory.class.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(c.getModifiers()), "конструктор private болуы керек: " + c);
        }
    }
}
