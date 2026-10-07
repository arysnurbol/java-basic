package kz.learn.shop.state;

/** Жолда: тек жеткізуге болады. Болдыруға БОЛМАЙДЫ — тауар қоймадан кетіп қалды. */
public final class ShippedState implements OrderState {

    @Override
    public OrderStatus status() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public OrderState deliver() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
