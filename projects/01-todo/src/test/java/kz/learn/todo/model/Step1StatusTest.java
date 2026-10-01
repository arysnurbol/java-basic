package kz.learn.todo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kz.learn.todo.model.Status.DONE;
import static kz.learn.todo.model.Status.IN_PROGRESS;
import static kz.learn.todo.model.Status.TODO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 1 — Status: күй ауысу ережелері")
class Step1StatusTest {

    @Test
    @DisplayName("TODO -> тек IN_PROGRESS")
    void fromTodo() {
        assertTrue(TODO.canMoveTo(IN_PROGRESS));
        assertFalse(TODO.canMoveTo(DONE), "TODO -> DONE рұқсат емес, алдымен бастау керек");
        assertFalse(TODO.canMoveTo(TODO), "өзіне-өзі ауысу рұқсат емес");
    }

    @Test
    @DisplayName("IN_PROGRESS -> DONE немесе TODO")
    void fromInProgress() {
        assertTrue(IN_PROGRESS.canMoveTo(DONE));
        assertTrue(IN_PROGRESS.canMoveTo(TODO));
        assertFalse(IN_PROGRESS.canMoveTo(IN_PROGRESS));
    }

    @Test
    @DisplayName("DONE — соңғы күй, ешқайда ауыспайды")
    void fromDone() {
        for (Status next : Status.values()) {
            assertFalse(DONE.canMoveTo(next), "DONE -> " + next);
        }
    }

    @Test
    @DisplayName("null -> false")
    void nullIsNotAllowed() {
        for (Status s : Status.values()) {
            assertFalse(s.canMoveTo(null), s + " -> null");
        }
    }
}
