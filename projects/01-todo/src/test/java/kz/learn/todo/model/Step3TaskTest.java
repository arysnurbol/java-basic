package kz.learn.todo.model;

import kz.learn.todo.exception.InvalidTaskException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 3 — Task: абстрактілі класс, инкапсуляция, күй")
class Step3TaskTest {

    /** Task абстрактілі болғандықтан, тексеру үшін ең қарапайым ұрпақ жасаймыз. */
    static class TestTask extends Task {
        TestTask(long id, String title, Priority priority) {
            super(id, title, priority);
        }

        @Override
        public boolean isOverdue(LocalDate today) {
            return false;
        }

        @Override
        protected String details() {
            return "";
        }
    }

    private Task task() {
        return new TestTask(1, "Buy milk", Priority.HIGH);
    }

    @Test
    @DisplayName("Task — abstract, өрістері private, id — final, setStatus жоқ")
    void structure() {
        assertTrue(Modifier.isAbstract(Task.class.getModifiers()), "Task abstract болуы керек");

        Field[] fields = Task.class.getDeclaredFields();
        assertTrue(fields.length >= 4, "кемінде 4 өріс: id, title, priority, status");
        for (Field f : fields) {
            assertTrue(Modifier.isPrivate(f.getModifiers()), "өріс private емес: " + f.getName());
        }
        assertTrue(Arrays.stream(fields)
                        .anyMatch(f -> f.getName().equals("id") && Modifier.isFinal(f.getModifiers())),
                "id өрісі final болуы керек");
        assertTrue(Arrays.stream(Task.class.getMethods()).noneMatch(m -> m.getName().equals("setStatus")),
                "setStatus() болмауы керек — күй тек start/complete/moveTo арқылы өзгереді");
    }

    @Test
    @DisplayName("конструктор: мәндер сақталады, күйі TODO, title trim")
    void constructor() {
        Task t = new TestTask(7, "  Read book  ", Priority.LOW);

        assertEquals(7, t.getId());
        assertEquals("Read book", t.getTitle());
        assertEquals(Priority.LOW, t.getPriority());
        assertEquals(Status.TODO, t.getStatus());
        assertFalse(t.isDone());
    }

    @Test
    @DisplayName("конструктор: бос/null title немесе null priority -> InvalidTaskException")
    void constructorValidation() {
        assertThrows(InvalidTaskException.class, () -> new TestTask(1, null, Priority.LOW));
        assertThrows(InvalidTaskException.class, () -> new TestTask(1, "", Priority.LOW));
        assertThrows(InvalidTaskException.class, () -> new TestTask(1, "   ", Priority.LOW));
        assertThrows(InvalidTaskException.class, () -> new TestTask(1, "Ok", null));
    }

    @Test
    @DisplayName("rename / changePriority: тексеріспен")
    void renameAndChangePriority() {
        Task t = task();

        t.rename("  Buy bread ");
        assertEquals("Buy bread", t.getTitle());
        assertThrows(InvalidTaskException.class, () -> t.rename(" "));
        assertEquals("Buy bread", t.getTitle(), "сәтсіз rename атауды өзгертпеуі керек");

        t.changePriority(Priority.MEDIUM);
        assertEquals(Priority.MEDIUM, t.getPriority());
        assertThrows(InvalidTaskException.class, () -> t.changePriority(null));
        assertEquals(Priority.MEDIUM, t.getPriority());
    }

    @Test
    @DisplayName("start -> complete: TODO -> IN_PROGRESS -> DONE")
    void happyPath() {
        Task t = task();

        t.start();
        assertEquals(Status.IN_PROGRESS, t.getStatus());

        t.complete();
        assertEquals(Status.DONE, t.getStatus());
        assertTrue(t.isDone());
    }

    @Test
    @DisplayName("ережеге қайшы ауысу -> InvalidTaskException, күй өзгермейді")
    void invalidTransition() {
        Task t = task();

        InvalidTaskException e = assertThrows(InvalidTaskException.class, t::complete);
        assertTrue(e.getMessage().contains("TODO") && e.getMessage().contains("DONE"),
                "хабарламада екі күй де болсын, келгені: " + e.getMessage());
        assertEquals(Status.TODO, t.getStatus());

        t.start();
        t.complete();
        assertThrows(InvalidTaskException.class, t::start);
        assertThrows(InvalidTaskException.class, () -> t.moveTo(Status.TODO));
        assertEquals(Status.DONE, t.getStatus());
    }

    @Test
    @DisplayName("IN_PROGRESS -> TODO қайтаруға болады")
    void backToTodo() {
        Task t = task();
        t.start();
        t.moveTo(Status.TODO);
        assertEquals(Status.TODO, t.getStatus());
    }

    @Test
    @DisplayName("describe(): \"#1 [HIGH] Buy milk (TODO)\"")
    void describe() {
        Task t = task();
        assertEquals("#1 [HIGH] Buy milk (TODO)", t.describe());

        t.start();
        assertEquals("#1 [HIGH] Buy milk (IN_PROGRESS)", t.describe());
        assertEquals(t.describe(), t.toString());
    }

    @Test
    @DisplayName("equals/hashCode тек id бойынша")
    void equality() {
        Task a = new TestTask(1, "A", Priority.LOW);
        Task sameId = new TestTask(1, "B", Priority.HIGH);
        Task otherId = new TestTask(2, "A", Priority.LOW);

        assertEquals(a, sameId);
        assertEquals(a.hashCode(), sameId.hashCode());
        assertNotEquals(a, otherId);
        assertNotEquals(a, null);
        assertNotEquals(a, "A");
    }
}
