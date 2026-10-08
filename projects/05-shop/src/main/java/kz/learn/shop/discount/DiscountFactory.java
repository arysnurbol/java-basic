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
        if (code == null || code.isBlank()) {
            return DiscountPolicy.none();
        }
        String normalized = code.strip().toUpperCase(Locale.ROOT);
        try {
            if (normalized.startsWith("SALE")) {
                return new PercentDiscount(Integer.parseInt(normalized.substring("SALE".length())));
            }
            if (normalized.startsWith("MINUS")) {
                return new FixedDiscount(Money.of(Integer.parseInt(normalized.substring("MINUS".length()))));
            }
        } catch (IllegalArgumentException e) {
            // NumberFormatException ("SALEABC") те осында түседі — ол IllegalArgumentException-ның ұрпағы
            throw new InvalidPromoCodeException(normalized);
        }
        throw new InvalidPromoCodeException(normalized);
    }
}
