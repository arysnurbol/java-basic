package kz.learn.shop.delivery;

import kz.learn.shop.model.Money;

/**
 * Жеткізу тәсілі — ӘР КОНСТАНТАНЫҢ ӨЗ ДЕНЕСІ БАР enum (constant-specific class body).
 *
 * Әзірге enum-да тек өрістер болды (RoomType(capacity, basePrice)). Мұнда әр константа абстрактілі
 * costFor әдісін ӨЗІНШЕ жүзеге асырады — бұл да Strategy, бірақ стратегиялар саны тұрақты
 * және бәрі бір файлда. switch керек емес: DeliveryType.COURIER.costFor(...) өз денесін шақырады.
 *
 * Ережелер (goods — жеңілдіктен КЕЙІНГІ тауар сомасы, units — барлық дана саны):
 *   PICKUP  — өзі алып кету: тегін;
 *   COURIER — 1 500 ₸; goods >= 20 000 ₸ болса — тегін;
 *   POST    — 1 000 ₸ + әр данаға 200 ₸ (3 дана -> 1 600 ₸).
 */
public enum DeliveryType {

    PICKUP {
        @Override
        public Money costFor(Money goods, int units) {
            return Money.ZERO;
        }
    },

    COURIER {
        @Override
        public Money costFor(Money goods, int units) {
            return goods.compareTo(Money.of(20_000)) >= 0 ? Money.ZERO : Money.of(1_500);
        }
    },

    POST {
        @Override
        public Money costFor(Money goods, int units) {
            return Money.of(1_000).plus(Money.of(200).times(units));
        }
    };

    /** Жеткізу құны. goods — null емес, units >= 0. */
    public abstract Money costFor(Money goods, int units);
}
