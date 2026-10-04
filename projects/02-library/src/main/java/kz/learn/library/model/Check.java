package kz.learn.library.model;

/**
 * Кіріс деректерді тексеретін утилита-класс. Item де, Member де қолданады (DRY).
 *
 * Утилита-класс үлгісі: final (мұрагерлік жоқ), private конструктор (объект жасалмайды),
 * тек static методтар. JDK-да осындай: Math, Objects, Collections.
 *
 * Қате дерек — IllegalArgumentException (JDK-ның стандартты exception-ы):
 * бұл бизнес-ереже емес, "дұрыс емес аргумент" — сондықтан LibraryException емес.
 */
public final class Check {

    private Check() {
    }

    /**
     * value null немесе бос (тек бос орын да) болса — IllegalArgumentException,
     * хабарламада field аты болсын: "title must not be blank".
     * Әйтпесе — шетіндегі бос орындары алынған (trim) мәнді қайтарады.
     */
    public static String text(String value, String field) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * value <= 0 болса — IllegalArgumentException: "minutes must be positive".
     * Әйтпесе value-ді өзгеріссіз қайтарады.
     */
    public static int positive(int value, String field) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
