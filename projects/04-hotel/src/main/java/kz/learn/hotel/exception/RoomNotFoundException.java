package kz.learn.hotel.exception;

/**
 * Бөлме нөмірі бойынша табылмаса.
 *
 * getMessage() пішімі: "Room not found: 101"
 */
public class RoomNotFoundException extends HotelException {

    private final String number;

    public RoomNotFoundException(String number) {
        super("Room not found: " + number);
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
