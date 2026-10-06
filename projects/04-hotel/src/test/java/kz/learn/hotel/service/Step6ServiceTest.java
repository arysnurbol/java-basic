package kz.learn.hotel.service;

import kz.learn.hotel.exception.BookingNotFoundException;
import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.exception.RoomNotAvailableException;
import kz.learn.hotel.exception.RoomNotFoundException;
import kz.learn.hotel.model.Booking;
import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Money;
import kz.learn.hotel.repository.InMemoryRepository;
import kz.learn.hotel.repository.Repository;
import kz.learn.hotel.room.Room;
import kz.learn.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 6 — HotelService: бөлмелер, іздеу, брондау, болдырмау")
class Step6ServiceTest {

    /** 2026-10-06, сейсенбі. */
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 6, 10, 0);

    private Repository<Room, String> rooms;
    private Repository<Booking, String> bookings;
    private TestClock clock;
    private HotelService hotel;

    @BeforeEach
    void setUp() {
        rooms = new InMemoryRepository<>();
        bookings = new InMemoryRepository<>();
        clock = new TestClock(NOW);
        hotel = new HotelService(rooms, bookings, clock);
        hotel.addRoom(RoomType.STANDARD, "101");
        hotel.addRoom(RoomType.STANDARD, "102");
        hotel.addRoom(RoomType.DELUXE, "201");
        hotel.addRoom(RoomType.SUITE, "301");
    }

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    private static DateRange range(int from, int to) {
        return new DateRange(oct(from), oct(to));
    }

    private static List<String> ids(List<Room> list) {
        return list.stream().map(Room::getId).toList();
    }

    @Test
    @DisplayName("addRoom: Factory арқылы жасалып, репозиторийге сақталады; қайталанған нөмір -> HotelException")
    void addRoom() {
        assertEquals(List.of("101", "102", "201", "301"), ids(rooms.findAll()));
        assertEquals(RoomType.DELUXE, hotel.getRoom("201").getType());

        HotelException e = assertThrows(HotelException.class, () -> hotel.addRoom(RoomType.SUITE, " 101 "));
        assertEquals("Room already exists: 101", e.getMessage());
        assertEquals(RoomType.STANDARD, hotel.getRoom("101").getType(), "бар бөлме ауыстырылмады");
    }

    @Test
    @DisplayName("getRoom / getBooking: жоқ болса — өз exception-ы")
    void notFound() {
        assertEquals("999", assertThrows(RoomNotFoundException.class, () -> hotel.getRoom("999")).getNumber());
        assertEquals("B0404", assertThrows(BookingNotFoundException.class, () -> hotel.getBooking("B0404")).getId());
        assertThrows(RoomNotFoundException.class, () -> hotel.book("999", "Aru", 1, range(10, 12)));
    }

    @Test
    @DisplayName("book: нөмірлер B0001, B0002, ..., баға есептеледі, репозиторийге сақталады")
    void book() {
        Booking first = hotel.book("101", "Aru", 2, range(10, 13));
        Booking second = hotel.book("201", "Daniyar", 3, range(8, 11));

        assertEquals("B0001", first.getId());
        assertEquals("B0002", second.getId());
        assertEquals(Money.of(60_000), first.getTotal());
        assertEquals(Money.of(122_500), second.getTotal());
        assertSame(first, hotel.getBooking("B0001"));
        assertEquals(List.of(first, second), bookings.findAll());
    }

    @Test
    @DisplayName("book: қиылысатын кезең -> RoomNotAvailableException; іргелес кезең — рұқсат")
    void overlap() {
        hotel.book("101", "Aru", 2, range(10, 13));

        RoomNotAvailableException e = assertThrows(RoomNotAvailableException.class,
                () -> hotel.book("101", "Daniyar", 1, range(12, 14)));
        assertEquals("Room 101 is not available for 2026-10-12..2026-10-14", e.getMessage());

        assertEquals("B0002", hotel.book("101", "Daniyar", 1, range(13, 15)).getId(), "13-і кетеді / келеді");
        assertEquals("B0003", hotel.book("102", "Daniyar", 1, range(12, 14)).getId(), "басқа бөлме — бос");
    }

    @Test
    @DisplayName("book: өткен күнге болмайды, бүгінге болады")
    void past() {
        assertEquals("checkIn must not be in the past", assertThrows(IllegalArgumentException.class,
                () -> hotel.book("101", "Aru", 1, range(5, 7))).getMessage());
        assertEquals(oct(6), hotel.book("101", "Aru", 1, range(6, 7)).getStay().checkIn());
    }

    @Test
    @DisplayName("book: сәтсіз брондау нөмірді «жемейді» және ештеңе сақтамайды")
    void failureKeepsNumber() {
        hotel.book("101", "Aru", 2, range(10, 13));
        assertThrows(IllegalArgumentException.class, () -> hotel.book("102", "Aru", 5, range(10, 13)));
        assertThrows(RoomNotAvailableException.class, () -> hotel.book("101", "Aru", 1, range(11, 12)));
        assertThrows(IllegalArgumentException.class, () -> hotel.book("301", "Aru", 1, DateRange.of(oct(10), 31)));

        assertEquals(1, bookings.findAll().size());
        assertEquals("B0002", hotel.book("102", "Aru", 2, range(10, 13)).getId());
    }

    @Test
    @DisplayName("isAvailable: белсенді бронь бөгейді")
    void isAvailable() {
        hotel.book("101", "Aru", 2, range(10, 13));

        assertFalse(hotel.isAvailable("101", range(11, 12)));
        assertTrue(hotel.isAvailable("101", range(13, 14)));
        assertTrue(hotel.isAvailable("102", range(11, 12)));
        assertThrows(RoomNotFoundException.class, () -> hotel.isAvailable("999", range(11, 12)));
    }

    @Test
    @DisplayName("findAvailable: сыйымдылық + бос; осы кезеңнің бағасы бойынша, тең болса — нөмір бойынша")
    void findAvailable() {
        assertEquals(List.of("101", "102", "201", "301"), ids(hotel.findAvailable(range(20, 21), 1)),
                "101 мен 102 бағасы тең — нөмір бойынша");

        hotel.book("102", "Aru", 2, range(10, 12));
        assertEquals(List.of("101", "201", "301"), ids(hotel.findAvailable(range(9, 11), 2)),
                "40 000 < 87 500 (жм + сн) < 120 000");
        assertEquals(List.of("201", "301"), ids(hotel.findAvailable(range(9, 11), 3)));
        assertEquals(List.of(), hotel.findAvailable(range(9, 11), 5));
    }

    @Test
    @DisplayName("findAvailable: рет базалық бағамен емес, ОСЫ кезеңнің бағасымен")
    void findAvailableUsesStayPrice() {
        hotel.addRoom(RoomType.DELUXE, "202");
        hotel.addRoom(RoomType.SUITE, "302");
        hotel.book("101", "A", 1, range(8, 15));
        hotel.book("102", "B", 1, range(8, 15));
        hotel.book("201", "C", 1, range(8, 15));
        hotel.book("301", "D", 1, range(8, 15));

        // 7 түн, ішінде жм + сн: Deluxe 35 000*5 + 43 750*2 = 262 500; Suite 420 000 - 10% = 378 000
        assertEquals(List.of("202", "302"), ids(hotel.findAvailable(range(8, 15), 1)));
        assertEquals(Money.of(262_500), hotel.getRoom("202").priceFor(range(8, 15)));
    }

    @Test
    @DisplayName("cancel: бүгінгі күн Clock-тан алынады; бөлме қайта босайды")
    void cancel() {
        Booking b = hotel.book("101", "Aru", 2, range(10, 13));

        assertEquals(Money.of(30_000), hotel.cancel("B0001"), "4 күн қалды — жартысы");
        assertFalse(b.isActive());
        assertEquals("B0002", hotel.book("101", "Daniyar", 2, range(10, 13)).getId(), "бөлме бос");
        assertThrows(HotelException.class, () -> hotel.cancel("B0001"));
        assertThrows(BookingNotFoundException.class, () -> hotel.cancel("B0404"));
    }

    @Test
    @DisplayName("cancel: уақыт жылжыса — ереже де өзгереді (TestClock)")
    void cancelWithMovedClock() {
        hotel.book("101", "Aru", 2, range(20, 23));
        hotel.book("102", "Aru", 2, range(20, 23));

        clock.set(LocalDateTime.of(2026, 10, 13, 23, 59));
        assertEquals(Money.of(60_000), hotel.cancel("B0001"), "дәл 7 күн — толық");

        clock.set(LocalDateTime.of(2026, 10, 20, 0, 0));
        assertEquals(Money.ZERO, hotel.cancel("B0002"), "келу күні — ештеңе");
    }
}
