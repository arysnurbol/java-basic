package kz.learn.library.model;

import kz.learn.library.exception.LibraryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 5 — Loan: мерзім, кешігу, айыппұл (екі иерархия бірге)")
class Step5LoanTest {

    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);

    private final Item book = new Book(1, "Clean Code", "Robert Martin");   // 14 күн, 50 ₸
    private final Item dvd = new Dvd(2, "Interstellar", 169);               // 3 күн, 200 ₸
    private final Member student = new StudentMember("S-1", "Aru");         // -50%
    private final Member regular = new RegularMember("R-1", "Daniyar");

    @Test
    @DisplayName("конструктор: dueDate = loanDate + item.loanDays(), returnDate == null")
    void constructor() {
        Loan l = new Loan(7, book, student, DAY);

        assertEquals(7L, l.getId());
        assertEquals(book, l.getItem());
        assertEquals(student, l.getMember());
        assertEquals(DAY, l.getLoanDate());
        assertEquals(DAY.plusDays(14), l.getDueDate());
        assertNull(l.getReturnDate());
        assertFalse(l.isReturned());
        assertEquals(DAY.plusDays(3), new Loan(8, dvd, student, DAY).getDueDate());
    }

    @Test
    @DisplayName("null item/member/loanDate -> NullPointerException")
    void nulls() {
        assertThrows(NullPointerException.class, () -> new Loan(1, null, student, DAY));
        assertThrows(NullPointerException.class, () -> new Loan(1, book, null, DAY));
        assertThrows(NullPointerException.class, () -> new Loan(1, book, student, null));
    }

    @Test
    @DisplayName("overdueDays: мерзімге дейін 0, кейін өседі, ешқашан теріс емес")
    void overdueDays() {
        Loan l = new Loan(1, book, regular, DAY);   // due = 10-15

        assertEquals(0, l.overdueDays(DAY));
        assertEquals(0, l.overdueDays(DAY.plusDays(14)));
        assertEquals(1, l.overdueDays(DAY.plusDays(15)));
        assertEquals(6, l.overdueDays(DAY.plusDays(20)));
        assertFalse(l.isOverdue(DAY.plusDays(14)));
        assertTrue(l.isOverdue(DAY.plusDays(15)));
    }

    @Test
    @DisplayName("fine: күн * item.dailyFine(), содан member жеңілдігі")
    void fine() {
        LocalDate fourDaysLate = DAY.plusDays(3 + 4);

        assertEquals(800, new Loan(1, dvd, regular, DAY).fine(fourDaysLate));   // 4 * 200
        assertEquals(400, new Loan(2, dvd, student, DAY).fine(fourDaysLate));   // 4 * 200 / 2
        assertEquals(0, new Loan(3, book, regular, DAY).fine(fourDaysLate));    // кітап әлі кешікпеді
        assertEquals(25, new Loan(4, book, student, DAY).fine(DAY.plusDays(15))); // 1 * 50 / 2
    }

    @Test
    @DisplayName("markReturned: қайтарылғаннан кейін кешігу returnDate бойынша қатып қалады")
    void returned() {
        Loan l = new Loan(1, dvd, regular, DAY);   // due = 10-04
        l.markReturned(DAY.plusDays(5));             // 2 күн кешікті

        assertTrue(l.isReturned());
        assertEquals(DAY.plusDays(5), l.getReturnDate());
        assertEquals(2, l.overdueDays(DAY.plusDays(100)), "today емес, returnDate есептеледі");
        assertEquals(400, l.fine(DAY.plusDays(100)));
        assertFalse(l.isOverdue(DAY.plusDays(100)), "қайтарылған Loan кешігіп жатқан жоқ");
    }

    @Test
    @DisplayName("markReturned: екі рет немесе loanDate-тен бұрын -> LibraryException")
    void returnRules() {
        Loan l = new Loan(1, book, regular, DAY);

        assertThrows(LibraryException.class, () -> l.markReturned(DAY.minusDays(1)));
        assertFalse(l.isReturned());

        l.markReturned(DAY);
        LibraryException e = assertThrows(LibraryException.class, () -> l.markReturned(DAY.plusDays(1)));
        assertEquals("Loan already returned: id=1", e.getMessage());
        assertEquals(DAY, l.getReturnDate());
    }

    @Test
    @DisplayName("toString")
    void text() {
        Loan l = new Loan(1, book, student, DAY);
        assertEquals("L1: #1 Book: Clean Code, Robert Martin -> S-1, due 2026-10-15", l.toString());

        l.markReturned(DAY.plusDays(20));
        assertTrue(l.toString().endsWith(", returned 2026-10-21"), l.toString());
    }
}
