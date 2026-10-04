package kz.learn.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 2 — Check утилитасы және Item иерархиясы")
class Step2ItemsTest {

    @Test
    @DisplayName("Check — final, private конструктор")
    void checkIsUtility() {
        assertTrue(Modifier.isFinal(Check.class.getModifiers()), "Check final болуы керек");
        for (Constructor<?> c : Check.class.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(c.getModifiers()), "конструктор private болуы керек");
        }
    }

    @Test
    @DisplayName("Check.text: trim, бос/null -> IllegalArgumentException атымен")
    void checkText() {
        assertEquals("Abai", Check.text("  Abai ", "name"));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Check.text("  ", "title"));
        assertEquals("title must not be blank", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> Check.text(null, "title"));
        assertThrows(IllegalArgumentException.class, () -> Check.text("", "title"));
    }

    @Test
    @DisplayName("Check.positive: > 0 өтеді, 0 және теріс -> IllegalArgumentException")
    void checkPositive() {
        assertEquals(5, Check.positive(5, "issue"));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Check.positive(0, "minutes"));
        assertEquals("minutes must be positive", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> Check.positive(-1, "minutes"));
    }

    @Test
    @DisplayName("Item — abstract, Identifiable<Long>, барлық өрістері private final (immutable)")
    void itemStructure() {
        assertTrue(Modifier.isAbstract(Item.class.getModifiers()), "Item abstract болуы керек");
        assertTrue(Identifiable.class.isAssignableFrom(Item.class));
        for (Class<?> c : List.of(Item.class, Book.class, Magazine.class, Dvd.class)) {
            Field[] fields = c.getDeclaredFields();
            assertTrue(fields.length > 0, c.getSimpleName() + ": өрістер жоқ");
            for (Field f : fields) {
                int m = f.getModifiers();
                assertTrue(Modifier.isPrivate(m) && Modifier.isFinal(m),
                        c.getSimpleName() + "." + f.getName() + " private final болуы керек");
            }
        }
    }

    @Test
    @DisplayName("Book: өрістер, trim, 14 күн, 50 ₸, describe")
    void book() {
        Book b = new Book(1, "  Clean Code ", " Robert Martin ");

        assertEquals(1L, b.getId());
        assertEquals("Clean Code", b.getTitle());
        assertEquals("Robert Martin", b.getAuthor());
        assertEquals(14, b.loanDays());
        assertEquals(50, b.dailyFine());
        assertEquals("#1 Book: Clean Code, Robert Martin", b.describe());
        assertEquals(b.describe(), b.toString());
    }

    @Test
    @DisplayName("Magazine: 7 күн, 20 ₸, describe")
    void magazine() {
        Magazine m = new Magazine(2, "Forbes", 12);

        assertEquals(12, m.getIssue());
        assertEquals(7, m.loanDays());
        assertEquals(20, m.dailyFine());
        assertEquals("#2 Magazine: Forbes, No 12", m.describe());
    }

    @Test
    @DisplayName("Dvd: 3 күн, 200 ₸, describe")
    void dvd() {
        Dvd d = new Dvd(3, "Interstellar", 169);

        assertEquals(169, d.getMinutes());
        assertEquals(3, d.loanDays());
        assertEquals(200, d.dailyFine());
        assertEquals("#3 Dvd: Interstellar, 169 min", d.describe());
    }

    @Test
    @DisplayName("қате дерек -> IllegalArgumentException")
    void validation() {
        assertThrows(IllegalArgumentException.class, () -> new Book(1, " ", "A"));
        assertThrows(IllegalArgumentException.class, () -> new Book(1, "T", null));
        assertThrows(IllegalArgumentException.class, () -> new Magazine(1, "T", 0));
        assertThrows(IllegalArgumentException.class, () -> new Dvd(1, "T", -5));
        assertThrows(IllegalArgumentException.class, () -> new Dvd(1, null, 90));
    }

    @Test
    @DisplayName("полиморфизм: Item ретінде қарасақ та, әрқайсысы өз ережесін береді")
    void polymorphism() {
        List<Item> all = List.of(new Book(1, "B", "A"), new Magazine(2, "M", 1), new Dvd(3, "D", 90));

        assertEquals(List.of(14, 7, 3), all.stream().map(Item::loanDays).toList());
        assertEquals(List.of(50L, 20L, 200L), all.stream().map(Item::dailyFine).toList());
    }

    @Test
    @DisplayName("equals/hashCode — тек id бойынша (тіпті түрі әртүрлі болса да)")
    void equality() {
        Item a = new Book(1, "A", "X");
        Item sameId = new Dvd(1, "Other", 90);
        Item otherId = new Book(2, "A", "X");

        assertEquals(a, sameId);
        assertEquals(a.hashCode(), sameId.hashCode());
        assertNotEquals(a, otherId);
        assertNotEquals(a, null);
    }
}
