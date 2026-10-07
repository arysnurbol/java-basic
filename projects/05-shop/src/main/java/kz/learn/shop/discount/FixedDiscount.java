package kz.learn.shop.discount;

import kz.learn.shop.model.Money;

import java.util.Objects;

/**
 * Белгілі сомаға жеңілдік: MINUS5000 -> 5 000 ₸.
 *
 * Compact конструктор: amount null емес -> Objects.requireNonNull(amount, "amount");
 *                      amount <= 0 -> IllegalArgumentException("amount must be positive").
 * discountFor: amount, бірақ subtotal-дан аспайды (3 000 ₸-лік тапсырысқа 5 000 ₸ жеңілдік -> 3 000 ₸).
 */
public record FixedDiscount(Money amount) implements DiscountPolicy {

    public FixedDiscount {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Money discountFor(Money subtotal) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
