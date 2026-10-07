package kz.learn.shop.exception;

/**
 * Ондай нөмірлі тапсырыс жоқ.
 *
 * getMessage(): "Order not found: ORD-0009"
 */
public class OrderNotFoundException extends ShopException {

    private final String id;

    public OrderNotFoundException(String id) {
        super("Order not found: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
