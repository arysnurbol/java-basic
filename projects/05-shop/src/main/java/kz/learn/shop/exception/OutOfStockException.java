package kz.learn.shop.exception;

/**
 * Қоймада тауар жеткіліксіз.
 *
 * getMessage(): "Not enough P1: requested 3, available 2"
 */
public class OutOfStockException extends ShopException {

    // TODO: өрістерді жаз

    public OutOfStockException(String productId, int requested, int available) {
        super("TODO");
    }

    public String getProductId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getRequested() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int getAvailable() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
