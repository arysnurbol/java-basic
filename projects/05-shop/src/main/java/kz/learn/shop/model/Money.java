package kz.learn.shop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Ақша сомасы (теңгемен) — IMMUTABLE VALUE OBJECT. ДАЙЫН — 03-bank-та сен жазған Money.
 *
 * Еске сал: масштаб әрқашан 2, HALF_EVEN; әр операция ЖАҢА Money қайтарады;
 * объект тек Money.of(...) арқылы жасалады; салыстыру — compareTo, ешқашан ==.
 */
public final class Money implements Comparable<Money> {

    private static final int SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount");
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public BigDecimal amount() {
        return amount;
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other");
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "other");
        return new Money(amount.subtract(other.amount));
    }

    /** ЖАҢА (04-hotel-да жоқ еді): amount * n. Money.of(1500).times(3) -> 4500.00 */
    public Money times(int n) {
        return new Money(amount.multiply(BigDecimal.valueOf(n)));
    }

    public Money negate() {
        return new Money(amount.negate());
    }

    /** amount * percent / 100, HALF_EVEN-мен 2 таңбаға: Money.of(1000).percent(new BigDecimal("1.5")) -> 15.00 */
    public Money percent(BigDecimal percent) {
        Objects.requireNonNull(percent, "percent");
        return new Money(amount.multiply(percent).divide(HUNDRED));
    }

    /** Екеуінің үлкені (тең болса — this). */
    public Money max(Money other) {
        Objects.requireNonNull(other, "other");
        return compareTo(other) >= 0 ? this : other;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    /** this > other (қатаң). */
    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "other");
        return amount.compareTo(other.amount) > 0;
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    /** "1234.50 ₸" */
    @Override
    public String toString() {
        return amount.toPlainString() + " ₸";
    }
}
