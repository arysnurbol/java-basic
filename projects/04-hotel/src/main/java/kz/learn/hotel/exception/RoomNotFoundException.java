package kz.learn.hotel.exception;

/**
 * Бөлме нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Room not found: 101"
 */
public class RoomNotFoundException extends HotelException {

    // TODO: өрісті жаз

    public RoomNotFoundException(String number) {
        super("TODO");
    }

    public String getNumber() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
