package kz.learn.bank.fee;

import kz.learn.bank.model.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Objects;

/**
 * Пайыздық комиссия, бірақ min-нен кем емес: 1.5%, кемі 200 ₸.
 */
public record PercentFee(BigDecimal percent, Money min) implements FeePolicy {

    /**
     * percent, min — null болмайды (NullPointerException).
     * percent <= 0 -> IllegalArgumentException("percent must be positive")  (кеңес: signum())
     * min < 0      -> IllegalArgumentException("min must not be negative")
     */
    public PercentFee {
        Objects.requireNonNull(percent, "percent");
        Objects.requireNonNull(min, "min");
        if (percent.signum() <= 0) {
            throw new IllegalArgumentException("percent must be positive");
        }
        if (min.isNegative()) {
            throw new IllegalArgumentException("min must not be negative");
        }
    }

    /** Кеңес: Money.percent(...) және Money.max(...) — бір жол. */
    @Override
    public Money feeFor(Money amount) {
        return amount.percent(percent).max(min);
    }
}
