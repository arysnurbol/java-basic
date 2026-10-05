package kz.learn.bank.model;

/**
 * id-сы бар кез келген нәрсе. ДАЙЫН — 02-library-дан өзгеріссіз.
 * Мұнда Account үшін ID = String (шот нөмірі: "KZ0001").
 */
public interface Identifiable<ID> {

    ID getId();
}
