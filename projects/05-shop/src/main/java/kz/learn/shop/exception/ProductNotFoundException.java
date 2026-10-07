package kz.learn.shop.exception;

/**
 * Ондай артикулы бар тауар жоқ.
 *
 * getMessage(): "Product not found: P9"
 */
public class ProductNotFoundException extends ShopException {

    // TODO: өрістерді жаз

    public ProductNotFoundException(String id) {
        super("TODO");
    }

    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
