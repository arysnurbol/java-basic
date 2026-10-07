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
            // TODO
            throw new UnsupportedOperationException("TODO");
        }
    },

    COURIER {
        @Override
        public Money costFor(Money goods, int units) {
            // TODO
            throw new UnsupportedOperationException("TODO");
        }
    },

    POST {
        @Override
        public Money costFor(Money goods, int units) {
            // TODO
            throw new UnsupportedOperationException("TODO");
        }
    };

    /** Жеткізу құны. goods — null емес, units >= 0. */
    public abstract Money costFor(Money goods, int units);
}
