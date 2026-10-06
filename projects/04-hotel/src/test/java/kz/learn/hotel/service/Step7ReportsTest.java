package kz.learn.hotel.service;

import kz.learn.hotel.model.Booking;
import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Money;
import kz.learn.hotel.repository.InMemoryRepository;
import kz.learn.hotel.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Қадам 7 — есептер: қонақтың брондары, толу, табыс, келушілер")
class Step7ReportsTest {

    private HotelService hotel;

    @BeforeEach
    void setUp() {
        hotel = newHotel();
        hotel.addRoom(RoomType.STANDARD, "101");
        hotel.addRoom(RoomType.STANDARD, "102");
        hotel.addRoom(RoomType.DELUXE, "201");
        hotel.addRoom(RoomType.SUITE, "301");
    }

    private static HotelService newHotel() {
        return new HotelService(new InMemoryRepository<>(), new InMemoryRepository<>(),
                new TestClock(LocalDateTime.of(2026, 10, 6, 10, 0)));
    }

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    private static DateRange range(int from, int to) {
        return new DateRange(oct(from), oct(to));
    }

    private static List<String> ids(List<Booking> list) {
        return list.stream().map(Booking::getId).toList();
    }

    @Test
    @DisplayName("bookingsOf: регистрге қарамай, болдырылмағандары да, келу күні бойынша")
    void bookingsOf() {
        hotel.book("101", "Aru", 1, range(20, 22));       // B0001
        hotel.book("102", "Daniyar", 1, range(10, 12));   // B0002
        hotel.book("201", "aru", 1, range(10, 11));       // B0003
        hotel.book("301", "ARU", 1, range(20, 21));       // B0004
        hotel.cancel("B0003");

        assertEquals(List.of("B0003", "B0001", "B0004"), ids(hotel.bookingsOf(" Aru ")),
                "10-ы, сосын 20-сы (B0001 < B0004)");
        assertEquals(List.of(), hotel.bookingsOf("Nobody"));
    }

    @Test
    @DisplayName("occupancyPercent: белсенді брондар ғана, кету күні бос")
    void occupancy() {
        hotel.book("101", "A", 1, range(10, 13));
        hotel.book("201", "B", 1, range(11, 12));
        hotel.book("301", "C", 1, range(10, 11));
        hotel.cancel("B0003");

        assertEquals(25, hotel.occupancyPercent(oct(10)), "101 ғана (301 болдырылмады)");
        assertEquals(50, hotel.occupancyPercent(oct(11)), "101 + 201");
        assertEquals(25, hotel.occupancyPercent(oct(12)));
        assertEquals(0, hotel.occupancyPercent(oct(13)), "13-і — кету күні");
    }

    @Test
    @DisplayName("occupancyPercent: бүтін бөлігі; бөлме жоқ — 0")
    void occupancyEdgeCases() {
        HotelService small = newHotel();
        assertEquals(0, small.occupancyPercent(oct(10)), "нөлге бөлу жоқ");

        small.addRoom(RoomType.STANDARD, "1");
        small.addRoom(RoomType.STANDARD, "2");
        small.addRoom(RoomType.STANDARD, "3");
        small.book("1", "A", 1, range(10, 11));
        assertEquals(33, small.occupancyPercent(oct(10)), "100 / 3 = 33");
    }

    @Test
    @DisplayName("revenueByType: EnumMap, тек броні бар түрлер, болдырылмағаннан қалған ақша да")
    void revenueByType() {
        hotel.book("101", "A", 1, range(10, 13));   // 60 000
        hotel.book("102", "B", 1, range(10, 11));   // 20 000
        hotel.book("201", "C", 1, range(8, 9));     // 35 000 (бейсенбі), 2 күн бұрын болдырмау -> 17 500 қалады
        hotel.cancel("B0003");

        Map<RoomType, Money> revenue = hotel.revenueByType();

        assertInstanceOf(EnumMap.class, revenue);
        assertEquals(List.of(RoomType.STANDARD, RoomType.DELUXE), List.copyOf(revenue.keySet()));
        assertEquals(Money.of(80_000), revenue.get(RoomType.STANDARD));
        assertEquals(Money.of(17_500), revenue.get(RoomType.DELUXE));

        hotel.book("301", "D", 1, range(20, 21));
        hotel.cancel("B0004");                      // 14 күн бұрын — толық қайтарылады
        assertEquals(Money.ZERO, hotel.revenueByType().get(RoomType.SUITE));
        assertEquals(Map.of(), newHotel().revenueByType());
    }

    @Test
    @DisplayName("checkInsOn: осы күні келетін белсенді брондар, бөлме нөмірі бойынша")
    void checkInsOn() {
        hotel.book("201", "A", 1, range(10, 12));   // B0001
        hotel.book("101", "B", 1, range(10, 11));   // B0002
        hotel.book("102", "C", 1, range(9, 11));    // B0003 — 9-ы келеді
        hotel.book("301", "D", 1, range(10, 11));   // B0004
        hotel.cancel("B0004");

        assertEquals(List.of("B0002", "B0001"), ids(hotel.checkInsOn(oct(10))), "101, 201; 301 болдырылмады");
        assertEquals(List.of("B0003"), ids(hotel.checkInsOn(oct(9))));
        assertEquals(List.of(), hotel.checkInsOn(oct(11)));
    }
}
