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

    private final Repository<Product, String> products;
    private final Repository<Order, String> orders;
    private final Map<String, Integer> stock = new HashMap<>();
    private int orderCounter;

    /** products, orders — null болмайды. */
    public ShopService(Repository<Product, String> products, Repository<Order, String> orders) {
        this.products = Objects.requireNonNull(products, "products");
        this.orders = Objects.requireNonNull(orders, "orders");
    }

    // ---------------------------------------------------------------- Қадам 7: операциялар

    /**
     * Жаңа тауар, қалдығы 0. Алдымен new Product(...) жаса (тексеріс пен strip сонда),
     * сосын: сондай артикул бар -> ShopException("Product already exists: P1").
     */
    public Product addProduct(String id, String name, Category category, long price) {
        Product product = new Product(id, name, category, Money.of(price));
        if (products.existsById(product.id())) {
            throw new ShopException("Product already exists: " + product.id());
        }
        products.save(product);
        stock.put(product.id(), 0);
        return product;
    }

    /** Табылмаса — ProductNotFoundException(id). */
    public Product getProduct(String id) {
        return products.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    /** Табылмаса — OrderNotFoundException(id). */
    public Order getOrder(String id) {
        return orders.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    /** Каталог: санат бойынша (enum ретімен), тең болса — артикул бойынша. */
    public List<Product> catalog() {
        return products.findAll().stream()
                .sorted(Comparator.comparing(Product::category).thenComparing(Product::id))
                .toList();
    }

    /**
     * Қоймаға түсіру. Тауар жоқ -> ProductNotFoundException;
     * quantity <= 0 -> IllegalArgumentException("quantity must be positive"). Кеңес: Map.merge.
     */
    public void restock(String productId, int quantity) {
        getProduct(productId);
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        stock.merge(productId, quantity, Integer::sum);
    }

    /** Қоймадағы қалдық. Тауар жоқ -> ProductNotFoundException. */
    public int stockOf(String productId) {
        return stock.getOrDefault(getProduct(productId).id(), 0);
    }

    /**
     * Жаңа тапсырыс (NEW). Сәтсіз жасау нөмірді «жемейді»: есептегішті new Order(...) сәтті
     * өткеннен КЕЙІН арттыр (HotelService.book-тағыдай).
     */
    public Order createOrder(String customer, DeliveryType delivery) {
        Order order = new Order(String.format("ORD-%04d", orderCounter + 1), customer, delivery);
        orderCounter++;
        return orders.save(order);
    }

    /**
     * Тапсырысқа тауар қосу: тапсырыс пен тауарды тап, order.addItem(...). Қайтарады: тапсырыс.
     * Қалдық мұнда ТЕКСЕРІЛМЕЙДІ — себетке салу тауарды ұстап қалмайды, ол тек төлегенде есептен шығады.
     */
    public Order addItem(String orderId, String productId, int quantity) {
        Order order = getOrder(orderId);
        order.addItem(getProduct(productId), quantity);
        return order;
    }

    /** order.removeItem(...). Қайтарады: тапсырыс. */
    public Order removeItem(String orderId, String productId) {
        Order order = getOrder(orderId);
        order.removeItem(productId);
        return order;
    }

    /** Промокод: DiscountFactory.fromCode(code), сосын order.applyDiscount(...). Бос код жеңілдікті алып тастайды. */
    public Order applyPromo(String orderId, String code) {
        Order order = getOrder(orderId);
        order.applyDiscount(DiscountFactory.fromCode(code));
        return order;
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
        Order order = getOrder(orderId);
        for (OrderLine line : order.getLines()) {
            int available = stockOf(line.product().id());
            if (line.quantity() > available) {
                throw new OutOfStockException(line.product().id(), line.quantity(), available);
            }
        }
        order.pay();
        for (OrderLine line : order.getLines()) {
            stock.merge(line.product().id(), -line.quantity(), Integer::sum);
        }
        return order.total();
    }

    public void ship(String orderId) {
        getOrder(orderId).ship();
    }

    public void deliver(String orderId) {
        getOrder(orderId).deliver();
    }

    /**
     * Болдырмау. Төленген тапсырыс болдырылса — тауар қоймаға қайтады, ақша толық қайтарылады.
     *   wasPaid = getStatus().isPaid();
     *   order.cancel() — рұқсат жоқ болса, осы жерде қате, ештеңе өзгермейді;
     *   wasPaid болса: қалдықтарды қайтар және order.total() қайтар; әйтпесе — Money.ZERO.
     */
    public Money cancel(String orderId) {
        Order order = getOrder(orderId);
        boolean wasPaid = order.getStatus().isPaid();
        order.cancel();
        if (!wasPaid) {
            return Money.ZERO;
        }
        for (OrderLine line : order.getLines()) {
            stock.merge(line.product().id(), line.quantity(), Integer::sum);
        }
        return order.total();
    }

    // ---------------------------------------------------------------- Қадам 8: есептер

    /** Клиенттің барлық тапсырысы (кез келген күйде), аты регистрге қарамай (strip), нөмір бойынша. */
    public List<Order> ordersOf(String customer) {
        String name = customer.strip();
        return orders.findWhere(o -> o.getCustomer().equalsIgnoreCase(name)).stream()
                .sorted(Comparator.comparing(Order::getId))
                .toList();
    }

    /** Күй бойынша тапсырыс саны. Тек кездесетін күйлер; EnumMap. Кеңес: groupingBy + counting. */
    public Map<OrderStatus, Long> countByStatus() {
        return orders.findAll().stream()
                .collect(Collectors.groupingBy(Order::getStatus, () -> new EnumMap<>(OrderStatus.class),
                        Collectors.counting()));
    }

    /** Табыс: төленген (getStatus().isPaid()) тапсырыстардың total() қосындысы, жеткізуімен бірге. */
    public Money revenue() {
        return paidOrders().stream().map(Order::total).reduce(Money.ZERO, Money::plus);
    }

    /**
     * Санат бойынша сатылым: төленген тапсырыстардың ЖОЛДАРЫ (line.total(), жеңілдікке дейін).
     * Тек сатылымы бар санаттар; EnumMap. Кеңес: flatMap -> groupingBy + reducing (04-hotel revenueByType).
     */
    public Map<Category, Money> salesByCategory() {
        return paidOrders().stream()
                .flatMap(order -> order.getLines().stream())
                .collect(Collectors.groupingBy(line -> line.product().category(),
                        () -> new EnumMap<>(Category.class),
                        Collectors.reducing(Money.ZERO, OrderLine::total, Money::plus)));
    }

    /**
     * Ең көп сатылған n тауар: төленген тапсырыстар, сатылған дана бойынша кему ретімен, тең болса — артикул бойынша.
     * Кеңес: groupingBy(артикул, summingInt) -> entrySet().stream() -> sorted -> limit -> getProduct.
     */
    public List<Product> topProducts(int n) {
        return paidOrders().stream()
                .flatMap(order -> order.getLines().stream())
                .collect(Collectors.groupingBy(line -> line.product().id(), Collectors.summingInt(OrderLine::quantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(entry -> getProduct(entry.getKey()))
                .toList();
    }

    /** Төленген тапсырыстар (PAID, SHIPPED, DELIVERED). */
    private List<Order> paidOrders() {
        return orders.findWhere(order -> order.getStatus().isPaid());
    }
}
