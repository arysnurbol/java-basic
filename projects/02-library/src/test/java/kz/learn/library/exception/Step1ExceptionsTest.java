package kz.learn.library.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 1 — exception иерархиясы")
class Step1ExceptionsTest {

    @Test
    @DisplayName("LibraryException — unchecked, хабарламаны сақтайды")
    void base() {
        LibraryException e = new LibraryException("boom");

        assertInstanceOf(RuntimeException.class, e);
        assertEquals("boom", e.getMessage());
    }

    @Test
    @DisplayName("NotFoundException: \"Item not found: 5\", \"Member not found: S-1\"")
    void notFound() {
        NotFoundException item = new NotFoundException("Item", 5L);
        assertEquals("Item not found: 5", item.getMessage());
        assertEquals("Item", item.getEntity());
        assertEquals(5L, item.getId());

        NotFoundException member = new NotFoundException("Member", "S-1");
        assertEquals("Member not found: S-1", member.getMessage());
        assertEquals("S-1", member.getId());
    }

    @Test
    @DisplayName("ItemNotAvailableException: \"Item is already on loan: id=3\"")
    void notAvailable() {
        ItemNotAvailableException e = new ItemNotAvailableException(3);

        assertEquals("Item is already on loan: id=3", e.getMessage());
        assertEquals(3, e.getItemId());
    }

    @Test
    @DisplayName("LoanLimitExceededException: \"Loan limit exceeded: S-1 (max 3)\"")
    void limit() {
        LoanLimitExceededException e = new LoanLimitExceededException("S-1", 3);

        assertEquals("Loan limit exceeded: S-1 (max 3)", e.getMessage());
        assertEquals("S-1", e.getCardNumber());
        assertEquals(3, e.getLimit());
    }

    @Test
    @DisplayName("барлығы LibraryException — бір catch бәрін ұстайды")
    void hierarchy() {
        RuntimeException[] all = {
                new NotFoundException("Item", 1L),
                new ItemNotAvailableException(1),
                new LoanLimitExceededException("S-1", 3)
        };
        for (RuntimeException e : all) {
            assertInstanceOf(LibraryException.class, e, e.getClass().getSimpleName());
        }
    }

    @Test
    @DisplayName("өрістер private final — exception immutable")
    void immutable() {
        for (Class<?> c : new Class<?>[]{NotFoundException.class, ItemNotAvailableException.class,
                LoanLimitExceededException.class}) {
            Field[] fields = c.getDeclaredFields();
            assertTrue(fields.length > 0, c.getSimpleName() + ": өрістер жоқ");
            for (Field f : fields) {
                int m = f.getModifiers();
                assertTrue(Modifier.isPrivate(m) && Modifier.isFinal(m),
                        c.getSimpleName() + "." + f.getName() + " private final болуы керек");
            }
        }
    }
}
