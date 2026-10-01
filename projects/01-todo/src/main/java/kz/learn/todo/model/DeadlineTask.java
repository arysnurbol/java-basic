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

    // TODO: өрістерді жаз

    public DeadlineTask(long id, String title, Priority priority, LocalDate dueDate) {
        super(id, title, priority);
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public LocalDate getDueDate() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean isOverdue(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    protected String details() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
