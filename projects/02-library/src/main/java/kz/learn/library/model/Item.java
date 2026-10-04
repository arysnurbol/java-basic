package kz.learn.library.model;

/**
 * Кітапхана экземплярының абстрактілі ата-класы — БІРІНШІ иерархия.
 *
 * ООП:
 *  - immutable: барлық өрістер private final, сеттер жоқ (ToDo-дағы Task-тан айырмашылығы осы);
 *  - абстракция: loanDays() және dailyFine() — әр түр өз ережесін береді, сервис оларды
 *    тек Item ретінде көреді (полиморфизм);
 *  - Identifiable<Long> — сондықтан generic репозиторийге сақталады.
 *
 * Өрістер: id (long), title (String). equals/hashCode — тек id бойынша.
 */
public abstract class Item implements Identifiable<Long> {

    // TODO: өрістерді жаз

    /** title-ды Check.text(...) арқылы тексеріп, trim етіп сақта. */
    protected Item(long id, String title) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Long getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getTitle() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Неше күнге беріледі. */
    public abstract int loanDays();

    /** Бір күн кешіктірудің айыппұлы, теңгемен. */
    public abstract long dailyFine();

    /** describe() соңына қосылатын ұрпақтың өз мәліметі: ", Robert Martin". */
    protected abstract String details();

    /** ДАЙЫН. Пішім: "#1 Book: Clean Code, Robert Martin" */
    public final String describe() {
        return "#" + getId() + " " + getClass().getSimpleName() + ": " + getTitle() + details();
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
