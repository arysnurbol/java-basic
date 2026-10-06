package kz.learn.hotel.exception;

/**
 * Қонақүйдің барлық бизнес-қателерінің ата-класы (BankException сияқты). Unchecked.
 */
public class HotelException extends RuntimeException {

    public HotelException(String message) {
        super(message);
    }
}
