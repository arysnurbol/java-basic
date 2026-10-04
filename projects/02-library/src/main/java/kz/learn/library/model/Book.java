package kz.learn.library.model;

/**
 * Кітап: 14 күнге беріледі, күніне 50 ₸ айыппұл.
 * author — бос болмайды (Check.text), trim. details() -> ", Robert Martin"
 */
public class Book extends Item {

    private final String author;

    public Book(long id, String title, String author) {
        super(id, title);
        this.author = Check.text(author, "author");
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int loanDays() {
        return 14;
    }

    @Override
    public long dailyFine() {
        return 50;
    }

    @Override
    protected String details() {
        return ", " + author;
    }
}
