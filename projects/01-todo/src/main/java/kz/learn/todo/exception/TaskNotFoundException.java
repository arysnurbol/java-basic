package kz.learn.todo.exception;

/**
 * Берілген id бойынша тапсырма табылмаса лақтырылады.
 * getMessage() пішімі: "Task not found: id=5"
 */
public class TaskNotFoundException extends RuntimeException {

    // TODO: өрістерді жаз

    public TaskNotFoundException(long taskId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public long getTaskId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
