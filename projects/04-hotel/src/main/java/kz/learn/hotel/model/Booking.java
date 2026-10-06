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

    private final String id;
    private final Room room;
    private final String guest;
    private final int guests;
    private final DateRange stay;
    private final Money total;
    private BookingStatus status;
    private Money refund;

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
        this.id = requireText(id, "id");
        this.guest = requireText(guest, "guest");
        this.room = Objects.requireNonNull(room, "room");
        this.stay = Objects.requireNonNull(stay, "stay");
        if (guests < 1) {
            throw new IllegalArgumentException("guests must be positive");
        }
        if (guests > room.capacity()) {
            throw new IllegalArgumentException("Room " + room.getId() + " fits at most " + room.capacity() + " guests");
        }
        int maxNights = HotelPolicy.getInstance().maxNights();
        if (stay.nights() > maxNights) {
            throw new IllegalArgumentException("Stay must not exceed " + maxNights + " nights");
        }
        this.guests = guests;
        this.total = room.priceFor(stay);
        this.status = BookingStatus.CONFIRMED;
        this.refund = Money.ZERO;
    }

    /** Бронь нөмірі. */
    @Override
    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public String getGuest() {
        return guest;
    }

    public int getGuests() {
        return guests;
    }

    public DateRange getStay() {
        return stay;
    }

    public Money getTotal() {
        return total;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Money getRefund() {
        return refund;
    }

    /** status == CONFIRMED. */
    public boolean isActive() {
        return status == BookingStatus.CONFIRMED;
    }

    /**
     * Осы бронь other кезеңінде бөлмені бөгей ме: белсенді ЖӘНЕ кезеңдері қиылысады.
     * Болдырылмаған бронь ештеңені бөгемейді — бөлме қайта босайды.
     */
    public boolean blocks(DateRange other) {
        return isActive() && stay.overlaps(other);
    }

    /**
     * Болдырмау. today — бүгінгі күн (сервис оны Clock-тан береді).
     *   1) белсенді болмаса -> HotelException("Booking B0001 is already cancelled");
     *   2) refund = HotelPolicy.getInstance().refundFor(total, today, stay.checkIn());
     *   3) status = CANCELLED.
     * Қайтарады: refund.
     */
    public Money cancel(LocalDate today) {
        if (!isActive()) {
            throw new HotelException("Booking " + id + " is already cancelled");
        }
        refund = HotelPolicy.getInstance().refundFor(total, today, stay.checkIn());
        status = BookingStatus.CANCELLED;
        return refund;
    }

    /**
     * Қонақүйде қалатын ақша: total - refund.
     * Белсенді бронь үшін refund = 0, сондықтан бұл — total. if керек емес.
     */
    public Money revenue() {
        return total.minus(refund);
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
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** ДАЙЫН. "B0001: 101 STANDARD, Aru x2, 2026-10-10..2026-10-13, 60000.00 ₸, CONFIRMED" */
    @Override
    public String toString() {
        return getId() + ": " + getRoom().getId() + " " + getRoom().getType() + ", " + getGuest() + " x" + getGuests()
                + ", " + getStay() + ", " + getTotal() + ", " + getStatus();
    }
}
