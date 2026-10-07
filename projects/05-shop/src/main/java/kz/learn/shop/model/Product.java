package kz.learn.shop.model;

import java.util.Objects;

/**
 * Тауар — IMMUTABLE record (банктегі Transaction сияқты). Қоймадағы саны мұнда ЕМЕС, ShopService-те:
 * саны өзгереді, ал тауардың өзі (аты, санаты, бағасы) өзгермейді.
 *
 * Compact конструктор, тексеріс реті:
 *   id, name бос емес -> IllegalArgumentException("id must not be blank") / ("name must not be blank"),
 *                       екеуі де strip() етіліп сақталады (" P1 " -> "P1");
 *   category, price  -> Objects.requireNonNull(x, "category");
 *   price <= 0       -> IllegalArgumentException("price must be positive").
 */
public record Product(String id, String name, Category category, Money price) implements Identifiable<String> {

    public Product {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. Record-тың өз id() әдісі Identifiable-дің getId()-ын өзі жаба алмайды. */
    @Override
    public String getId() {
        return id;
    }

    /** ДАЙЫН. */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }

    /** ДАЙЫН. "P1 Ноутбук (ELECTRONICS) 350000.00 ₸" */
    @Override
    public String toString() {
        return id + " " + name + " (" + category + ") " + price;
    }
}
