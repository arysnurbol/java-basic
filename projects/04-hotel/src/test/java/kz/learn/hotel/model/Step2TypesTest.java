package kz.learn.hotel.model;

import kz.learn.hotel.exception.BookingNotFoundException;
import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.exception.RoomNotAvailableException;
import kz.learn.hotel.exception.RoomNotFoundException;
import kz.learn.hotel.room.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 2 — RoomType (өрісі бар enum) және exception-дар")
class Step2TypesTest {

    @Test
    @DisplayName("RoomType: сыйымдылық және базалық баға")
    void roomType() {
        assertEquals(List.of(RoomType.STANDARD, RoomType.DELUXE, RoomType.SUITE), List.of(RoomType.values()));

        assertEquals(2, RoomType.STANDARD.capacity());
        assertEquals(3, RoomType.DELUXE.capacity());
        assertEquals(4, RoomType.SUITE.capacity());

        assertEquals(Money.of(20_000), RoomType.STANDARD.basePrice());
        assertEquals(Money.of(35_000), RoomType.DELUXE.basePrice());
        assertEquals(Money.of(60_000), RoomType.SUITE.basePrice());
    }

    @Test
    @DisplayName("exception иерархиясы: бәрі HotelException, ол — unchecked")
    void hierarchy() {
        assertTrue(RuntimeException.class.isAssignableFrom(HotelException.class));
        assertTrue(HotelException.class.isAssignableFrom(RoomNotFoundException.class));
        assertTrue(HotelException.class.isAssignableFrom(BookingNotFoundException.class));
        assertTrue(HotelException.class.isAssignableFrom(RoomNotAvailableException.class));
        assertEquals("boom", new HotelException("boom").getMessage());
    }

    @Test
    @DisplayName("хабарламалар мен getter-лер")
    void messages() {
        RoomNotFoundException room = new RoomNotFoundException("101");
        assertEquals("Room not found: 101", room.getMessage());
        assertEquals("101", room.getNumber());

        BookingNotFoundException booking = new BookingNotFoundException("B0001");
        assertEquals("Booking not found: B0001", booking.getMessage());
        assertEquals("B0001", booking.getId());

        DateRange stay = new DateRange(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 13));
        RoomNotAvailableException busy = new RoomNotAvailableException("101", stay);
        assertEquals("Room 101 is not available for 2026-10-10..2026-10-13", busy.getMessage());
        assertEquals("101", busy.getNumber());
        assertEquals(stay, busy.getStay());
    }
}
