package kz.learn.library.model;

/**
 * Оқырманның абстрактілі ата-класы — ЕКІНШІ иерархия.
 *
 * id — оқырман билетінің нөмірі (String, мысалы "S-1"), сондықтан Identifiable<String>.
 * Өрістер: cardNumber, name — private final, екеуі де Check.text арқылы тексеріліп, trim.
 * equals/hashCode — тек cardNumber бойынша.
 *
 * Екі түрлі метод:
 *  - maxLoans()       — abstract: әр түр МІНДЕТТІ түрде өзі анықтайды;
 *  - applyDiscount()  — кәдімгі метод ("hook"): әдепкі мінез-құлқы бар,
 *                       қажет болған ұрпақ қана override етеді.
 */
public abstract class Member implements Identifiable<String> {

    private final String name;
    private final String cardNumber;

    protected Member(String cardNumber, String name) {
        this.cardNumber = Check.text(cardNumber, "cardNumber");
        this.name = Check.text(name, "name");
    }

    @Override
    public String getId() {
        return cardNumber;
    }

    public String getName() {
        return name;
    }

    /** Бір уақытта ең көп неше экземпляр ұстай алады. */
    public abstract int maxLoans();

    /** Есептелген айыппұлға жеңілдік қолданады. Әдепкі — жеңілдік жоқ. */
    public long applyDiscount(long fine) {
        return fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member other)) return false;
        return cardNumber.equals(other.cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }

    /** ДАЙЫН. Пішім: "S-1 Aru (StudentMember)" */
    @Override
    public String toString() {
        return getId() + " " + getName() + " (" + getClass().getSimpleName() + ")";
    }
}
