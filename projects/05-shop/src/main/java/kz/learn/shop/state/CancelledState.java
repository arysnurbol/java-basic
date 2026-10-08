package kz.learn.shop.state;

/** Болдырылмады — соңғы күй: ешбір әрекет рұқсат етілмейді. */
public final class CancelledState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.CANCELLED;
    }
}
