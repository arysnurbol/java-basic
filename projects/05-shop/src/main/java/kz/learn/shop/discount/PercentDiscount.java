package kz.learn.shop.discount;

import kz.learn.shop.model.Money;

import java.math.BigDecimal;

/**
 * Пайыздық жеңілдік: SALE10 -> 10%.
 *
 * Compact конструктор: percent 1..50 болмаса -> IllegalArgumentException("percent must be between 1 and 50").
 * discountFor: subtotal.percent(...) — дөңгелектеуді Money өзі жасайды.
 */
public record PercentDiscount(int percent) implements DiscountPolicy {

    public PercentDiscount {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Money discountFor(Money subtotal) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
