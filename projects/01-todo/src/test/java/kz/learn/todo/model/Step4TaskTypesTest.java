package kz.learn.todo.model;

import kz.learn.todo.exception.InvalidTaskException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 4 — SimpleTask, DeadlineTask: мұрагерлік және полиморфизм")
class Step4TaskTypesTest {

    private static final LocalDate DUE = LocalDate.of(2026, 10, 5);

    @Test
    @DisplayName("SimpleTask: ешқашан кешікпейді, details бос")
    void simpleTask() {
        Task t = new SimpleTask(1, "Call mom", Priority.MEDIUM);

        assertFalse(t.isOverdue(LocalDate.of(2100, 1, 1)));
        assertEquals("#1 [MEDIUM] Call mom (TODO)", t.describe());
    }

    @Test
    @DisplayName("DeadlineTask: dueDate сақталады, private final")
    void deadlineStructure() throws NoSuchFieldException {
        DeadlineTask t = new DeadlineTask(2, "Pay bills", Priority.LOW, DUE);
        assertEquals(DUE, t.getDueDate());

        Field f = DeadlineTask.class.getDeclaredField("dueDate");
        assertTrue(Modifier.isPrivate(f.getModifiers()) && Modifier.isFinal(f.getModifiers()),
                "dueDate private final болуы керек");
    }

    @Test
    @DisplayName("DeadlineTask: null dueDate -> InvalidTaskException")
    void deadlineValidation() {
        assertThrows(InvalidTaskException.class, () -> new DeadlineTask(2, "Pay", Priority.LOW, null));
        assertThrows(InvalidTaskException.class, () -> new DeadlineTask(2, " ", Priority.LOW, DUE),
                "ата-класс тексерісі де жұмыс істеуі керек");
    }

    @Test
    @DisplayName("DeadlineTask.isOverdue: мерзім күні — жоқ, келесі күні — иә, DONE — жоқ")
    void deadlineOverdue() {
        DeadlineTask t = new DeadlineTask(2, "Pay bills", Priority.LOW, DUE);

        assertFalse(t.isOverdue(DUE.minusDays(1)));
        assertFalse(t.isOverdue(DUE), "мерзім күнінің өзінде әлі кешікпеген");
        assertTrue(t.isOverdue(DUE.plusDays(1)));

        t.start();
        t.complete();
        assertFalse(t.isOverdue(DUE.plusDays(1)), "аяқталған тапсырма кешікпейді");
    }

    @Test
    @DisplayName("DeadlineTask.describe(): \", due 2026-10-05\" қосылады")
    void deadlineDescribe() {
        DeadlineTask t = new DeadlineTask(2, "Pay bills", Priority.LOW, DUE);
        t.start();
        assertEquals("#2 [LOW] Pay bills (IN_PROGRESS), due 2026-10-05", t.describe());
    }

    @Test
    @DisplayName("полиморфизм: List<Task> ішінде әр тип өзінше жауап береді")
    void polymorphism() {
        List<Task> tasks = List.of(
                new SimpleTask(1, "A", Priority.LOW),
                new DeadlineTask(2, "B", Priority.LOW, DUE));

        LocalDate late = DUE.plusDays(10);
        assertFalse(tasks.get(0).isOverdue(late));
        assertTrue(tasks.get(1).isOverdue(late));
    }
}
