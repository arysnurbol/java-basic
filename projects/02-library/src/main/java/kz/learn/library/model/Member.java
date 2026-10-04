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

    // TODO: өрістерді жаз

    protected Member(String cardNumber, String name) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getName() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Бір уақытта ең көп неше экземпляр ұстай алады. */
    public abstract int maxLoans();

    /** Есептелген айыппұлға жеңілдік қолданады. Әдепкі — жеңілдік жоқ. */
    public long applyDiscount(long fine) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int hashCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. Пішім: "S-1 Aru (StudentMember)" */
    @Override
    public String toString() {
        return getId() + " " + getName() + " (" + getClass().getSimpleName() + ")";
    }
}
