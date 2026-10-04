package kz.learn.library.model;

/**
 * Журнал: 7 күнге беріледі, күніне 20 ₸ айыппұл.
 * issue (шығарылым нөмірі) — оң сан (Check.positive). details() -> ", No 12"
 */
public class Magazine extends Item {

    private final int issue;

    public Magazine(long id, String title, int issue) {
        super(id, title);
        this.issue = Check.positive(issue, "issue");
    }

    public int getIssue() {
        return issue;
    }

    @Override
    public int loanDays() {
        return 7;
    }

    @Override
    public long dailyFine() {
        return 20;
    }

    @Override
    protected String details() {
        return ", No " + issue;
    }
}
