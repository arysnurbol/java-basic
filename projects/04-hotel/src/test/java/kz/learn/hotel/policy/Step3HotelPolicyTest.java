package kz.learn.hotel.policy;

import kz.learn.hotel.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 3 — Singleton: HotelPolicy")
class Step3HotelPolicyTest {

    private final HotelPolicy policy = HotelPolicy.getInstance();

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    @Test
    @DisplayName("getInstance() әрқашан сол бір объектіні қайтарады")
    void sameInstance() {
        assertSame(HotelPolicy.getInstance(), HotelPolicy.getInstance());
    }

    @Test
    @DisplayName("сырттан жасау мүмкін емес: класс final, барлық конструктор private")
    void cannotInstantiate() {
        assertTrue(Modifier.isFinal(HotelPolicy.class.getModifiers()), "final — ұрпақ арқылы екінші дана жасалмасын");
        for (Constructor<?> c : HotelPolicy.class.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(c.getModifiers()), "конструктор private болуы керек: " + c);
        }
    }

    @Test
    @DisplayName("ережелердің мәндері")
    void values() {
        assertEquals(30, policy.maxNights());
        assertEquals(0, BigDecimal.valueOf(25).compareTo(policy.weekendSurchargePercent()));
        assertEquals(7, policy.longStayNights());
        assertEquals(0, BigDecimal.valueOf(10).compareTo(policy.longStayDiscountPercent()));
        assertEquals(7, policy.freeCancellationDays());
        assertEquals(0, BigDecimal.valueOf(50).compareTo(policy.lateCancellationRefundPercent()));
    }

    @Test
    @DisplayName("isWeekend: жұма мен сенбі түндері ғана")
    void weekend() {
        assertFalse(policy.isWeekend(oct(8)), "бейсенбі");
        assertTrue(policy.isWeekend(oct(9)), "жұма");
        assertTrue(policy.isWeekend(oct(10)), "сенбі");
        assertFalse(policy.isWeekend(oct(11)), "жексенбі түні — ертең жұмыс");
        assertFalse(policy.isWeekend(oct(12)), "дүйсенбі");
    }

    @Test
    @DisplayName("refundFor: >= 7 күн — толық, 1–6 — жартысы, келу күні және кейін — 0")
    void refund() {
        Money paid = Money.of(100_000);
        LocalDate checkIn = oct(20);

        assertEquals(paid, policy.refundFor(paid, oct(1), checkIn), "19 күн");
        assertEquals(paid, policy.refundFor(paid, oct(13), checkIn), "дәл 7 күн — шекара кіреді");
        assertEquals(Money.of(50_000), policy.refundFor(paid, oct(14), checkIn), "6 күн");
        assertEquals(Money.of(50_000), policy.refundFor(paid, oct(19), checkIn), "1 күн");
        assertEquals(Money.ZERO, policy.refundFor(paid, oct(20), checkIn), "келу күні");
        assertEquals(Money.ZERO, policy.refundFor(paid, oct(22), checkIn), "келу күні өтіп кетті");
    }

    @Test
    @DisplayName("refundFor: жартысы HALF_EVEN-мен дөңгелектенеді")
    void refundRounding() {
        assertEquals(Money.of("16666.66"), policy.refundFor(Money.of("33333.33"), oct(18), oct(20)),
                "16666.665 -> 16666.66 (ең жақын жұп)");
    }
}
