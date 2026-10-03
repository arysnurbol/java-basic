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

    private final long id;
    private String title;
    private Priority priority;
    private Status status;

    /**
     * title null немесе бос (тек бос орын да) болса — InvalidTaskException.
     * priority null болса — InvalidTaskException.
     * title-дың шетіндегі бос орындар алынады (trim). Жаңа тапсырманың күйі әрқашан TODO.
     */
    protected Task(long id, String title, Priority priority) {

        this.id = id;
        this.title = validateAndTrimTitle(title);
        this.priority = validatePriority(priority);
        this.status = Status.TODO;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    /** Конструктордағыдай тексеріс және trim. */
    public void rename(String newTitle) {
        this.title = validateAndTrimTitle(newTitle);
    }

    /** null — InvalidTaskException. */
    public void changePriority(Priority newPriority) {
        this.priority = validatePriority(newPriority);
    }

    /**
     * status.canMoveTo(next) рұқсат бермесе — InvalidTaskException,
     * хабарламада қай күйден қай күйге екені жазылсын: "Cannot move from DONE to TODO".
     */
    public void moveTo(Status next) {
        if (next == null) {
            throw new InvalidTaskException("Next status can not be null!");
        }
        if (!this.status.canMoveTo(next)) {
            throw new InvalidTaskException("Cannot move from " + this.status + " to " + next);
        }
        this.status = next;
    }

    /** moveTo(IN_PROGRESS) — кодты қайталама, moveTo-ны шақыр. */
    public void start() {
        moveTo(Status.IN_PROGRESS);
    }

    /** moveTo(DONE) */
    public void complete() {
        moveTo(Status.DONE);
    }

    public boolean isDone() {
        return status == Status.DONE;
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
        return "#" + id + " [" + priority + "] " + title + " (" + status + ")" + details();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return describe();
    }

    private static String validateAndTrimTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidTaskException("title can not be null or empty");
        }
        return title.trim();
    }

    private static Priority validatePriority(Priority priority) {
        if (priority == null) {
            throw new InvalidTaskException("priority can not be null");
        }
        return priority;
    }
}
