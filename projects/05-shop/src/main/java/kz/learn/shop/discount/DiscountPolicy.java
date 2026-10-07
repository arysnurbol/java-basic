package kz.learn.shop.discount;

import kz.learn.shop.model.Money;

/**
 * Жеңілдік — STRATEGY (банктегі FeePolicy сияқты). Функционалдық интерфейс: лямбда да жарайды.
 *
 * Келісім (контракт): нәтиже 0-ден subtotal-ға дейін — жеңілдік ешқашан тауар сомасынан асып,
 * тапсырысты «теріс» етпейді.
 */
@FunctionalInterface
public interface DiscountPolicy {

    /** subtotal — жеңілдікке дейінгі тауар сомасы. Қайтарады: жеңілдік сомасы (шегерілетін ақша). */
    Money discountFor(Money subtotal);

    /** Жеңілдік жоқ: әрқашан Money.ZERO. Кеңес: бір жолдық лямбда (FeePolicy.none() сияқты). */
    static DiscountPolicy none() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
