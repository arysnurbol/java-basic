package kz.learn.hotel.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 1 — DateRange: жартылай ашық интервал [checkIn, checkOut)")
class Step1DateRangeTest {

    private static LocalDate oct(int day) {
        return LocalDate.of(2026, 10, day);
    }

    private static DateRange range(int from, int to) {
        return new DateRange(oct(from), oct(to));
    }

    @Test
    @DisplayName("DateRange — record")
    void isRecord() {
        assertTrue(DateRange.class.isRecord());
        assertEquals(range(10, 13), range(10, 13), "record equals тегін");
        assertEquals("2026-10-10..2026-10-13", range(10, 13).toString());
    }

    @Test
    @DisplayName("nights: кету күні саналмайды; ай мен жыл шекарасы")
    void nights() {
        assertEquals(3, range(10, 13).nights());
        assertEquals(1, range(10, 11).nights());
        assertEquals(3, new DateRange(oct(30), LocalDate.of(2026, 11, 2)).nights(), "ай шекарасы");
        assertEquals(2, new DateRange(LocalDate.of(2028, 2, 28), LocalDate.of(2028, 3, 1)).nights(), "кібісе жыл: 29-ақпан бар");
        assertEquals(2, new DateRange(LocalDate.of(2026, 12, 31), LocalDate.of(2027, 1, 2)).nights(), "жыл шекарасы");
    }

    @Test
    @DisplayName("тексеріс: checkOut <= checkIn -> IllegalArgumentException; null -> NullPointerException")
    void validation() {
        assertEquals("checkOut must be after checkIn",
                assertThrows(IllegalArgumentException.class, () -> range(10, 10)).getMessage(), "0 түн — бронь емес");
        assertThrows(IllegalArgumentException.class, () -> range(13, 10));
        assertThrows(NullPointerException.class, () -> new DateRange(null, oct(10)));
        assertThrows(NullPointerException.class, () -> new DateRange(oct(10), null));
    }

    @Test
    @DisplayName("of(checkIn, nights): статикалық фабрика")
    void of() {
        assertEquals(range(10, 13), DateRange.of(oct(10), 3));
        assertEquals(new DateRange(oct(31), LocalDate.of(2026, 11, 1)), DateRange.of(oct(31), 1));
        assertEquals("nights must be positive",
                assertThrows(IllegalArgumentException.class, () -> DateRange.of(oct(10), 0)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> DateRange.of(oct(10), -1));
    }

    @Test
    @DisplayName("contains: келу күні кіреді, кету күні — жоқ")
    void contains() {
        DateRange stay = range(10, 13);

        assertFalse(stay.contains(oct(9)));
        assertTrue(stay.contains(oct(10)), "келу күнінің түні — бронь ішінде");
        assertTrue(stay.contains(oct(12)));
        assertFalse(stay.contains(oct(13)), "кету күні — бөлме бос");
    }

    @Test
    @DisplayName("overlaps: ортақ түн болса ғана; іргелес кезеңдер қиылыспайды")
    void overlaps() {
        DateRange stay = range(10, 13);

        assertTrue(stay.overlaps(range(10, 13)), "дәл сол кезең");
        assertTrue(stay.overlaps(range(12, 15)), "оң жақтан");
        assertTrue(stay.overlaps(range(8, 11)), "сол жақтан");
        assertTrue(stay.overlaps(range(11, 12)), "ішінде");
        assertTrue(stay.overlaps(range(9, 14)), "толық жауып тұр");

        assertFalse(stay.overlaps(range(13, 15)), "біреуі 13-і кетеді, біреуі 13-і келеді");
        assertFalse(stay.overlaps(range(7, 10)), "іргелес сол жақтан");
        assertFalse(stay.overlaps(range(20, 25)));
    }

    @Test
    @DisplayName("overlaps симметриялы: a.overlaps(b) == b.overlaps(a)")
    void overlapsIsSymmetric() {
        List<DateRange> ranges = List.of(range(10, 13), range(12, 15), range(13, 15), range(11, 12), range(1, 30));
        for (DateRange a : ranges) {
            for (DateRange b : ranges) {
                assertEquals(a.overlaps(b), b.overlaps(a), a + " vs " + b);
            }
        }
    }

    @Test
    @DisplayName("nightDates: барлық түн ретімен, кету күнісіз")
    void nightDates() {
        assertEquals(List.of(oct(10), oct(11), oct(12)), range(10, 13).nightDates());
        assertEquals(List.of(oct(31), LocalDate.of(2026, 11, 1)),
                new DateRange(oct(31), LocalDate.of(2026, 11, 2)).nightDates());
    }
}
