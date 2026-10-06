package kz.learn.hotel.room;

import kz.learn.hotel.model.Money;

import java.time.LocalDate;

/**
 * Стандарт: кез келген түн — базалық баға.
 *
 * Класс та, конструктор да public ЕМЕС (package-private): room пакетінен тыс ешкім
 * new StandardRoom(...) жаза алмайды — тек RoomFactory.create(...).
 */
class StandardRoom extends Room {

    /** super(number, RoomType.STANDARD). */
    StandardRoom(String number) {
        super(number, RoomType.STANDARD);
    }

    @Override
    public Money priceForNight(LocalDate night) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
