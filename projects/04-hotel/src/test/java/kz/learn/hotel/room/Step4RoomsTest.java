package kz.learn.hotel.room;

import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 4 — Room иерархиясы және Factory")
class Step4RoomsTest {

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    private static DateRange range(int from, int to) {
        return new DateRange(oct(from), oct(to));
    }

    @Test
    @DisplayName("Room — абстрактілі; ұрпақтар мен олардың конструкторлары public емес")
    void structure() {
        assertTrue(Modifier.isAbstract(Room.class.getModifiers()));
        for (Class<?> c : List.of(StandardRoom.class, DeluxeRoom.class, Suite.class)) {
            assertTrue(Room.class.isAssignableFrom(c));
            assertFalse(Modifier.isPublic(c.getModifiers()), c.getSimpleName() + " public болмауы керек");
            for (Constructor<?> ctor : c.getDeclaredConstructors()) {
                assertFalse(Modifier.isPublic(ctor.getModifiers()), "тек RoomFactory арқылы: " + ctor);
            }
        }
    }

    @Test
    @DisplayName("RoomFactory.create(type, number): түрге сәйкес класс")
    void factoryByType() {
        Room standard = RoomFactory.create(RoomType.STANDARD, "101");
        Room deluxe = RoomFactory.create(RoomType.DELUXE, "201");
        Room suite = RoomFactory.create(RoomType.SUITE, "301");

        assertInstanceOf(StandardRoom.class, standard);
        assertInstanceOf(DeluxeRoom.class, deluxe);
        assertInstanceOf(Suite.class, suite);

        assertEquals("101", standard.getId());
        assertEquals(RoomType.DELUXE, deluxe.getType());
        assertEquals(4, suite.capacity());
        assertEquals(Money.of(60_000), suite.basePrice());
        assertEquals("101 STANDARD (2 guests, 20000.00 ₸/night)", standard.toString());
    }

    @Test
    @DisplayName("RoomFactory.create(\"deluxe\", ...): мәтіннен, регистрге қарамай; нөмір strip")
    void factoryByName() {
        Room room = RoomFactory.create(" Deluxe ", " 201 ");

        assertInstanceOf(DeluxeRoom.class, room);
        assertEquals("201", room.getId());
        assertInstanceOf(Suite.class, RoomFactory.create("suite", "301"));
    }

    @Test
    @DisplayName("RoomFactory: белгісіз түр, бос нөмір, null — әрқайсысы өз қатесімен")
    void factoryValidation() {
        assertEquals("Unknown room type: penthouse", assertThrows(IllegalArgumentException.class,
                () -> RoomFactory.create("penthouse", "1")).getMessage());
        assertEquals("number must not be blank", assertThrows(IllegalArgumentException.class,
                () -> RoomFactory.create("suite", " ")).getMessage(), "бос нөмір «Unknown room type» болып кетпесін");
        assertThrows(NullPointerException.class, () -> RoomFactory.create((RoomType) null, "1"));
    }

    @Test
    @DisplayName("Standard: әр түн бірдей")
    void standardPrice() {
        Room room = RoomFactory.create(RoomType.STANDARD, "101");

        assertEquals(Money.of(20_000), room.priceForNight(oct(10)));
        assertEquals(Money.of(60_000), room.priceFor(range(8, 11)));
    }

    @Test
    @DisplayName("Deluxe: жұма мен сенбі түндері +25%")
    void deluxePrice() {
        Room room = RoomFactory.create(RoomType.DELUXE, "201");

        assertEquals(Money.of(35_000), room.priceForNight(oct(8)), "бейсенбі");
        assertEquals(Money.of(43_750), room.priceForNight(oct(9)), "жұма");
        assertEquals(Money.of(43_750), room.priceForNight(oct(10)), "сенбі");
        assertEquals(Money.of(35_000), room.priceForNight(oct(11)), "жексенбі");
        assertEquals(Money.of(122_500), room.priceFor(range(8, 11)), "бс + жм + сн = 35 000 + 43 750 + 43 750");
    }

    @Test
    @DisplayName("Suite: 7 түн және одан көп — бүкіл сомаға 10% жеңілдік")
    void suitePrice() {
        Room room = RoomFactory.create(RoomType.SUITE, "301");

        assertEquals(Money.of(60_000), room.priceForNight(oct(10)), "демалыс үстемесі жоқ");
        assertEquals(Money.of(360_000), room.priceFor(range(5, 11)), "6 түн — жеңілдіксіз");
        assertEquals(Money.of(378_000), room.priceFor(range(5, 12)), "7 түн: 420 000 - 10%");
    }

    @Test
    @DisplayName("Room тексерісі және equals/hashCode — тек нөмір бойынша")
    void equality() {
        assertThrows(IllegalArgumentException.class, () -> RoomFactory.create(RoomType.STANDARD, null));

        Room a = RoomFactory.create(RoomType.STANDARD, "101");
        Room same = RoomFactory.create(RoomType.STANDARD, " 101 ");
        assertEquals(a, same);
        assertEquals(a.hashCode(), same.hashCode());
        assertNotEquals(a, RoomFactory.create(RoomType.STANDARD, "102"));
    }
}
