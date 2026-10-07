package kz.learn.shop.state;

/** Төленген: жіберуге болады; болдырмауға болады (ақша қайтарылады). */
public final class PaidState implements OrderState {

    @Override
    public OrderStatus status() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public OrderState ship() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public OrderState cancel() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
