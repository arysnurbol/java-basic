package kz.learn.bank.model;

import kz.learn.bank.exception.BankException;
import kz.learn.bank.exception.InsufficientFundsException;
import kz.learn.bank.fee.FeePolicy;
import kz.learn.bank.fee.FixedFee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 4 — Account: баланс, тарих, стратегия ішінде")
class Step4AccountTest {

    private static final LocalDateTime AT = LocalDateTime.of(2026, 10, 5, 10, 0);

    private static Account free() {
        return new Account("KZ0001", "Aru", FeePolicy.none(), Money.ZERO);
    }

    private static Account withFee(long fee) {
        return new Account("KZ0001", "Aru", new FixedFee(Money.of(fee)), Money.ZERO);
    }

    @Test
    @DisplayName("конструктор: баланс 0, тарих бос, мәтін strip")
    void constructor() {
        Account a = new Account(" KZ0001 ", "  Aru ", FeePolicy.none(), Money.of(1000));

        assertEquals("KZ0001", a.getId());
        assertEquals("Aru", a.getOwner());
        assertEquals(Money.ZERO, a.getBalance());
        assertEquals(Money.of(1000), a.getOverdraftLimit());
        assertEquals(Money.of(1000), a.available(), "available = balance + overdraftLimit");
        assertTrue(a.history().isEmpty());
        assertEquals("KZ0001 (Aru): 0.00 ₸", a.toString());
    }

