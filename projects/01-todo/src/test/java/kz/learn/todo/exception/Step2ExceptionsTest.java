package kz.learn.todo.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("Қадам 2 — Өз exception-дарың")
class Step2ExceptionsTest {

    @Test
    @DisplayName("InvalidTaskException: хабарлама сақталады, unchecked")
    void invalidTask() {
        InvalidTaskException e = new InvalidTaskException("bad title");

        assertEquals("bad title", e.getMessage());
        assertInstanceOf(RuntimeException.class, e);
    }

    @Test
    @DisplayName("TaskNotFoundException: id және хабарлама")
    void notFound() {
        TaskNotFoundException e = new TaskNotFoundException(5);

        assertEquals(5, e.getTaskId());
        assertEquals("Task not found: id=5", e.getMessage());
        assertInstanceOf(RuntimeException.class, e);
    }
}
