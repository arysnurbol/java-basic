package kz.learn.bank.model;

import kz.learn.bank.exception.AccountNotFoundException;
import kz.learn.bank.exception.BankException;
import kz.learn.bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 2 — exception-дар, TransactionType, Transaction (record)")
class Step2TransactionTest {

    private static final LocalDateTime AT = LocalDateTime.of(2026, 10, 5, 10, 0);

    @Test
    @DisplayName("BankException — unchecked, хабарламаны сақтайды")
    void bankException() {
        BankException e = new BankException("boom");
        assertInstanceOf(RuntimeException.class, e);
        assertEquals("boom", e.getMessage());
    }

    @Test
    @DisplayName("AccountNotFoundException: хабарлама және нөмір")
    void notFound() {
        AccountNotFoundException e = new AccountNotFoundException("KZ0007");
        assertInstanceOf(BankException.class, e);
        assertEquals("Account not found: KZ0007", e.getMessage());
        assertEquals("KZ0007", e.getNumber());
    }

    @Test
    @DisplayName("InsufficientFundsException: хабарлама Money.toString арқылы")
    void insufficient() {
        InsufficientFundsException e =
                new InsufficientFundsException("KZ0001", Money.of(1100), Money.of("500.5"));
        assertInstanceOf(BankException.class, e);
        assertEquals("Insufficient funds on KZ0001: requested 1100.00 ₸, available 500.50 ₸", e.getMessage());
        assertEquals("KZ0001", e.getNumber());
        assertEquals(Money.of(1100), e.getRequested());
        assertEquals(Money.of("500.5"), e.getAvailable());
    }

    @Test
    @DisplayName("TransactionType: ақша кіре ме (credit) әлде шыға ма")
    void types() {
        assertTrue(TransactionType.DEPOSIT.isCredit());
        assertTrue(TransactionType.TRANSFER_IN.isCredit());
        assertFalse(TransactionType.WITHDRAWAL.isCredit());
        assertFalse(TransactionType.TRANSFER_OUT.isCredit());
        assertFalse(TransactionType.FEE.isCredit());
    }

    @Test
    @DisplayName("Transaction — record: accessor-лар, equals/hashCode тегін")
    void record() {
        Transaction tx = new Transaction(1, TransactionType.DEPOSIT, Money.of(500), Money.of(500), AT, "salary");

        assertTrue(Transaction.class.isRecord(), "Transaction record болуы керек");
        assertEquals(1, tx.id());
        assertEquals(TransactionType.DEPOSIT, tx.type());
        assertEquals(Money.of(500), tx.amount());
        assertEquals(Money.of(500), tx.balanceAfter());
        assertEquals(AT, tx.at());
        assertEquals("salary", tx.description());
        assertEquals(tx, new Transaction(1, TransactionType.DEPOSIT, Money.of(500), Money.of(500), AT, "salary"));
    }

    @Test
    @DisplayName("compact конструктор: description — strip, null -> \"\"")
    void description() {
        assertEquals("to KZ0002",
                new Transaction(1, TransactionType.TRANSFER_OUT, Money.of(1), Money.ZERO, AT, "  to KZ0002 ").description());
        assertEquals("", new Transaction(1, TransactionType.DEPOSIT, Money.of(1), Money.of(1), AT, null).description());
    }

    @Test
    @DisplayName("compact конструктор: amount > 0, null өрістер -> NullPointerException")
    void validation() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(1, TransactionType.DEPOSIT, Money.ZERO, Money.ZERO, AT, ""));
        assertEquals("amount must be positive", e.getMessage());
        assertThrows(IllegalArgumentException.class,
                () -> new Transaction(1, TransactionType.DEPOSIT, Money.of(-5), Money.ZERO, AT, ""));

        assertThrows(NullPointerException.class, () -> new Transaction(1, null, Money.of(1), Money.of(1), AT, ""));
        assertThrows(NullPointerException.class, () -> new Transaction(1, TransactionType.DEPOSIT, null, Money.of(1), AT, ""));
        assertThrows(NullPointerException.class, () -> new Transaction(1, TransactionType.DEPOSIT, Money.of(1), null, AT, ""));
        assertThrows(NullPointerException.class, () -> new Transaction(1, TransactionType.DEPOSIT, Money.of(1), Money.of(1), null, ""));
    }

    @Test
    @DisplayName("signedAmount: кіріс +, шығыс −")
    void signedAmount() {
        assertEquals(Money.of(300),
                new Transaction(1, TransactionType.DEPOSIT, Money.of(300), Money.of(300), AT, "").signedAmount());
        assertEquals(Money.of(-300),
                new Transaction(2, TransactionType.WITHDRAWAL, Money.of(300), Money.ZERO, AT, "").signedAmount());
        assertEquals(Money.of(-100),
                new Transaction(3, TransactionType.FEE, Money.of(100), Money.of(-100), AT, "").signedAmount());
    }
}
