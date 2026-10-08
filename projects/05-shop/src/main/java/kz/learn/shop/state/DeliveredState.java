package kz.learn.shop.state;

/** Жеткізілді — соңғы күй: ешбір әрекет рұқсат етілмейді. */
public final class DeliveredState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.DELIVERED;
    }
}
