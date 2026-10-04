package kz.learn.library.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 3 — Member иерархиясы: abstract метод vs hook")
class Step3MembersTest {

    @Test
    @DisplayName("Member — abstract, Identifiable<String>")
    void structure() {
        assertTrue(Modifier.isAbstract(Member.class.getModifiers()), "Member abstract болуы керек");
        assertTrue(Identifiable.class.isAssignableFrom(Member.class));
    }

    @Test
    @DisplayName("конструктор: trim, getId() = билет нөмірі")
    void constructor() {
        Member m = new StudentMember(" S-1 ", "  Aru ");

        assertEquals("S-1", m.getId());
        assertEquals("Aru", m.getName());
        assertEquals("S-1 Aru (StudentMember)", m.toString());
    }

    @Test
    @DisplayName("бос билет нөмірі немесе аты -> IllegalArgumentException")
    void validation() {
        assertThrows(IllegalArgumentException.class, () -> new StudentMember(" ", "Aru"));
        assertThrows(IllegalArgumentException.class, () -> new RegularMember("R-1", null));
    }

    @Test
    @DisplayName("студент: 3 экземпляр, 50% жеңілдік (бүтін бөлу)")
    void student() {
        Member s = new StudentMember("S-1", "Aru");

        assertEquals(3, s.maxLoans());
        assertEquals(100, s.applyDiscount(200));
        assertEquals(37, s.applyDiscount(75));
        assertEquals(0, s.applyDiscount(0));
    }

    @Test
    @DisplayName("кәдімгі оқырман: 5 экземпляр, жеңілдік жоқ")
    void regular() {
        Member r = new RegularMember("R-1", "Daniyar");

        assertEquals(5, r.maxLoans());
        assertEquals(200, r.applyDiscount(200));
    }

    @Test
    @DisplayName("RegularMember applyDiscount-ты override ЕТПЕЙДІ — ата-кластікін қолданады")
    void hookNotOverridden() throws NoSuchMethodException {
        assertEquals(Member.class, RegularMember.class.getMethod("applyDiscount", long.class).getDeclaringClass());
        assertEquals(StudentMember.class, StudentMember.class.getMethod("applyDiscount", long.class).getDeclaringClass());
    }

    @Test
    @DisplayName("equals/hashCode — тек билет нөмірі бойынша")
    void equality() {
        Member a = new StudentMember("S-1", "Aru");
        Member sameCard = new RegularMember("S-1", "Other");
        Member otherCard = new StudentMember("S-2", "Aru");

        assertEquals(a, sameCard);
        assertEquals(a.hashCode(), sameCard.hashCode());
        assertNotEquals(a, otherCard);
        assertNotEquals(a, "S-1");
    }
}
