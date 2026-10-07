package kz.learn.shop.delivery;

import kz.learn.shop.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("Қадам 2 — DeliveryType: әр константаның өз денесі бар enum")
class Step2DeliveryTest {

    @Test
    @DisplayName("PICKUP: әрқашан тегін")
    void pickup() {
        assertEquals(Money.ZERO, DeliveryType.PICKUP.costFor(Money.of(500), 1));
        assertEquals(Money.ZERO, DeliveryType.PICKUP.costFor(Money.of(1_000_000), 40));
    }

    @Test
    @DisplayName("COURIER: 1 500 ₸, 20 000 ₸ және одан көп болса — тегін")
    void courier() {
        assertEquals(Money.of(1_500), DeliveryType.COURIER.costFor(Money.of(5_000), 3));
        assertEquals(Money.of(1_500), DeliveryType.COURIER.costFor(Money.of("19999.99"), 1), "бір тиын жетпейді");
        assertEquals(Money.ZERO, DeliveryType.COURIER.costFor(Money.of(20_000), 1), "дәл 20 000 — шекара кіреді");
        assertEquals(Money.ZERO, DeliveryType.COURIER.costFor(Money.of(350_000), 1));
    }

    @Test
    @DisplayName("POST: 1 000 ₸ + әр данаға 200 ₸, сомаға қарамайды")
    void post() {
        assertEquals(Money.of(1_200), DeliveryType.POST.costFor(Money.of(500), 1));
        assertEquals(Money.of(1_600), DeliveryType.POST.costFor(Money.of(500), 3));
        assertEquals(Money.of(3_000), DeliveryType.POST.costFor(Money.of(1_000_000), 10));
        assertEquals(Money.of(1_000), DeliveryType.POST.costFor(Money.ZERO, 0), "бос тапсырыс");
    }

    @Test
    @DisplayName("денесі бар константа — enum-ның анонимді ұрпағы, бірақ типі бәрібір DeliveryType")
    void constantBodies() {
        assertNotSame(DeliveryType.class, DeliveryType.COURIER.getClass(), "getClass() — анонимді ішкі класс (DeliveryType$2)");
        assertSame(DeliveryType.class, DeliveryType.COURIER.getDeclaringClass(), "getDeclaringClass() — enum-ның өзі");
        assertSame(DeliveryType.POST, DeliveryType.valueOf("POST"));
    }
}
