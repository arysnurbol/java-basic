package kz.learn.library.exception;

/**
 * Бір нәрсе id бойынша табылмаса. Барлық сущность үшін БІР класс — id типі әртүрлі
 * (Long, String), сондықтан өріс типі — Object.
 *
 * getMessage() пішімі: "Item not found: 5", "Member not found: S-1"
 */
public class NotFoundException extends LibraryException {

    private final String entity;
    private final Object id;

    public NotFoundException(String entity, Object id) {
        super(entity + " not found: " + id);
        this.entity = entity;
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public Object getId() {
        return id;
    }
}
