package kz.learn.shop.state;

/** Жолда: тек жеткізуге болады. Болдыруға БОЛМАЙДЫ — тауар қоймадан кетіп қалды. */
public final class ShippedState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.SHIPPED;
    }

    @Override
    public OrderState deliver() {
        return new DeliveredState();
    }
}
