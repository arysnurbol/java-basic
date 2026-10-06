package kz.learn.hotel.exception;

/**
 * Бронь нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Booking not found: B0001"
 */
public class BookingNotFoundException extends HotelException {

    private final String id;

    public BookingNotFoundException(String id) {
        super("Booking not found: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
