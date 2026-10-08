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

    private final String id;
    private final String customer;
    private final DeliveryType delivery;
    private final Map<String, OrderLine> lines = new LinkedHashMap<>();
    private DiscountPolicy discount = DiscountPolicy.none();
    private OrderState state = new NewState();

    /**
     * id, customer — бос емес, strip (requireText);  delivery — Objects.requireNonNull(delivery, "delivery").
     */
    public Order(String id, String customer, DeliveryType delivery) {
        this.id = requireText(id, "id");
        this.customer = requireText(customer, "customer");
        this.delivery = Objects.requireNonNull(delivery, "delivery");
    }

    @Override
    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public DeliveryType getDelivery() {
        return delivery;
    }

    /** Күйдің атауын күйдің өзінен сұра. */
    public OrderStatus getStatus() {
        return state.status();
    }

    /** Жолдар қосылған ретімен; қайтқан тізімді өзгерту тапсырысқа әсер етпейді (List.copyOf). */
    public List<OrderLine> getLines() {
        return List.copyOf(lines.values());
    }

    /**
     * Тауар қосу. Тек өзгертуге болатын күйде (requireEditable).
     * Бұл тауар бұрыннан бар болса — санын қос: жаңа OrderLine(product, ескі + quantity) (99-дан асса —
     * OrderLine өзі IllegalArgumentException береді, ескі жол өзгеріссіз қалады).
     * Тұзақ: quantity-ді де тексер — ескі 2 + 0 = 2 жарамды сан, бірақ «0 дана қосу» қате.
     * Кеңес: алдымен new OrderLine(product, quantity) (тексеріс осында), сосын Map.merge.
     */
    public void addItem(Product product, int quantity) {
        requireEditable();
        OrderLine added = new OrderLine(product, quantity);
        lines.merge(product.id(), added,
                (old, add) -> new OrderLine(old.product(), old.quantity() + add.quantity()));
    }

    /**
     * Тауарды толық өшіру. Тек өзгертуге болатын күйде.
     * Ондай тауар тапсырыста жоқ -> ProductNotFoundException(productId).
     */
    public void removeItem(String productId) {
        requireEditable();
        if (lines.remove(productId) == null) {
            throw new ProductNotFoundException(productId);
        }
    }

    /** Жеңілдікті ауыстыру (соңғысы жарайды). Тек өзгертуге болатын күйде; policy — null емес. */
    public void applyDiscount(DiscountPolicy policy) {
        requireEditable();
        this.discount = Objects.requireNonNull(policy, "policy");
    }

    /** Барлық дана саны (P1 x2 + P2 x3 -> 5). */
    public int units() {
        return lines.values().stream().mapToInt(OrderLine::quantity).sum();
    }

    /** Жолдар сомасы, жеңілдікке дейін. Бос тапсырыс — Money.ZERO. */
    public Money subtotal() {
        return lines.values().stream().map(OrderLine::total).reduce(Money.ZERO, Money::plus);
    }

    /** Жеңілдік сомасы: discount.discountFor(subtotal()). */
    public Money discount() {
        return discount.discountFor(subtotal());
    }

    /** Жеткізу құны: delivery.costFor(жеңілдіктен кейінгі сома, units()). */
    public Money deliveryCost() {
        return delivery.costFor(subtotal().minus(discount()), units());
    }

    /** Төлейтін сома: subtotal - discount + deliveryCost. */
    public Money total() {
        return subtotal().minus(discount()).plus(deliveryCost());
    }

    /**
     * Төлеу. «Алдымен тексер, сосын өзгерт»:
     *   1) next = state.pay() — күй рұқсат бермесе, осы жерде OrderStateException;
     *   2) тапсырыс бос -> OrderStateException("Cannot pay empty order");
     *   3) state = next.
     * (1 мен 2-нің ретін ауыстырсаң: CANCELLED болған бос тапсырыс қандай хабарлама береді?)
     */
    public void pay() {
        OrderState next = state.pay();
        if (lines.isEmpty()) {
            throw new OrderStateException("Cannot pay empty order");
        }
        state = next;
    }

    /** state = state.ship(). */
    public void ship() {
        state = state.ship();
    }

    /** state = state.deliver(). */
    public void deliver() {
        state = state.deliver();
    }

    /** state = state.cancel(). */
    public void cancel() {
        state = state.cancel();
    }

    /** Өзгертуге болмаса -> OrderStateException("Order ORD-0001 cannot be changed in status PAID"). */
    private void requireEditable() {
        if (!state.canEdit()) {
            throw new OrderStateException("Order " + id + " cannot be changed in status " + getStatus());
        }
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
        return this == o || o instanceof Order other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /** ДАЙЫН. "ORD-0001: Aru, COURIER, 3 дана, 701500.00 ₸, PAID" */
    @Override
    public String toString() {
        return getId() + ": " + getCustomer() + ", " + getDelivery() + ", " + units() + " дана, "
                + total() + ", " + getStatus();
    }
}
