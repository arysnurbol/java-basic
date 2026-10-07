package kz.learn.shop.state;

/**
 * Тапсырыс күйінің АТАУЫ. ДАЙЫН.
 *
 * Назар аудар: бұл тек белгі (есептерге, экранға шығаруға). Ауысу ЕРЕЖЕЛЕРІ мұнда емес —
 * олар OrderState кластарында. (01-todo-да ережелер Status.canMoveTo()-да болған еді — салыстыр.)
 *
 * paid — тапсырыс төленген және ақшасы дүкенде қалады (табыс есебіне кіреді).
 */
public enum OrderStatus {
    NEW(false),
    PAID(true),
    SHIPPED(true),
    DELIVERED(true),
    CANCELLED(false);

    private final boolean paid;

    OrderStatus(boolean paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }
}
