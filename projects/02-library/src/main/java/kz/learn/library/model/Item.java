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

    private final long id;
    private final String title;

    /** title-ды Check.text(...) арқылы тексеріп, trim етіп сақта. */
    protected Item(long id, String title) {
        this.id = id;
        this.title = Check.text(title, "title");
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
        if (this == o) return true;
        if (!(o instanceof Item other)) return false;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return describe();
    }
}
