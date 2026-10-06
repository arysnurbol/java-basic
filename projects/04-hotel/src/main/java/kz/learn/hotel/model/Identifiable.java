package kz.learn.hotel.model;

/**
 * id-сы бар кез келген нәрсе. ДАЙЫН — 03-bank-тан өзгеріссіз.
 * Мұнда Room үшін ID = бөлме нөмірі ("101"), Booking үшін — бронь нөмірі ("B0001").
 */
public interface Identifiable<ID> {

    ID getId();
}
