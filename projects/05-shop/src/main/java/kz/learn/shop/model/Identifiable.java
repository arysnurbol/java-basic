package kz.learn.shop.model;

/**
 * id-сы бар кез келген нәрсе. ДАЙЫН — 04-hotel-дан өзгеріссіз.
 * Мұнда Product үшін ID = артикул ("P1"), Order үшін — тапсырыс нөмірі ("ORD-0001").
 */
public interface Identifiable<ID> {

    ID getId();
}
