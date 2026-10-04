package kz.learn.library.model;

/**
 * Кітап: 14 күнге беріледі, күніне 50 ₸ айыппұл.
 * author — бос болмайды (Check.text), trim. details() -> ", Robert Martin"
 */
public class Book extends Item {

    // TODO: өрістерді жаз

    public Book(long id, String title, String author) {
        super(id, title);
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getAuthor() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int loanDays() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public long dailyFine() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    protected String details() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
