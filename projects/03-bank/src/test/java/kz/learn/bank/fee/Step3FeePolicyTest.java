package kz.learn.bank.fee;

import kz.learn.bank.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 3 — Strategy: FeePolicy және оның жүзеге асырылулары")
class Step3FeePolicyTest {

    @Test
    @DisplayName("FeePolicy — функционалдық интерфейс")
    void functional() {
        assertTrue(FeePolicy.class.isAnnotationPresent(FunctionalInterface.class));
    }

    @Test
    @DisplayName("none(): кез келген сомаға комиссия 0")
    void none() {
        FeePolicy none = FeePolicy.none();
        assertEquals(Money.ZERO, none.feeFor(Money.of(1)));
        assertEquals(Money.ZERO, none.feeFor(Money.of(1_000_000)));
    }

    @Test
    @DisplayName("FixedFee: сомаға қарамай тұрақты комиссия; record")
    void fixed() {
        FeePolicy fee = new FixedFee(Money.of(100));

        assertTrue(FixedFee.class.isRecord());
        assertEquals(Money.of(100), fee.feeFor(Money.of(1)));
        assertEquals(Money.of(100), fee.feeFor(Money.of(1_000_000)));
        assertEquals(Money.ZERO, new FixedFee(Money.ZERO).feeFor(Money.of(500)), "0 рұқсат");
        assertEquals(new FixedFee(Money.of(100)), new FixedFee(Money.of("100.00")), "record equals тегін");
    }

    @Test
    @DisplayName("FixedFee: теріс немесе null -> exception")
    void fixedValidation() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new FixedFee(Money.of(-1)));
        assertEquals("fee must not be negative", e.getMessage());
        assertThrows(NullPointerException.class, () -> new FixedFee(null));
    }

    @Test
    @DisplayName("PercentFee: сомадан пайыз, бірақ min-нен кем емес")
    void percent() {
        FeePolicy fee = new PercentFee(new BigDecimal("1.5"), Money.of(200));

        assertTrue(PercentFee.class.isRecord());
        assertEquals(Money.of(1500), fee.feeFor(Money.of(100_000)));
        assertEquals(Money.of(200), fee.feeFor(Money.of(1000)), "1.5% = 15 < 200 -> min");
        assertEquals(Money.of(200), fee.feeFor(Money.of("13333.33")), "199.99995 -> 200.00");
        assertEquals(Money.of("4.99"), new PercentFee(new BigDecimal("0.5"), Money.ZERO).feeFor(Money.of(998)));
    }

    @Test
    @DisplayName("PercentFee: percent > 0, min >= 0, null -> NullPointerException")
    void percentValidation() {
        assertEquals("percent must be positive", assertThrows(IllegalArgumentException.class,
                () -> new PercentFee(BigDecimal.ZERO, Money.ZERO)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new PercentFee(new BigDecimal("-1"), Money.ZERO));
        assertEquals("min must not be negative", assertThrows(IllegalArgumentException.class,
                () -> new PercentFee(BigDecimal.ONE, Money.of(-1))).getMessage());
        assertThrows(NullPointerException.class, () -> new PercentFee(null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new PercentFee(BigDecimal.ONE, null));
    }

    @Test
    @DisplayName("жаңа стратегия үшін класс міндетті емес — лямбда да жарайды")
    void lambdaStrategy() {
        FeePolicy vip = amount -> amount.isGreaterThan(Money.of(100_000)) ? Money.ZERO : Money.of(50);

        assertEquals(Money.of(50), vip.feeFor(Money.of(100_000)));
        assertEquals(Money.ZERO, vip.feeFor(Money.of(100_001)));
    }
}
