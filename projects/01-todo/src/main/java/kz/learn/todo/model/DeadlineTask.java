package kz.learn.todo.model;

import kz.learn.todo.exception.InvalidTaskException;

import java.time.LocalDate;

/**
 * Мерзімі (dueDate) бар тапсырма.
 *
 * - dueDate өрісі private final.
 * - dueDate null болса — InvalidTaskException.
 * - Кешікті деп саналады: тапсырма DONE ЕМЕС және today > dueDate.
 *   (dueDate күнінің өзінде — әлі кешікпеген.)
 * - details() -> ", due 2026-10-05"
 */
public class DeadlineTask extends Task {

    private final LocalDate dueDate;

    public DeadlineTask(long id, String title, Priority priority, LocalDate dueDate) {
        super(id, title, priority);
        this.dueDate = validateDueDate(dueDate);
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public boolean isOverdue(LocalDate today) {
        return today.isAfter(dueDate) && !isDone();
    }

    @Override
    protected String details() {
        return ", due " + dueDate;
    }

    private static LocalDate validateDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new InvalidTaskException("due date cannot be null!");
        }
        return dueDate;
    }
}
