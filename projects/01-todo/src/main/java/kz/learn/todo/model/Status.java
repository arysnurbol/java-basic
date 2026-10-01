package kz.learn.todo.model;

/**
 * Тапсырманың күйі.
 *
 * Рұқсат етілген ауысулар:
 *   TODO        -> IN_PROGRESS
 *   IN_PROGRESS -> DONE, TODO
 *   DONE        -> (ешқайда, соңғы күй)
 * Өзіне-өзі ауысу (TODO -> TODO) — рұқсат ЕМЕС. next == null -> false.
 *
 * Кеңес: switch (this) { case TODO -> ... }
 */
public enum Status {
    TODO,
    IN_PROGRESS,
    DONE;

    public boolean canMoveTo(Status next) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
