package kz.learn.hotel.exception;

import kz.learn.hotel.model.DateRange;

/**
 * Бөлме сол кезеңде бос емес (басқа белсенді броньмен қиылысады).
 *
 * getMessage() пішімі (DateRange.toString арқылы):
 *   "Room 101 is not available for 2026-10-10..2026-10-13"
 */
public class RoomNotAvailableException extends HotelException {

    // TODO: өрістерді жаз

    public RoomNotAvailableException(String number, DateRange stay) {
        super("TODO");
    }

    public String getNumber() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public DateRange getStay() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
