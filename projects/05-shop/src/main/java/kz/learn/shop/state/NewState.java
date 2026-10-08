package kz.learn.shop.state;

/** Жаңа тапсырыс (себет): құрамын өзгертуге болады; төлеуге және болдырмауға болады. */
public final class NewState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.NEW;
    }

    @Override
    public boolean canEdit() {
        return true;
    }

    @Override
    public OrderState pay() {
        return new PaidState();
    }

    @Override
    public OrderState cancel() {
        return new CancelledState();
    }
}
