package kz.learn.shop.exception;

/**
 * Ондай нөмірлі тапсырыс жоқ.
 *
 * getMessage(): "Order not found: ORD-0009"
 */
public class OrderNotFoundException extends ShopException {

    // TODO: өрістерді жаз

    public OrderNotFoundException(String id) {
        super("TODO");
    }

    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
