package kz.learn.hotel.room;

import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Identifiable;
import kz.learn.hotel.model.Money;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Бөлме — АБСТРАКТІЛІ класс. Әр түр бір түннің бағасын ӨЗІНШЕ есептейді (priceForNight — abstract),
 * ал бүкіл кезеңнің бағасы (priceFor) — ортақ: барлық түннің қосындысы.
 * Ұрпақ қажет болса, priceFor-ды да override ете алады (Suite солай жасайды).
 *
 * Банкте мінез-құлық Strategy (композиция) арқылы берілді. Мұнда — мұрагерлік. Неге?
 * Бөлменің түрі өмір бойы өзгермейді және әр түрдің бағасы — сол түрдің өз қасиеті.
 * Ал шот тарифі ауыстырылатын, комбинацияланатын ереже еді. Құралды міндетке қарай таңда.
 *
 * Ұрпақтарды сырттан new арқылы жасау МҮМКІН ЕМЕС: олардың конструкторы package-private,
 * объект тек RoomFactory арқылы жасалады (FACTORY үлгісі).
 *
 * Өрістер: number, type — final.
 */
public abstract class Room implements Identifiable<String> {

    private final String number;
    private final RoomType type;

    /** number — requireText(...) арқылы; type — null болмайды: Objects.requireNonNull(type, "type"). */
    protected Room(String number, RoomType type) {
        this.number = requireText(number, "number");
        Objects.requireNonNull(type, "type");
        this.type = type;
    }

    /** Бөлме нөмірі. */
    @Override
    public String getId() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    /** Кеңес: type-қа тапсыр (delegation). */
    public int capacity() {
        return type.capacity();
    }

    public Money basePrice() {
        return type.basePrice();
    }

    /** Осы күннің түні үшін баға. Әр ұрпақ өзінше жазады. */
    public abstract Money priceForNight(LocalDate night);

    /**
     * Бүкіл кезеңнің бағасы: әр түннің priceForNight(...) қосындысы.
     * Кеңес: stay.nightDates().stream().map(this::priceForNight).reduce(Money.ZERO, Money::plus).
     * Назар аудар: бұл метод абстрактілі priceForNight-ты шақырады, ал оның қайсысы орындалатынын
     * объектінің нақты класы шешеді — полиморфизм (Template Method үлгісінің қарапайым түрі).
     */
    public Money priceFor(DateRange stay) {
        return stay.nightDates().stream().map(this::priceForNight).reduce(Money.ZERO, Money::plus);
    }

    /** ДАЙЫН. */
    protected static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }

    /** Тек number бойынша. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return Objects.equals(number, room.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    /** ДАЙЫН. "101 STANDARD (2 guests, 20000.00 ₸/night)" */
    @Override
    public String toString() {
        return getId() + " " + getType() + " (" + capacity() + " guests, " + basePrice() + "/night)";
    }
}
