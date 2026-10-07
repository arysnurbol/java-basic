package kz.learn.shop.state;

import kz.learn.shop.exception.OrderStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 5 — State: күйлер және ауысулар")
class Step5StateTest {

    private static void assertNotAllowed(OrderState state, String action, UnaryOperator<OrderState> call) {
        OrderStateException e = assertThrows(OrderStateException.class, () -> call.apply(state),
                state.status() + " күйінде " + action + " рұқсат етілмеуі керек");
        assertEquals("Cannot " + action + " order in status " + state.status(), e.getMessage());
    }

    @Test
    @DisplayName("әр күй өз атауын біледі")
    void statuses() {
        assertEquals(OrderStatus.NEW, new NewState().status());
        assertEquals(OrderStatus.PAID, new PaidState().status());
        assertEquals(OrderStatus.SHIPPED, new ShippedState().status());
        assertEquals(OrderStatus.DELIVERED, new DeliveredState().status());
        assertEquals(OrderStatus.CANCELLED, new CancelledState().status());
    }

    @Test
    @DisplayName("бақытты жол: NEW -> PAID -> SHIPPED -> DELIVERED")
    void happyPath() {
        OrderState state = new NewState();

        state = state.pay();
        assertInstanceOf(PaidState.class, state);
        state = state.ship();
        assertInstanceOf(ShippedState.class, state);
        state = state.deliver();
        assertInstanceOf(DeliveredState.class, state);
    }

    @Test
    @DisplayName("болдырмау: NEW мен PAID-тан ғана")
    void cancel() {
        assertInstanceOf(CancelledState.class, new NewState().cancel());
        assertInstanceOf(CancelledState.class, new PaidState().cancel());
        assertNotAllowed(new ShippedState(), "cancel", OrderState::cancel);
        assertNotAllowed(new DeliveredState(), "cancel", OrderState::cancel);
        assertNotAllowed(new CancelledState(), "cancel", OrderState::cancel);
    }

    @Test
    @DisplayName("рұқсат етілмеген ауысулар -> OrderStateException, хабарламада әрекет пен күй")
    void notAllowed() {
        assertNotAllowed(new NewState(), "ship", OrderState::ship);
        assertNotAllowed(new NewState(), "deliver", OrderState::deliver);
        assertNotAllowed(new PaidState(), "pay", OrderState::pay);
        assertNotAllowed(new PaidState(), "deliver", OrderState::deliver);
        assertNotAllowed(new ShippedState(), "pay", OrderState::pay);
        assertNotAllowed(new ShippedState(), "ship", OrderState::ship);
    }

    @Test
    @DisplayName("соңғы күйлер: ешбір әрекет жоқ")
    void finalStates() {
        for (OrderState state : List.of(new DeliveredState(), new CancelledState())) {
            assertNotAllowed(state, "pay", OrderState::pay);
            assertNotAllowed(state, "ship", OrderState::ship);
            assertNotAllowed(state, "deliver", OrderState::deliver);
        }
    }

    @Test
    @DisplayName("canEdit: тек NEW")
    void canEdit() {
        assertTrue(new NewState().canEdit());
        for (OrderState state : List.of(new PaidState(), new ShippedState(), new DeliveredState(), new CancelledState())) {
            assertFalse(state.canEdit(), state.status().toString());
        }
    }

    @Test
    @DisplayName("ауысу ескі күйді өзгертпейді — жаңасын қайтарады")
    void transitionsReturnNewState() {
        NewState created = new NewState();
        created.pay();
        assertEquals(OrderStatus.NEW, created.status());
        assertTrue(created.canEdit());
    }

    @Test
    @DisplayName("OrderStatus.isPaid: PAID, SHIPPED, DELIVERED")
    void paidFlag() {
        assertFalse(OrderStatus.NEW.isPaid());
        assertTrue(OrderStatus.PAID.isPaid());
        assertTrue(OrderStatus.SHIPPED.isPaid());
        assertTrue(OrderStatus.DELIVERED.isPaid());
        assertFalse(OrderStatus.CANCELLED.isPaid());
    }
}
