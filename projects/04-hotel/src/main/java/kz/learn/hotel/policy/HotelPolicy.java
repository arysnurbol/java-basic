package kz.learn.hotel.policy;

import kz.learn.hotel.model.Money;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Қонақүй ережелері — SINGLETON: бүкіл жүйеде бір ғана данасы бар.
 *
 * Классикалық үш бөлік:
 *   1) private static final HotelPolicy INSTANCE = new HotelPolicy();  — жалғыз дана;
 *   2) private конструктор — сырттан new HotelPolicy() жазу мүмкін емес;
 *   3) public static getInstance() — данаға жалғыз жол.
 * Мұндағы нұсқа — «eager» (класс жүктелгенде жасалады). JVM класты бір рет, ағын-қауіпсіз жүктейді,
 * сондықтан synchronized, double-checked locking сияқты күрделі нұсқалар керек емес.
 * (Тағы бір қысқа жол — enum HotelPolicy { INSTANCE; ... }. Joshua Bloch осыны ұсынады.)
 *
 * Бұл singleton ҚАУІПСІЗ, өйткені immutable: барлық өріс final, күйі өзгермейді.
 * Өзгермелі күйі бар singleton (мысалы, ортақ есептегіш) — жасырын глобал айнымалы:
 * тесттер бір-біріне әсер ете бастайды, ал «кім өзгертті?» деген сұраққа жауап табу қиын.
 * Сол себепті BankService/HotelService-ті singleton етпедік — оларды DI арқылы береміз.
 *
 * Өрістер (бәрі final, мәндері конструкторда):
 *   maxNights = 30                       — бір броньның ең ұзақ мерзімі;
 *   weekendSurchargePercent = 25         — DELUXE бөлмеде жұма мен сенбі түндеріне үстеме;
 *   longStayNights = 7, longStayDiscountPercent = 10 — SUITE: 7 түн және одан көп болса, бүкіл сомаға жеңілдік;
 *   freeCancellationDays = 7             — келуге кемі 7 күн қалса, ақша толық қайтады;
 *   lateCancellationRefundPercent = 50   — 1–6 күн қалса — жартысы; келу күні және кейін — ештеңе.
 */
public final class HotelPolicy {

    // TODO: өрістерді жаз

    /** private! Мәндерді осында меншікте (BigDecimal.valueOf(25) т.с.с.). */
    private HotelPolicy() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Әрқашан СОЛ БІР объект. */
    public static HotelPolicy getInstance() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int maxNights() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal weekendSurchargePercent() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int longStayNights() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal longStayDiscountPercent() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int freeCancellationDays() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal lateCancellationRefundPercent() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Демалыс ТҮНІ ме: жұма немесе сенбі (жексенбі түні — дүйсенбіге қарсы, жұмыс күні).
     * Кеңес: night.getDayOfWeek(), DayOfWeek.FRIDAY.
     */
    public boolean isWeekend(LocalDate night) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Болдырмағанда қайтарылатын ақша.
     * daysBefore = today-ден checkIn-ге дейінгі күн саны (ChronoUnit.DAYS.between(today, checkIn)):
     *   daysBefore >= freeCancellationDays -> paid (толық);
     *   daysBefore >= 1                    -> paid.percent(lateCancellationRefundPercent);
     *   әйтпесе (келу күні немесе одан кейін, daysBefore <= 0) -> Money.ZERO.
     * paid, today, checkIn — null болмайды.
     */
    public Money refundFor(Money paid, LocalDate today, LocalDate checkIn) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
