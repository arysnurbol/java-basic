package kz.learn.hotel.room;

import kz.learn.hotel.model.Money;

/**
 * Бөлме түрі: сыйымдылығы (ең көп қонақ саны) және бір түннің базалық бағасы.
 *
 * Банктегі TransactionType-та бір өріс болды, мұнда — екеу. Конструктор long алады,
 * өрісте Money сақталады: STANDARD(2, 20_000) жазу Money.of(...) жазудан оқуға оңай.
 */
public enum RoomType {
    STANDARD(2, 20_000),
    DELUXE(3, 35_000),
    SUITE(4, 60_000);

    // TODO: өрістерді жаз

    /** basePrice-ты Money.of(...) арқылы сақта. */
    RoomType(int capacity, long basePrice) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int capacity() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money basePrice() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
