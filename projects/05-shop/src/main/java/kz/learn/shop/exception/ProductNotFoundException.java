package kz.learn.shop.exception;

/**
 * Ондай артикулы бар тауар жоқ.
 *
 * getMessage(): "Product not found: P9"
 */
public class ProductNotFoundException extends ShopException {

    private final String id;

    public ProductNotFoundException(String id) {
        super("Product not found: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
