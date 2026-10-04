package kz.learn.library.exception;

/**
 * Бір нәрсе id бойынша табылмаса. Барлық сущность үшін БІР класс — id типі әртүрлі
 * (Long, String), сондықтан өріс типі — Object.
 *
 * getMessage() пішімі: "Item not found: 5", "Member not found: S-1"
 */
public class NotFoundException extends LibraryException {

    // TODO: өрістерді жаз

    public NotFoundException(String entity, Object id) {
        super("TODO"); // TODO: хабарламаны дұрыс құрастыр
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getEntity() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Object getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
