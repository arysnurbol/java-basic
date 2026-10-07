package kz.learn.shop.exception;

/**
 * Промокод жарамсыз.
 *
 * getMessage(): "Invalid promo code: SALE99"
 */
public class InvalidPromoCodeException extends ShopException {

    // TODO: өрістерді жаз

    public InvalidPromoCodeException(String code) {
        super("TODO");
    }

    public String getCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
