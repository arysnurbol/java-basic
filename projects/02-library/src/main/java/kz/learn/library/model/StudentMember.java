package kz.learn.library.model;

/**
 * Студент: бір уақытта 3 экземпляр, айыппұлға 50% жеңілдік (бүтін бөлу: 75 -> 37).
 */
public class StudentMember extends Member {

    public StudentMember(String cardNumber, String name) {
        super(cardNumber, name);
    }

    @Override
    public int maxLoans() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public long applyDiscount(long fine) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
