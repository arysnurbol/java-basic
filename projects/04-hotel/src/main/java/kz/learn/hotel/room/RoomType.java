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

    private final int capacity;
    private final Money basePrice;

    /** basePrice-ты Money.of(...) арқылы сақта. */
    RoomType(int capacity, long basePrice) {
        this.capacity = capacity;
        this.basePrice = Money.of(basePrice);
    }

    public int capacity() {
        return capacity;
    }

    public Money basePrice() {
        return basePrice;
    }
}
