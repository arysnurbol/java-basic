package kz.learn.hotel.room;

import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Money;
import kz.learn.hotel.policy.HotelPolicy;

import java.time.LocalDate;

/**
 * Люкс: түн бағасы тұрақты, бірақ ұзақ тұрсаң (longStayNights және одан көп) —
 * БҮКІЛ сомаға longStayDiscountPercent% жеңілдік. 7 түн: 420 000 - 10% = 378 000 ₸.
 *
 * Мұнда бір түннің бағасы емес, бүкіл кезеңнің ережесі өзгереді — сондықтан priceFor-ды override етеміз.
 */
class Suite extends Room {

    Suite(String number) {
        super(number, RoomType.SUITE);
    }

    @Override
    public Money priceForNight(LocalDate night) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Кеңес: Money total = super.priceFor(stay); — ата-класстың есебін қайта жазба, қолдан. */
    @Override
    public Money priceFor(DateRange stay) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
