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
        if (percent < 1 || percent > 50) {
            throw new IllegalArgumentException("percent must be between 1 and 50");
        }
    }

    @Override
    public Money discountFor(Money subtotal) {
        return subtotal.percent(BigDecimal.valueOf(percent));
    }
}
