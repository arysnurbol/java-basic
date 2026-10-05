package kz.learn.bank.event;

import kz.learn.bank.fee.FixedFee;
import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 5 — Observer: TransactionPublisher, AuditLog, LargeTransactionAlert")
class Step5EventsTest {

    private static final LocalDateTime AT = LocalDateTime.of(2026, 10, 5, 10, 0);

    private final Account account = new Account("KZ0001", "Aru", new FixedFee(Money.of(100)), Money.ZERO);

    @Test
    @DisplayName("publish: әр транзакция үшін әр бақылаушы, жазылу ретімен")
    void order() {
        TransactionPublisher publisher = new TransactionPublisher();
        List<String> calls = new ArrayList<>();
        publisher.subscribe((acc, tx) -> calls.add("A" + tx.id()));
        publisher.subscribe((acc, tx) -> calls.add("B" + tx.id()));

        account.deposit(Money.of(1000), AT);
        publisher.publish(account, account.withdraw(Money.of(300), AT));   // #2 WITHDRAWAL, #3 FEE

        assertEquals(List.of("A2", "B2", "A3", "B3"), calls);
        assertEquals(2, publisher.listenerCount());
    }

    @Test
    @DisplayName("бақылаушы Account-ты да алады")
    void receivesAccount() {
        TransactionPublisher publisher = new TransactionPublisher();
        List<Account> seen = new ArrayList<>();
        publisher.subscribe((acc, tx) -> seen.add(acc));

        publisher.publish(account, List.of(account.deposit(Money.of(5), AT)));

        assertEquals(List.of(account), seen);
    }

    @Test
    @DisplayName("unsubscribe: бұдан былай хабар келмейді; жоқ бақылаушы -> false")
    void unsubscribe() {
        TransactionPublisher publisher = new TransactionPublisher();
        List<Long> calls = new ArrayList<>();
        TransactionListener listener = (acc, tx) -> calls.add(tx.id());
        publisher.subscribe(listener);

        publisher.publish(account, List.of(account.deposit(Money.of(5), AT)));
        assertTrue(publisher.unsubscribe(listener));
        publisher.publish(account, List.of(account.deposit(Money.of(5), AT)));

        assertEquals(List.of(1L), calls);
        assertEquals(0, publisher.listenerCount());
        assertFalse(publisher.unsubscribe(listener));
    }

    @Test
    @DisplayName("бақылаушысыз publish — қатесіз; null бақылаушы -> NullPointerException")
    void edgeCases() {
        TransactionPublisher publisher = new TransactionPublisher();
        assertDoesNotThrow(() -> publisher.publish(account, List.of(account.deposit(Money.of(5), AT))));
        assertThrows(NullPointerException.class, () -> publisher.subscribe(null));
    }

    @Test
    @DisplayName("AuditLog: әр транзакцияға бір жол")
    void auditLog() {
        AuditLog log = new AuditLog();
        TransactionPublisher publisher = new TransactionPublisher();
        publisher.subscribe(log);

        publisher.publish(account, List.of(account.deposit(Money.of(1000), AT)));
        publisher.publish(account, account.transferOut(Money.of(300), "KZ0002", AT));

        assertEquals(List.of(
                "KZ0001 #1 DEPOSIT +1000.00 ₸, balance 1000.00 ₸",
                "KZ0001 #2 TRANSFER_OUT -300.00 ₸ (to KZ0002), balance 700.00 ₸",
                "KZ0001 #3 FEE -100.00 ₸ (fee for #2), balance 600.00 ₸"), log.lines());
    }

    @Test
    @DisplayName("AuditLog.lines(): сырттан өзгертуге болмайды")
    void auditLogReadOnly() {
        AuditLog log = new AuditLog();
        log.onTransaction(account, account.deposit(Money.of(5), AT));

        assertThrows(UnsupportedOperationException.class, () -> log.lines().clear());
        assertEquals(1, log.lines().size());
    }

    @Test
    @DisplayName("LargeTransactionAlert: сома >= шек болса ғана хабарлайды")
    void alert() {
        List<String> sent = new ArrayList<>();
        TransactionListener alert = new LargeTransactionAlert(Money.of(500_000), sent::add);

        Transaction big = account.deposit(Money.of(500_000), AT);
        Transaction small = account.deposit(Money.of("499999.99"), AT);
        alert.onTransaction(account, big);
        alert.onTransaction(account, small);

        assertEquals(List.of("ALERT KZ0001: DEPOSIT 500000.00 ₸"), sent);
    }

    @Test
    @DisplayName("LargeTransactionAlert: шек > 0, null -> NullPointerException")
    void alertValidation() {
        assertEquals("threshold must be positive", assertThrows(IllegalArgumentException.class,
                () -> new LargeTransactionAlert(Money.ZERO, s -> { })).getMessage());
        assertThrows(NullPointerException.class, () -> new LargeTransactionAlert(null, s -> { }));
        assertThrows(NullPointerException.class, () -> new LargeTransactionAlert(Money.of(1), null));
    }
}
