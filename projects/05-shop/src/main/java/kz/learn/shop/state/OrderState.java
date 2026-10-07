package kz.learn.shop.state;

import kz.learn.shop.exception.OrderStateException;

/**
 * Тапсырыс күйі — STATE үлгісі.
 *
 * Әр күй — жеке класс. Ол «менде қандай әрекет рұқсат етілген және одан кейін қай күйге өтемін»
 * дегенді өзі біледі. Order (context) if/switch жазбайды: state = state.ship(); — болды.
 *
 *         pay()          ship()           deliver()
 *   NEW ───────▶ PAID ────────▶ SHIPPED ──────────▶ DELIVERED
 *    │            │
 *    └─ cancel() ─┴──────────▶ CANCELLED
 *
 * Әдепкі (default) жүзеге асыру: әрекет РҰҚСАТ ЕТІЛМЕЙДІ -> OrderStateException.
 * Әр күй тек өзіне рұқсат етілген әрекеттерді override етеді. Әдіс күйді ӨЗГЕРТПЕЙДІ —
 * келесі күйді ҚАЙТАРАДЫ, ал оны сақтау — Order-дің ісі.
 *
 * Хабарлама: "Cannot ship order in status NEW" — оны private әдіс құрастырады.
 */
public interface OrderState {

    /** Осы күйдің атауы. */
    OrderStatus status();

    /** Тапсырыс құрамын (тауарлар, промокод) өзгертуге бола ма. Әдепкі — жоқ. */
    default boolean canEdit() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Әдепкі: рұқсат жоқ -> throw notAllowed("pay"). ship/deliver/cancel — дәл осылай. */
    default OrderState pay() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    default OrderState ship() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    default OrderState deliver() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    default OrderState cancel() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Интерфейстегі private әдіс (Java 9+): default әдістер ортақ кодты осылай бөліседі,
     * ал сырттағылар (Order, тесттер) оны көрмейді.
     * Қайтарады (лақтырмайды!): new OrderStateException("Cannot " + action + " order in status " + status()).
     */
    private OrderStateException notAllowed(String action) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
