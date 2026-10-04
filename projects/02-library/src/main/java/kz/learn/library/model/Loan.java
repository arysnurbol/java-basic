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

    // TODO: өрістерді жаз

    /** item, member, loanDate — null болмайды: Objects.requireNonNull(x, "item") -> NullPointerException. */
    public Loan(long id, Item item, Member member, LocalDate loanDate) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Long getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Item getItem() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Member getMember() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public LocalDate getLoanDate() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public LocalDate getDueDate() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Қайтарылмаса — null. */
    public LocalDate getReturnDate() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isReturned() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Қайтарылды деп белгілейді.
     * Бұрын қайтарылған болса — LibraryException("Loan already returned: id=1").
     * date < loanDate болса — LibraryException("Return date is before loan date").
     */
    public void markReturned(LocalDate date) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Неше күн кешікті. Қайтарылған болса — returnDate бойынша (кейін өзгермейді),
     * әйтпесе — today бойынша. Кешікпесе — 0, ешқашан теріс емес.
     * Кеңес: ChronoUnit.DAYS.between(dueDate, end)
     */
    public long overdueDays(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Әлі қайтарылмаған ЖӘНЕ мерзімі өткен. */
    public boolean isOverdue(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Айыппұл = overdueDays * item.dailyFine(), содан кейін member.applyDiscount(...).
     * Мұнда instanceof ЖАЗБА — жұмысты Item мен Member-ге тапсыр.
     */
    public long fine(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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

    /** ДАЙЫН. Пішім: "L1: #3 Book: Clean Code, Robert Martin -> S-1, due 2026-10-15" (+ ", returned ...") */
    @Override
    public String toString() {
        return "L" + getId() + ": " + getItem().describe() + " -> " + getMember().getId() + ", due " + getDueDate()
                + (isReturned() ? ", returned " + getReturnDate() : "");
    }
}
