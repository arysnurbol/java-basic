package kz.learn.library.model;

import kz.learn.library.exception.LibraryException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Беру жазбасы: КІМ (Member) ҚАНДАЙ экземплярды (Item) ҚАШАН алды.
 * Екі иерархия осы жерде кездеседі: мерзім мен күнделікті айыппұлды Item береді,
 * жеңілдікті Member береді. Loan олардың нақты типін білмейді — полиморфизм.
 *
 * Өрістер: id, item, member, loanDate, dueDate — final; returnDate — қайтарылғанға дейін null.
 * dueDate = loanDate + item.loanDays().
 */
public class Loan implements Identifiable<Long> {

    private final Long id;
    private final Item item;
    private final Member member;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate = null;

    /** item, member, loanDate — null болмайды: Objects.requireNonNull(x, "item") -> NullPointerException. */
    public Loan(long id, Item item, Member member, LocalDate loanDate) {
        this.id = id;
        this.item = Objects.requireNonNull(item, "item cannot be null");
        this.member = Objects.requireNonNull(member, "member cannot be null");
        this.loanDate = Objects.requireNonNull(loanDate, "loanDate cannot be null");

        this.dueDate = loanDate.plusDays(item.loanDays());
    }

    @Override
    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /** Қайтарылмаса — null. */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    /**
     * Қайтарылды деп белгілейді.
     * Бұрын қайтарылған болса — LibraryException("Loan already returned: id=1").
     * date < loanDate болса — LibraryException("Return date is before loan date").
     */
    public void markReturned(LocalDate date) {
        Objects.requireNonNull(date, "Return date cannot be null");

        if (date.isBefore(loanDate)) {
            throw new LibraryException("Return date is before loan date");
        }
        if (isReturned()) {
            throw new LibraryException("Loan already returned: id=" + id);
        }
        this.returnDate = date;
    }

    /**
     * Неше күн кешікті. Қайтарылған болса — returnDate бойынша (кейін өзгермейді),
     * әйтпесе — today бойынша. Кешікпесе — 0, ешқашан теріс емес.
     * Кеңес: ChronoUnit.DAYS.between(dueDate, end)
     */
    public long overdueDays(LocalDate today) {
        LocalDate endPoint = isReturned() ? returnDate : today;

        if (endPoint.isBefore(dueDate) || endPoint.isEqual(dueDate)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(dueDate, endPoint);
    }

    /** Әлі қайтарылмаған ЖӘНЕ мерзімі өткен. */
    public boolean isOverdue(LocalDate today) {
        return dueDate.isBefore(today) && !isReturned();
    }

    /**
     * Айыппұл = overdueDays * item.dailyFine(), содан кейін member.applyDiscount(...).
     * Мұнда instanceof ЖАЗБА — жұмысты Item мен Member-ге тапсыр.
     */
    public long fine(LocalDate today) {
        long days = overdueDays(today);
        if (days == 0) {
            return 0;
        }

        long baseFine = days * item.dailyFine();

        return member.applyDiscount(baseFine);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** ДАЙЫН. Пішім: "L1: #3 Book: Clean Code, Robert Martin -> S-1, due 2026-10-15" (+ ", returned ...") */
    @Override
    public String toString() {
        return "L" + getId() + ": " + getItem().describe() + " -> " + getMember().getId() + ", due " + getDueDate()
                + (isReturned() ? ", returned " + getReturnDate() : "");
    }
}
