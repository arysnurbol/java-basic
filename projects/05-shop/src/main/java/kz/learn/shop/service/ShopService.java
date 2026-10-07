package kz.learn.shop.service;

import kz.learn.shop.delivery.DeliveryType;
import kz.learn.shop.discount.DiscountFactory;
import kz.learn.shop.exception.OrderNotFoundException;
import kz.learn.shop.exception.OutOfStockException;
import kz.learn.shop.exception.ProductNotFoundException;
import kz.learn.shop.exception.ShopException;
import kz.learn.shop.model.Category;
import kz.learn.shop.model.Money;
import kz.learn.shop.model.Order;
import kz.learn.shop.model.OrderLine;
import kz.learn.shop.model.Product;
import kz.learn.shop.repository.Repository;
import kz.learn.shop.state.OrderStatus;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Дүкеннің бизнес-логикасы. Үш үлгі осында кездеседі:
 *   STATE    — сервис тапсырыстың күйін тексермейді, order.pay()/ship()/... өзі шешеді;
 *   STRATEGY — жеңілдік пен жеткізу құнын Order DiscountPolicy мен DeliveryType-тан сұрайды;
 *   FACTORY  — промокодты DiscountFactory.fromCode(...) жеңілдікке айналдырады.
 *
 * Өрістер:
 *   products, orders — репозиторийлер, конструктор арқылы (DI);
 *   stock — Map<String, Integer> (артикул -> дана), HashMap: тауар өзгермейді, ал қалдық үнемі өзгереді,
 *           сондықтан ол Product-та емес, осында;
 *   orderCounter — соңғы берілген тапсырыс нөмірі.
 *
 * Тапсырыс нөмірлері: "ORD-0001", "ORD-0002", ... — String.format("ORD-%04d", n).
 */
public class ShopService {

    // TODO: өрістерді жаз

    /** products, orders — null болмайды. */
    public ShopService(Repository<Product, String> products, Repository<Order, String> orders) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 7: операциялар

    /**
     * Жаңа тауар, қалдығы 0. Алдымен new Product(...) жаса (тексеріс пен strip сонда),
     * сосын: сондай артикул бар -> ShopException("Product already exists: P1").
     */
    public Product addProduct(String id, String name, Category category, long price) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — ProductNotFoundException(id). */
    public Product getProduct(String id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — OrderNotFoundException(id). */
    public Order getOrder(String id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Каталог: санат бойынша (enum ретімен), тең болса — артикул бойынша. */
    public List<Product> catalog() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Қоймаға түсіру. Тауар жоқ -> ProductNotFoundException;
     * quantity <= 0 -> IllegalArgumentException("quantity must be positive"). Кеңес: Map.merge.
     */
    public void restock(String productId, int quantity) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Қоймадағы қалдық. Тауар жоқ -> ProductNotFoundException. */
    public int stockOf(String productId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Жаңа тапсырыс (NEW). Сәтсіз жасау нөмірді «жемейді»: есептегішті new Order(...) сәтті
     * өткеннен КЕЙІН арттыр (HotelService.book-тағыдай).
     */
    public Order createOrder(String customer, DeliveryType delivery) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Тапсырысқа тауар қосу: тапсырыс пен тауарды тап, order.addItem(...). Қайтарады: тапсырыс.
     * Қалдық мұнда ТЕКСЕРІЛМЕЙДІ — себетке салу тауарды ұстап қалмайды, ол тек төлегенде есептен шығады.
     */
    public Order addItem(String orderId, String productId, int quantity) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** order.removeItem(...). Қайтарады: тапсырыс. */
    public Order removeItem(String orderId, String productId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Промокод: DiscountFactory.fromCode(code), сосын order.applyDiscount(...). Бос код жеңілдікті алып тастайды. */
    public Order applyPromo(String orderId, String code) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Төлеу. «Алдымен тексер, сосын өзгерт» (банктегі transfer сияқты):
     *  1) тапсырысты тап;
     *  2) ӘР жолдың қалдығын тексер, жетпесе -> OutOfStockException(артикул, сұралған, бар)
     *     (жолдар ретімен — бірінші жетпегені);
     *  3) order.pay() — күйді және бос тапсырысты өзі тексереді;
     *  4) енді ғана қалдықтарды азайт.
     * 2–3-қадамда қате болса, қойма да, тапсырыс та бұрынғыдай қалады.
     * Қайтарады: order.total().
     */
    public Money pay(String orderId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public void ship(String orderId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public void deliver(String orderId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Болдырмау. Төленген тапсырыс болдырылса — тауар қоймаға қайтады, ақша толық қайтарылады.
     *   wasPaid = getStatus().isPaid();
     *   order.cancel() — рұқсат жоқ болса, осы жерде қате, ештеңе өзгермейді;
     *   wasPaid болса: қалдықтарды қайтар және order.total() қайтар; әйтпесе — Money.ZERO.
     */
    public Money cancel(String orderId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 8: есептер

    /** Клиенттің барлық тапсырысы (кез келген күйде), аты регистрге қарамай (strip), нөмір бойынша. */
    public List<Order> ordersOf(String customer) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Күй бойынша тапсырыс саны. Тек кездесетін күйлер; EnumMap. Кеңес: groupingBy + counting. */
    public Map<OrderStatus, Long> countByStatus() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табыс: төленген (getStatus().isPaid()) тапсырыстардың total() қосындысы, жеткізуімен бірге. */
    public Money revenue() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Санат бойынша сатылым: төленген тапсырыстардың ЖОЛДАРЫ (line.total(), жеңілдікке дейін).
     * Тек сатылымы бар санаттар; EnumMap. Кеңес: flatMap -> groupingBy + reducing (04-hotel revenueByType).
     */
    public Map<Category, Money> salesByCategory() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Ең көп сатылған n тауар: төленген тапсырыстар, сатылған дана бойынша кему ретімен, тең болса — артикул бойынша.
     * Кеңес: groupingBy(артикул, summingInt) -> entrySet().stream() -> sorted -> limit -> getProduct.
     */
    public List<Product> topProducts(int n) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Төленген тапсырыстар (PAID, SHIPPED, DELIVERED). */
    private List<Order> paidOrders() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
