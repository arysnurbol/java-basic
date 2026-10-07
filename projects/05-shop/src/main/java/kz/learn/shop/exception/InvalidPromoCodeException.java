package kz.learn.shop.exception;

/**
 * Промокод жарамсыз.
 *
 * getMessage(): "Invalid promo code: SALE99"
 */
public class InvalidPromoCodeException extends ShopException {

    private final String code;

    public InvalidPromoCodeException(String code) {
        super("Invalid promo code: " + code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
