package kz.learn.hotel.room;

import kz.learn.hotel.model.Money;
import kz.learn.hotel.policy.HotelPolicy;

import java.time.LocalDate;

/**
 * Делюкс: демалыс түндері (жұма, сенбі) қымбат — базалық баға + weekendSurchargePercent%.
 * 35 000 + 25% = 43 750 ₸.
 */
class DeluxeRoom extends Room {

    DeluxeRoom(String number) {
        super(number, RoomType.DELUXE);
    }

    /** Кеңес: HotelPolicy.getInstance().isWeekend(night), basePrice().percent(...). */
    @Override
    public Money priceForNight(LocalDate night) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
