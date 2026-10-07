package kz.learn.shop.state;

/** Жаңа тапсырыс (себет): құрамын өзгертуге болады; төлеуге және болдырмауға болады. */
public final class NewState implements OrderState {

    @Override
    public OrderStatus status() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean canEdit() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public OrderState pay() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public OrderState cancel() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