    @Test
    @DisplayName("конструктор: бос мәтін, теріс овердрафт -> IllegalArgumentException; null -> NullPointerException")
    void constructorValidation() {
        assertEquals("owner must not be blank", assertThrows(IllegalArgumentException.class,
                () -> new Account("KZ0001", " ", FeePolicy.none(), Money.ZERO)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> new Account(null, "Aru", FeePolicy.none(), Money.ZERO));
        assertEquals("overdraftLimit must not be negative", assertThrows(IllegalArgumentException.class,
                () -> new Account("KZ0001", "Aru", FeePolicy.none(), Money.of(-1))).getMessage());
        assertThrows(NullPointerException.class, () -> new Account("KZ0001", "Aru", null, Money.ZERO));
        assertThrows(NullPointerException.class, () -> new Account("KZ0001", "Aru", FeePolicy.none(), null));
    }

    @Test
    @DisplayName("deposit: баланс өседі, Transaction тарихқа жазылады")
    void deposit() {
        Account a = free();
        Transaction tx = a.deposit(Money.of(1000), AT);

        assertEquals(new Transaction(1, TransactionType.DEPOSIT, Money.of(1000), Money.of(1000), AT, ""), tx);
        assertEquals(Money.of(1000), a.getBalance());
        assertEquals(List.of(tx), a.history());
    }

    @Test
    @DisplayName("deposit/withdraw: сома <= 0 -> IllegalArgumentException, ештеңе өзгермейді")
    void nonPositive() {
        Account a = free();
        a.deposit(Money.of(100), AT);

        assertEquals("amount must be positive",
                assertThrows(IllegalArgumentException.class, () -> a.deposit(Money.ZERO, AT)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> a.withdraw(Money.of(-5), AT));
        assertThrows(IllegalArgumentException.class, () -> a.transferIn(Money.ZERO, "KZ0002", AT));
        assertEquals(Money.of(100), a.getBalance());
        assertEquals(1, a.history().size());
    }

    @Test
    @DisplayName("withdraw комиссиясыз: бір Transaction")
    void withdrawFree() {
        Account a = free();
        a.deposit(Money.of(1000), AT);
        List<Transaction> txs = a.withdraw(Money.of(300), AT);

        assertEquals(List.of(new Transaction(2, TransactionType.WITHDRAWAL, Money.of(300), Money.of(700), AT, "")), txs);
        assertEquals(Money.of(700), a.getBalance());
    }

    @Test
    @DisplayName("withdraw комиссиямен: WITHDRAWAL, содан кейін бөлек FEE транзакциясы")
    void withdrawWithFee() {
        Account a = withFee(100);
        a.deposit(Money.of(1000), AT);
        List<Transaction> txs = a.withdraw(Money.of(300), AT);

        assertEquals(List.of(
                new Transaction(2, TransactionType.WITHDRAWAL, Money.of(300), Money.of(700), AT, ""),
                new Transaction(3, TransactionType.FEE, Money.of(100), Money.of(600), AT, "fee for #2")), txs);
        assertEquals(Money.of(600), a.getBalance());
        assertEquals(3, a.history().size());
    }

    @Test
    @DisplayName("ақша жетпесе: InsufficientFundsException (сома + комиссия), ештеңе өзгермейді")
    void insufficient() {
        Account a = withFee(100);
        a.deposit(Money.of(500), AT);

        InsufficientFundsException e = assertThrows(InsufficientFundsException.class,
                () -> a.withdraw(Money.of(401), AT));
        assertEquals(Money.of(501), e.getRequested(), "requested = сома + комиссия");
        assertEquals(Money.of(500), e.getAvailable());
        assertEquals(Money.of(500), a.getBalance());
        assertEquals(1, a.history().size(), "сәтсіз операция тарихта із қалдырмайды");

        a.withdraw(Money.of(400), AT);
        assertEquals(Money.ZERO, a.getBalance(), "дәл шекара — рұқсат");
    }

    @Test
    @DisplayName("овердрафт: баланс -limit-ке дейін түсе алады")
    void overdraft() {
        Account a = new Account("KZ0001", "Aru", FeePolicy.none(), Money.of(1000));

        a.withdraw(Money.of(1000), AT);
        assertEquals(Money.of(-1000), a.getBalance());
        assertEquals(Money.ZERO, a.available());
        assertThrows(InsufficientFundsException.class, () -> a.withdraw(Money.of("0.01"), AT));
    }

    @Test
    @DisplayName("transferOut / transferIn: тип және сипаттама; комиссия тек жіберушіде")
    void transfers() {
        Account from = withFee(100);
        from.deposit(Money.of(1000), AT);
        Account to = new Account("KZ0002", "Daniyar", new FixedFee(Money.of(999)), Money.ZERO);

        List<Transaction> out = from.transferOut(Money.of(400), "KZ0002", AT);
        Transaction in = to.transferIn(Money.of(400), "KZ0001", AT);

        assertEquals(TransactionType.TRANSFER_OUT, out.get(0).type());
        assertEquals("to KZ0002", out.get(0).description());
        assertEquals(TransactionType.FEE, out.get(1).type());
        assertEquals(Money.of(500), from.getBalance());

        assertEquals(new Transaction(1, TransactionType.TRANSFER_IN, Money.of(400), Money.of(400), AT, "from KZ0001"), in);
        assertEquals(Money.of(400), to.getBalance(), "алушыдан комиссия алынбайды");
    }

    @Test
    @DisplayName("history(): сырттан өзгертуге болмайды")
    void historyIsReadOnly() {
        Account a = free();
        a.deposit(Money.of(100), AT);
        Transaction fake = new Transaction(99, TransactionType.DEPOSIT, Money.of(1_000_000), Money.of(1_000_000), AT, "");

        assertThrows(UnsupportedOperationException.class, () -> a.history().add(fake));
        assertThrows(UnsupportedOperationException.class, () -> a.history().clear());
        assertEquals(1, a.history().size());
    }

    @Test
    @DisplayName("Strategy: Account кез келген FeePolicy-мен жұмыс істейді, тіпті лямбдамен")
    void anyStrategy() {
        FeePolicy tenPercent = amount -> amount.percent(java.math.BigDecimal.TEN);
        Account a = new Account("KZ0001", "Aru", tenPercent, Money.ZERO);
        a.deposit(Money.of(1000), AT);
        a.withdraw(Money.of(500), AT);

        assertEquals(Money.of(450), a.getBalance());
    }

    @Test
    @DisplayName("equals/hashCode — тек нөмір бойынша")
    void equality() {
        Account a = new Account("KZ0001", "Aru", FeePolicy.none(), Money.ZERO);
        Account same = new Account("KZ0001", "Other", new FixedFee(Money.of(5)), Money.of(10));

        assertEquals(a, same);
        assertEquals(a.hashCode(), same.hashCode());
        assertNotEquals(a, new Account("KZ0002", "Aru", FeePolicy.none(), Money.ZERO));
        assertTrue(BankException.class.isAssignableFrom(InsufficientFundsException.class));
    }
}
