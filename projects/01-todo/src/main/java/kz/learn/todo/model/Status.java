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
        return switch (this) {
            case TODO -> next == IN_PROGRESS;
            case IN_PROGRESS -> next == DONE || next == TODO;
            case DONE -> false;
        };
    }
}
