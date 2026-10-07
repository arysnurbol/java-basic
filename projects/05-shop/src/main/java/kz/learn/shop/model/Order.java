package kz.learn.shop.model;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.discount.DiscountPolicy;
import kz.learn.shop.exception.OrderStateException;
import kz.learn.shop.exception.ProductNotFoundException;
import kz.learn.shop.state.NewState;
import kz.learn.shop.state.OrderState;
import kz.learn.shop.state.OrderStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Тапсырыс — State үлгісінің CONTEXT-і. Өзгермелі, бірақ инкапсуляцияланған (Booking сияқты).
 *
 * Өрістер:
 *   id, customer, delivery — final;
 *   lines — Map<String, OrderLine> (кілті — артикул), LinkedHashMap: қосылған реті сақталады;
 *   discount — DiscountPolicy.none()-ден басталады;
 *   state — new NewState()-тен басталады.
 *
 * Күйге қатысты if/switch МҰНДА ЖОҚ. Әр әрекет күйге тапсырылады: state = state.ship().
 * Тапсырыс құрамы тек NEW күйінде өзгереді, сондықтан төлегеннен кейін сомалар өзгермейді —
 * оларды сақтап қоюдың қажеті жоқ, әр жолы есептей береміз.
 */
public class Order implements Identifiable<String> {

    // TODO: өрістерді жаз

    /**
     * id, customer — бос емес, strip (requireText);  delivery — Objects.requireNonNull(delivery, "delivery").
     */
    public Order(String id, String customer, DeliveryType delivery) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getCustomer() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public DeliveryType getDelivery() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Күйдің атауын күйдің өзінен сұра. */
    public OrderStatus getStatus() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жолдар қосылған ретімен; қайтқан тізімді өзгерту тапсырысқа әсер етпейді (List.copyOf). */
    public List<OrderLine> getLines() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Тауар қосу. Тек өзгертуге болатын күйде (requireEditable).
     * Бұл тауар бұрыннан бар болса — санын қос: жаңа OrderLine(product, ескі + quantity) (99-дан асса —
     * OrderLine өзі IllegalArgumentException береді, ескі жол өзгеріссіз қалады).
     * Тұзақ: quantity-ді де тексер — ескі 2 + 0 = 2 жарамды сан, бірақ «0 дана қосу» қате.
     * Кеңес: алдымен new OrderLine(product, quantity) (тексеріс осында), сосын Map.merge.
     */
    public void addItem(Product product, int quantity) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Тауарды толық өшіру. Тек өзгертуге болатын күйде.
     * Ондай тауар тапсырыста жоқ -> ProductNotFoundException(productId).
     */
    public void removeItem(String productId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жеңілдікті ауыстыру (соңғысы жарайды). Тек өзгертуге болатын күйде; policy — null емес. */
    public void applyDiscount(DiscountPolicy policy) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Барлық дана саны (P1 x2 + P2 x3 -> 5). */
    public int units() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жолдар сомасы, жеңілдікке дейін. Бос тапсырыс — Money.ZERO. */
    public Money subtotal() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жеңілдік сомасы: discount.discountFor(subtotal()). */
    public Money discount() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жеткізу құны: delivery.costFor(жеңілдіктен кейінгі сома, units()). */
    public Money deliveryCost() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Төлейтін сома: subtotal - discount + deliveryCost. */
    public Money total() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Төлеу. «Алдымен тексер, сосын өзгерт»:
     *   1) next = state.pay() — күй рұқсат бермесе, осы жерде OrderStateException;
     *   2) тапсырыс бос -> OrderStateException("Cannot pay empty order");
     *   3) state = next.
     * (1 мен 2-нің ретін ауыстырсаң: CANCELLED болған бос тапсырыс қандай хабарлама береді?)
     */
    public void pay() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** state = state.ship(). */
    public void ship() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** state = state.deliver(). */
    public void deliver() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** state = state.cancel(). */
    public void cancel() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Өзгертуге болмаса -> OrderStateException("Order ORD-0001 cannot be changed in status PAID"). */
    private void requireEditable() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }

    /** Тек id бойынша. */
    @Override
    public boolean equals(Object o) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int hashCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. "ORD-0001: Aru, COURIER, 3 дана, 701500.00 ₸, PAID" */
    @Override
    public String toString() {
        return getId() + ": " + getCustomer() + ", " + getDelivery() + ", " + units() + " дана, "
                + total() + ", " + getStatus();
    }
}
