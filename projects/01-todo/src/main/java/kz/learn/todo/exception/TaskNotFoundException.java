package kz.learn.todo.exception;

/**
 * Берілген id бойынша тапсырма табылмаса лақтырылады.
 * getMessage() пішімі: "Task not found: id=5"
 */
public class TaskNotFoundException extends RuntimeException {

    private final long taskId;

    public TaskNotFoundException(long taskId) {
        super("Task not found: id=" + taskId);
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }
}
