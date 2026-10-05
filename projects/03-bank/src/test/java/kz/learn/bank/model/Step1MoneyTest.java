package kz.learn.bank.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 1 — Money: BigDecimal, immutable value object")
class Step1MoneyTest {

    @Test
    @DisplayName("of(...): әрқашан 2 таңба үтірден кейін, toString '₸'-мен")
    void factories() {
        assertEquals(new BigDecimal("5.00"), Money.of(5).amount());
        assertEquals(new BigDecimal("1234.50"), Money.of("1234.5").amount());
        assertEquals("1234.50 ₸", Money.of("1234.5").toString());
        assertEquals("0.00 ₸", Money.ZERO.toString());
        assertEquals("-7.00 ₸", Money.of(-7).toString());
    }

    @Test
    @DisplayName("double қателеседі, BigDecimal — жоқ: 0.1 + 0.2 == 0.3")
    void noDoubleErrors() {
        assertNotEquals(0.3, 0.1 + 0.2, "double-да 0.1 + 0.2 = 0.30000000000000004");
        assertEquals(Money.of("0.3"), Money.of("0.1").plus(Money.of("0.2")));
    }

    @Test
    @DisplayName("equals масштабқа тәуелді емес: 1.0 == 1.00 (BigDecimal.equals-тың тұзағы)")
    void equalsIgnoresScale() {
        assertNotEquals(new BigDecimal("1.0"), new BigDecimal("1.00"), "BigDecimal.equals scale-ді де салыстырады");
        assertEquals(Money.of("1.0"), Money.of("1.00"));
        assertEquals(Money.of("1.0").hashCode(), Money.of("1.00").hashCode());
        assertEquals(Money.of(1), Money.of("1"));
        assertNotEquals(Money.of(1), Money.of(2));
        assertNotEquals(Money.of(1), "1.00 ₸");
    }

    @Test
    @DisplayName("дөңгелектеу — HALF_EVEN (банкирлік): 0.125 -> 0.12, 0.135 -> 0.14")
    void halfEven() {
        assertEquals(Money.of("0.12"), Money.of("0.125"));
        assertEquals(Money.of("0.14"), Money.of("0.135"));
        assertEquals(Money.of("2.68"), Money.of("2.675"));
        assertEquals(Money.of("-0.12"), Money.of("-0.125"));
        assertEquals(Money.of("0.13"), Money.of("0.1251"), "жартыдан көп — жоғары");
    }

    @Test
    @DisplayName("plus / minus / negate — ЖАҢА объект қайтарады, өзі өзгермейді")
    void arithmetic() {
        Money a = Money.of(100);
        Money b = Money.of("30.25");

        assertEquals(Money.of("130.25"), a.plus(b));
        assertEquals(Money.of("69.75"), a.minus(b));
        assertEquals(Money.of("-69.75"), b.minus(a));
        assertEquals(Money.of(-100), a.negate());
        assertEquals(Money.of(100), a, "a өзгермеді — immutable");
        assertEquals(Money.of("30.25"), b);
    }

    @Test
    @DisplayName("percent: amount * p / 100, нәтиже HALF_EVEN-мен дөңгелектеледі")
    void percent() {
        assertEquals(Money.of(15), Money.of(1000).percent(new BigDecimal("1.5")));
        assertEquals(Money.of(5), Money.of("333.33").percent(new BigDecimal("1.5")), "4.99995 -> 5.00");
        assertEquals(Money.of("0.02"), Money.of(10).percent(new BigDecimal("0.25")), "0.025 -> 0.02");
        assertEquals(Money.of(250), Money.of(1000).percent(new BigDecimal("25")));
    }

    @Test
    @DisplayName("салыстыру: compareTo, isGreaterThan, max, isPositive, isNegative")
    void comparisons() {
        Money small = Money.of(10);
        Money big = Money.of("10.01");

        assertTrue(small.compareTo(big) < 0);
        assertTrue(big.compareTo(small) > 0);
        assertEquals(0, Money.of("10").compareTo(Money.of("10.00")));
        assertTrue(big.isGreaterThan(small));
        assertFalse(small.isGreaterThan(big));
        assertFalse(small.isGreaterThan(Money.of(10)));
        assertSame(big, small.max(big));
        assertSame(big, big.max(small));

        assertTrue(Money.of("0.01").isPositive());
        assertFalse(Money.ZERO.isPositive());
        assertFalse(Money.ZERO.isNegative());
        assertTrue(Money.of("-0.01").isNegative());
    }

    @Test
    @DisplayName("of(\"abc\") -> IllegalArgumentException (NumberFormatException — оның ұрпағы)")
    void badInput() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("abc"));
        assertThrows(IllegalArgumentException.class, () -> Money.of(""));
    }

    @Test
    @DisplayName("құрылым: final класс, барлық өрістер private final, public конструктор жоқ")
    void structure() {
        assertTrue(Modifier.isFinal(Money.class.getModifiers()), "Money final болуы керек");
        assertEquals(0, Money.class.getConstructors().length, "объект тек of(...) арқылы жасалады");
        for (Field f : Money.class.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                assertTrue(Modifier.isPrivate(f.getModifiers()) && Modifier.isFinal(f.getModifiers()),
                        f.getName() + " private final болуы керек");
            }
        }
    }
}
