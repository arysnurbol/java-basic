package kz.learn.library.model;

/**
 * Кәдімгі оқырман: бір уақытта 5 экземпляр, жеңілдік жоқ.
 * applyDiscount()-ты override ЕТПЕ — ата-кластағы әдепкі мінез-құлық жарайды.
 */
public class RegularMember extends Member {

    public RegularMember(String cardNumber, String name) {
        super(cardNumber, name);
    }

    @Override
    public int maxLoans() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
