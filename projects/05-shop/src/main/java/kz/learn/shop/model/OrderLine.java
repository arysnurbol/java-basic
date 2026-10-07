package kz.learn.shop.model;

import java.util.Objects;

/**
 * Тапсырыстың бір жолы: тауар және саны. IMMUTABLE record.
 * Санын «өзгерту» = жаңа OrderLine жасау (Order.addItem осылай істейді).
 *
 * Compact конструктор:
 *   product -> Objects.requireNonNull(product, "product");
 *   quantity 1..99 болмаса -> IllegalArgumentException("quantity must be between 1 and 99").
 */
public record OrderLine(Product product, int quantity) {

    public static final int MAX_QUANTITY = 99;

    public OrderLine {
        Objects.requireNonNull(product, "product");
        if (quantity < 1 || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("quantity must be between 1 and " + MAX_QUANTITY);
        }
    }

    /** Жол сомасы: баға * саны. Кеңес: Money.times. */
    public Money total() {
        return product.price().times(quantity);
    }

    /** ДАЙЫН. "P1 Ноутбук x2 = 700000.00 ₸" */
    @Override
    public String toString() {
        return product.id() + " " + product.name() + " x" + quantity + " = " + total();
    }
}
