package kz.learn.hotel.exception;

import kz.learn.hotel.model.DateRange;

/**
 * Бөлме сол кезеңде бос емес (басқа белсенді броньмен қиылысады).
 *
 * getMessage() пішімі (DateRange.toString арқылы):
 *   "Room 101 is not available for 2026-10-10..2026-10-13"
 */
public class RoomNotAvailableException extends HotelException {

    private final String number;
    private final DateRange stay;

    public RoomNotAvailableException(String number, DateRange stay) {
        super("Room " + number + " is not available for " + stay);
        this.number = number;
        this.stay = stay;
    }

    public String getNumber() {
        return number;
    }

    public DateRange getStay() {
        return stay;
    }
}
