package kz.learn.shop.exception;

/**
 * Қоймада тауар жеткіліксіз.
 *
 * getMessage(): "Not enough P1: requested 3, available 2"
 */
public class OutOfStockException extends ShopException {

    private final String productId;
    private final int requested;
    private final int available;

    public OutOfStockException(String productId, int requested, int available) {
        super("Not enough " + productId + ": requested " + requested + ", available " + available);
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }

    public String getProductId() {
        return productId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
