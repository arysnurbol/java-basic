package kz.learn.hotel.model;

import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.policy.HotelPolicy;
import kz.learn.hotel.room.Room;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Бронь. Account сияқты — ӨЗГЕРМЕЛІ, бірақ инкапсуляцияланған: күйі тек cancel(...) арқылы өзгереді.
 *
 * Өрістер:
 *   id, room, guest, guests, stay, total — final;
 *   status — BookingStatus.CONFIRMED-тен басталады;
 *   refund — Money.ZERO-дан басталады (болдырмағанда қайтарылған ақша).
 *
 * total брондау сәтінде БІР РЕТ есептеледі және сақталады. Ертең баға өзгерсе де, қонақ
 * келісілген бағамен тұрады — сондықтан getTotal() әр жолы room.priceFor(...) шақырмайды.
 */
public class Booking implements Identifiable<String> {

    // TODO: өрістерді жаз

    /**
     * Тексеріс реті:
     *   id, guest — requireText(...);  room, stay — Objects.requireNonNull(x, "room");
     *   guests < 1               -> IllegalArgumentException("guests must be positive");
     *   guests > room.capacity() -> IllegalArgumentException("Room 101 fits at most 2 guests");
     *   stay.nights() > HotelPolicy.getInstance().maxNights()
     *                            -> IllegalArgumentException("Stay must not exceed 30 nights").
     * Содан total = room.priceFor(stay).
     */
    public Booking(String id, Room room, String guest, int guests, DateRange stay) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Бронь нөмірі. */
    @Override
    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Room getRoom() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getGuest() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getGuests() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public DateRange getStay() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getTotal() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public BookingStatus getStatus() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getRefund() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** status == CONFIRMED. */
    public boolean isActive() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Осы бронь other кезеңінде бөлмені бөгей ме: белсенді ЖӘНЕ кезеңдері қиылысады.
     * Болдырылмаған бронь ештеңені бөгемейді — бөлме қайта босайды.
     */
    public boolean blocks(DateRange other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Болдырмау. today — бүгінгі күн (сервис оны Clock-тан береді).
     *   1) белсенді болмаса -> HotelException("Booking B0001 is already cancelled");
     *   2) refund = HotelPolicy.getInstance().refundFor(total, today, stay.checkIn());
     *   3) status = CANCELLED.
     * Қайтарады: refund.
     */
    public Money cancel(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Қонақүйде қалатын ақша: total - refund.
     * Белсенді бронь үшін refund = 0, сондықтан бұл — total. if керек емес.
     */
    public Money revenue() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }

    /** Тек id бойынша. */
    @Override
    public boolean equals(Object o) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int hashCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. "B0001: 101 STANDARD, Aru x2, 2026-10-10..2026-10-13, 60000.00 ₸, CONFIRMED" */
    @Override
    public String toString() {
        return getId() + ": " + getRoom().getId() + " " + getRoom().getType() + ", " + getGuest() + " x" + getGuests()
                + ", " + getStay() + ", " + getTotal() + ", " + getStatus();
    }
}
