package kz.learn.library.model;

/**
 * DVD: 3 күнге беріледі, күніне 200 ₸ айыппұл.
 * minutes — оң сан (Check.positive). details() -> ", 148 min"
 */
public class Dvd extends Item {

    // TODO: өрістерді жаз

    public Dvd(long id, String title, int minutes) {
        super(id, title);
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getMinutes() {
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
