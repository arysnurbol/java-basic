package kz.learn.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * Тұру кезеңі — ЖАРТЫЛАЙ АШЫҚ интервал [checkIn, checkOut): келу күні кіреді, кету күні КІРМЕЙДІ.
 *
 * Неге жартылай ашық: қонақүйде ақы ТҮН үшін алынады. 10-нан 13-ке дейін = 10, 11, 12 түндері = 3 түн.
 * 13-і — кету күні, сол түні бөлме бос, оны келесі қонақ ала алады.
 * Сондықтан [10, 13) мен [13, 15) ҚИЫЛЫСПАЙДЫ — бір күні біреуі кетеді, біреуі келеді.
 * Жабық интервалмен [10, 12] + [13, 14] деп жазсақ, «түн саны = айырма + 1» сияқты +1/-1 қателері басталады.
 * java.time да осылай ойлайды: LocalDate.datesUntil(end), ChronoUnit.DAYS.between(a, b) — end кірмейді.
 *
 * record — immutable: кезеңді өзгерту = жаңа DateRange.
 */
public record DateRange(LocalDate checkIn, LocalDate checkOut) {

    /**
     * checkIn, checkOut — null болмайды: Objects.requireNonNull(x, "checkIn").
     * checkOut checkIn-нен КЕЙІН болуы керек (кемі 1 түн),
     * әйтпесе IllegalArgumentException("checkOut must be after checkIn").
     */
    public DateRange {
        Objects.requireNonNull(checkIn, "checkIn");
        Objects.requireNonNull(checkOut, "checkOut");
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("checkOut must be after checkIn");
        }
    }

    /**
     * Статикалық фабрика: келу күні + түн саны. DateRange.of(10-қазан, 3) -> [10, 13).
     * nights < 1 -> IllegalArgumentException("nights must be positive").
     * Кеңес: checkIn.plusDays(nights).
     */
    public static DateRange of(LocalDate checkIn, int nights) {
        if (nights < 1) {
            throw new IllegalArgumentException("nights must be positive");
        }
        return new DateRange(checkIn, checkIn.plusDays(nights));
    }

    /**
     * Түн саны. Кеңес: ChronoUnit.DAYS.between(...).
     * Неге checkOut.getDayOfMonth() - checkIn.getDayOfMonth() емес? 30-қазаннан 2-қарашаға дейін байқап көр.
     */
    public long nights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    /** Осы күннің ТҮНІ кезеңге кіре ме: checkIn <= night < checkOut. Кету күні — false. */
    public boolean contains(LocalDate night) {
        return !night.isBefore(checkIn) && night.isBefore(checkOut);
    }

    /**
     * Екі кезең қиылыса ма (кемі бір ортақ түн бар ма).
     *
     * Классикалық формула: a.start < b.end && b.start < a.end. Бір жол — бірақ оны ТҮСІН:
     * «қиылыспайды» деген не? Не a b-дан бұрын толық бітеді (a.end <= b.start),
     * не b a-дан бұрын бітеді (b.end <= a.start). Осыны терістесең (де Морган) — формула шығады.
     * Төрт if-пен «ішінде ме, сол жақтан ба, оң жақтан ба, жауып тұр ма» деп жазба.
     */
    public boolean overlaps(DateRange other) {
        return checkIn.isBefore(other.checkOut) && other.checkIn.isBefore(checkOut);
    }

    /** Барлық түндер ретімен: [10, 13) -> [10, 11, 12]. Кеңес: checkIn.datesUntil(checkOut).toList(). */
    public List<LocalDate> nightDates() {
        return checkIn.datesUntil(checkOut).toList();
    }

    /** ДАЙЫН. "2026-10-10..2026-10-13" */
    @Override
    public String toString() {
        return checkIn + ".." + checkOut;
    }
}
