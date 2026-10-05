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
        return 3;
    }

    @Override
    public long applyDiscount(long fine) {
        return fine / 2;
    }
}
