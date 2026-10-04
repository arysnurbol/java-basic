package kz.learn.library.model;

/**
 * DVD: 3 күнге беріледі, күніне 200 ₸ айыппұл.
 * minutes — оң сан (Check.positive). details() -> ", 148 min"
 */
public class Dvd extends Item {

    private final int minutes;

    public Dvd(long id, String title, int minutes) {
        super(id, title);
        this.minutes = Check.positive(minutes,  "minutes");
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public int loanDays() {
        return 3;
    }

    @Override
    public long dailyFine() {
        return 200;
    }

    @Override
    protected String details() {
        return ", " + minutes + " min";
    }
}
