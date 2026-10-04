package kz.learn.library.exception;

/**
 * Экземпляр қазір басқа оқырманда.
 * getMessage() пішімі: "Item is already on loan: id=3"
 */
public class ItemNotAvailableException extends LibraryException {

    private final long itemId;

    public ItemNotAvailableException(long itemId) {

        super("Item is already on loan: id=" + itemId);
        this.itemId = itemId;

    }

    public long getItemId() {
        return itemId;
    }
}
