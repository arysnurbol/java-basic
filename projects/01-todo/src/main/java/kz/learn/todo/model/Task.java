package kz.learn.todo.model;

import kz.learn.todo.exception.InvalidTaskException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Барлық тапсырмалардың абстрактілі ата-класы.
 *
 * ООП:
 *  - абстракция: isOverdue() және details() — әр ұрпақ өзінше жүзеге асырады;
 *  - инкапсуляция: өрістер private, id — final; күй тек start()/complete()/moveTo()
 *    арқылы өзгереді, setStatus() ЖАЗБА;
 *  - Template Method: describe() final — жалпы пішімді осы класс құрады,
 *    ал айырмашылықты details() арқылы ұрпақ қосады.
 *
 * Өрістер: id (long, final), title (String), priority (Priority), status (Status).
 * Екі тапсырманың id-лары тең болса — олар тең (equals/hashCode тек id бойынша).
 *
 * Кеңес: title мен priority тексерісін private static көмекші методқа шығар —
 * конструктор да, rename()/changePriority() да соны қолдансын (DRY).
 */
public abstract class Task {

    // TODO: өрістерді жаз

    /**
     * title null немесе бос (тек бос орын да) болса — InvalidTaskException.
     * priority null болса — InvalidTaskException.
     * title-дың шетіндегі бос орындар алынады (trim). Жаңа тапсырманың күйі әрқашан TODO.
     */
    protected Task(long id, String title, Priority priority) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public long getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getTitle() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Priority getPriority() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Status getStatus() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Конструктордағыдай тексеріс және trim. */
    public void rename(String newTitle) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** null — InvalidTaskException. */
    public void changePriority(Priority newPriority) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * status.canMoveTo(next) рұқсат бермесе — InvalidTaskException,
     * хабарламада қай күйден қай күйге екені жазылсын: "Cannot move from DONE to TODO".
     */
    public void moveTo(Status next) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** moveTo(IN_PROGRESS) — кодты қайталама, moveTo-ны шақыр. */
    public void start() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** moveTo(DONE) */
    public void complete() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isDone() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** today күні тапсырма мерзімінен өтіп кетті ме? Әр ұрпақ өзі шешеді. */
    public abstract boolean isOverdue(LocalDate today);

    /** describe() соңына қосылатын ұрпақтың өз мәліметі. Қосары болмаса — "". */
    protected abstract String details();

    /**
     * Пішім: "#1 [HIGH] Buy milk (TODO)" + details()
     * DeadlineTask үшін мысал: "#2 [LOW] Pay bills (IN_PROGRESS), due 2026-10-05"
     */
    public final String describe() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int hashCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String toString() {
        return describe();
    }
}
