package kz.learn.shop.exception;

/**
 * Дүкеннің барлық бизнес-қателерінің ата-класы (HotelException сияқты). Unchecked.
 */
public class ShopException extends RuntimeException {

    public ShopException(String message) {
        super(message);
    }
}
