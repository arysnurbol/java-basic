package kz.learn.shop.discount;

import kz.learn.shop.exception.InvalidPromoCodeException;
import kz.learn.shop.model.Money;

import java.util.Locale;

/**
 * Промокодтан жеңілдік жасайтын FACTORY (RoomFactory сияқты): клиент мәтін береді,
 * ал қай класс жасалатынын фабрика шешеді. Даналары жоқ утилита-класс: final, private конструктор.
 *
 * Код алдымен strip() + toUpperCase(Locale.ROOT) арқылы қалыпқа келтіріледі (" sale10 " -> "SALE10"):
 *   null немесе бос        -> DiscountPolicy.none();
 *   "SALE<n>"              -> new PercentDiscount(n);
 *   "MINUS<n>"             -> new FixedDiscount(Money.of(n));
 *   басқа кез келген мәтін -> InvalidPromoCodeException(қалыпқа келген код).
 * Сан дұрыс болмаса ("SALE", "SALEabc") немесе record оны қабылдамаса ("SALE0", "SALE99", "MINUS-5") —
 * бұл да InvalidPromoCodeException(қалыпқа келген код).
 */
public final class DiscountFactory {

    private DiscountFactory() {
    }

    public static DiscountPolicy fromCode(String code) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
