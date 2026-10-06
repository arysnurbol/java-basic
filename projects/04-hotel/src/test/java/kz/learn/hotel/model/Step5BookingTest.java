package kz.learn.hotel.model;

import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.room.Room;
import kz.learn.hotel.room.RoomFactory;
import kz.learn.hotel.room.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 5 — Booking: тексеріс, бөгеу, болдырмау")
class Step5BookingTest {

    private final Room room = RoomFactory.create(RoomType.STANDARD, "101");

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    private static DateRange range(int from, int to) {
        return new DateRange(oct(from), oct(to));
    }

    private Booking booking() {
        return new Booking("B0001", room, "Aru", 2, range(10, 13));
    }

    @Test
    @DisplayName("конструктор: өрістер, баға брондау сәтінде есептеледі, күй CONFIRMED")
    void constructor() {
        Booking b = new Booking(" B0001 ", room, "  Aru ", 2, range(10, 13));

        assertEquals("B0001", b.getId());
        assertSame(room, b.getRoom());
        assertEquals("Aru", b.getGuest());
        assertEquals(2, b.getGuests());
        assertEquals(range(10, 13), b.getStay());
        assertEquals(Money.of(60_000), b.getTotal());
        assertEquals(BookingStatus.CONFIRMED, b.getStatus());
        assertTrue(b.isActive());
        assertEquals(Money.ZERO, b.getRefund());
        assertEquals("B0001: 101 STANDARD, Aru x2, 2026-10-10..2026-10-13, 60000.00 ₸, CONFIRMED", b.toString());
    }

    @Test
    @DisplayName("тексеріс: қонақ саны, сыйымдылық, ең ұзақ мерзім, бос мәтін, null")
    void validation() {
        assertEquals("guests must be positive", assertThrows(IllegalArgumentException.class,
                () -> new Booking("B1", room, "Aru", 0, range(10, 13))).getMessage());
        assertEquals("Room 101 fits at most 2 guests", assertThrows(IllegalArgumentException.class,
                () -> new Booking("B1", room, "Aru", 3, range(10, 13))).getMessage());
        assertEquals("Stay must not exceed 30 nights", assertThrows(IllegalArgumentException.class,
                () -> new Booking("B1", room, "Aru", 1, DateRange.of(oct(1), 31))).getMessage());
        assertEquals(30, new Booking("B1", room, "Aru", 1, DateRange.of(oct(1), 30)).getStay().nights(), "дәл 30 — рұқсат");

        assertEquals("guest must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Booking("B1", room, " ", 1, range(10, 13))).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new Booking(null, room, "Aru", 1, range(10, 13)));
        assertThrows(NullPointerException.class, () -> new Booking("B1", null, "Aru", 1, range(10, 13)));
        assertThrows(NullPointerException.class, () -> new Booking("B1", room, "Aru", 1, null));
    }

    @Test
    @DisplayName("blocks: белсенді бронь қиылысатын кезеңді бөгейді")
    void blocks() {
        Booking b = booking();

        assertTrue(b.blocks(range(12, 14)));
        assertFalse(b.blocks(range(13, 15)), "кету күні келесі қонақ келе алады");
        assertFalse(b.blocks(range(5, 10)));
    }

    @Test
    @DisplayName("cancel ертерек (>= 7 күн): толық қайтарылады, бөлме босайды")
    void cancelEarly() {
        Booking b = booking();
        Money refund = b.cancel(oct(3));

        assertEquals(Money.of(60_000), refund);
        assertEquals(refund, b.getRefund());
        assertEquals(BookingStatus.CANCELLED, b.getStatus());
        assertFalse(b.isActive());
        assertFalse(b.blocks(range(10, 13)), "болдырылмаған бронь бөлмені бөгемейді");
        assertEquals(Money.ZERO, b.revenue());
    }

    @Test
    @DisplayName("cancel кеш: 1–6 күн — жартысы; келу күні — ештеңе")
    void cancelLate() {
        Booking late = booking();
        assertEquals(Money.of(30_000), late.cancel(oct(8)));
        assertEquals(Money.of(30_000), late.revenue(), "қонақүйде жартысы қалады");

        Booking sameDay = booking();
        assertEquals(Money.ZERO, sameDay.cancel(oct(10)));
        assertEquals(Money.of(60_000), sameDay.revenue());
    }

    @Test
    @DisplayName("екінші рет болдырмау -> HotelException, ештеңе өзгермейді")
    void cancelTwice() {
        Booking b = booking();
        b.cancel(oct(8));

        HotelException e = assertThrows(HotelException.class, () -> b.cancel(oct(1)));
        assertEquals("Booking B0001 is already cancelled", e.getMessage());
        assertEquals(Money.of(30_000), b.getRefund(), "алғашқы қайтарым сол күйі");
    }

    @Test
    @DisplayName("revenue: белсенді бронь — толық сома")
    void revenueActive() {
        assertEquals(Money.of(60_000), booking().revenue());
    }

    @Test
    @DisplayName("equals/hashCode — тек id бойынша")
    void equality() {
        Booking a = booking();
        Booking same = new Booking("B0001", RoomFactory.create(RoomType.SUITE, "301"), "Other", 1, range(1, 2));

        assertEquals(a, same);
        assertEquals(a.hashCode(), same.hashCode());
        assertNotEquals(a, new Booking("B0002", room, "Aru", 2, range(10, 13)));
    }
}
