package kz.learn.todo.model;

import java.time.LocalDate;

/**
 * Мерзімі жоқ қарапайым тапсырма — ешқашан кешікпейді, details() бос.
 */
public class SimpleTask extends Task {

    public SimpleTask(long id, String title, Priority priority) {
        super(id, title, priority);
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
