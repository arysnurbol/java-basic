package kz.learn.bank.fee;

import kz.learn.bank.model.Money;

import java.math.BigDecimal;

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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Кеңес: Money.percent(...) және Money.max(...) — бір жол. */
    @Override
    public Money feeFor(Money amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
