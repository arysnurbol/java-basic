package kz.learn.library.model;

/**
 * Журнал: 7 күнге беріледі, күніне 20 ₸ айыппұл.
 * issue (шығарылым нөмірі) — оң сан (Check.positive). details() -> ", No 12"
 */
public class Magazine extends Item {

    // TODO: өрістерді жаз

    public Magazine(long id, String title, int issue) {
        super(id, title);
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getIssue() {
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
