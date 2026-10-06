package kz.learn.hotel.exception;

/**
 * Бронь нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Booking not found: B0001"
 */
public class BookingNotFoundException extends HotelException {

    // TODO: өрісті жаз

    public BookingNotFoundException(String id) {
        super("TODO");
    }

    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
