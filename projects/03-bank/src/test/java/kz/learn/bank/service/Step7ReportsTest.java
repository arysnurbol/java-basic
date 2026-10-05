package kz.learn.bank.service;

import kz.learn.bank.exception.AccountNotFoundException;
import kz.learn.bank.fee.FeePolicy;
import kz.learn.bank.fee.FixedFee;
import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.model.Transaction;
import kz.learn.bank.model.TransactionType;
import kz.learn.bank.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Қадам 7 — есептер: көшірме, түрлер бойынша сомалар, рейтинг")
class Step7ReportsTest {

    private static final LocalDate OCT_1 = LocalDate.of(2026, 10, 1);

    private TestClock clock;
    private BankService bank;

    @BeforeEach
    void setUp() {
        clock = new TestClock(OCT_1.atTime(9, 0));
        bank = new BankService(new InMemoryRepository<>(), clock);
    }

    private void at(LocalDate day, int hour) {
        clock.set(LocalDateTime.of(day, java.time.LocalTime.of(hour, 0)));
    }

    @Test
    @DisplayName("statement: [from, to] күндері ішіндегі транзакциялар (шекаралар қоса)")
    void statement() {
        bank.openAccount("Aru", FeePolicy.none(), Money.ZERO);
        at(OCT_1, 23);              bank.deposit("KZ0001", Money.of(100));   // #1
        at(OCT_1.plusDays(1), 0);   bank.deposit("KZ0001", Money.of(200));   // #2
        at(OCT_1.plusDays(2), 12);  bank.withdraw("KZ0001", Money.of(50));   // #3
        at(OCT_1.plusDays(3), 8);   bank.deposit("KZ0001", Money.of(10));    // #4

        List<Long> ids = bank.statement("KZ0001", OCT_1.plusDays(1), OCT_1.plusDays(2))
                .stream().map(Transaction::id).toList();
        assertEquals(List.of(2L, 3L), ids);
        assertEquals(4, bank.statement("KZ0001", OCT_1, OCT_1.plusDays(3)).size());
        assertEquals(1, bank.statement("KZ0001", OCT_1, OCT_1).size(), "бір күн: from == to");
        assertEquals(List.of(), bank.statement("KZ0001", OCT_1.plusDays(10), OCT_1.plusDays(20)));
    }

    @Test
    @DisplayName("statement: from > to -> IllegalArgumentException; шот жоқ -> AccountNotFoundException")
    void statementValidation() {
        bank.openAccount("Aru", FeePolicy.none(), Money.ZERO);

        assertEquals("from must not be after to", assertThrows(IllegalArgumentException.class,
                () -> bank.statement("KZ0001", OCT_1.plusDays(1), OCT_1)).getMessage());
        assertThrows(AccountNotFoundException.class, () -> bank.statement("KZ0404", OCT_1, OCT_1));
    }

    @Test
    @DisplayName("totalsByType: EnumMap, тек кездескен түрлер, enum ретімен")
    void totalsByType() {
        bank.openAccount("Aru", new FixedFee(Money.of(100)), Money.ZERO);
        bank.openAccount("Daniyar", FeePolicy.none(), Money.ZERO);
        bank.deposit("KZ0001", Money.of(1000));
        bank.deposit("KZ0001", Money.of("500.50"));
        bank.withdraw("KZ0001", Money.of(200));
        bank.transfer("KZ0001", "KZ0002", Money.of(300));

        Map<TransactionType, Money> totals = bank.totalsByType("KZ0001");

        assertInstanceOf(EnumMap.class, totals);
        assertEquals(List.of(TransactionType.DEPOSIT, TransactionType.WITHDRAWAL,
                TransactionType.TRANSFER_OUT, TransactionType.FEE), List.copyOf(totals.keySet()));
        assertEquals(Money.of("1500.50"), totals.get(TransactionType.DEPOSIT));
        assertEquals(Money.of(200), totals.get(TransactionType.WITHDRAWAL));
        assertEquals(Money.of(300), totals.get(TransactionType.TRANSFER_OUT));
        assertEquals(Money.of(200), totals.get(TransactionType.FEE));
        assertEquals(Map.of(TransactionType.TRANSFER_IN, Money.of(300)), bank.totalsByType("KZ0002"));
    }

    @Test
    @DisplayName("totalBalance: барлық шоттың қосындысы (теріс балансы да)")
    void totalBalance() {
        assertEquals(Money.ZERO, bank.totalBalance(), "шот жоқ -> 0");

        bank.openAccount("Aru", FeePolicy.none(), Money.ZERO);
        bank.openAccount("Daniyar", FeePolicy.none(), Money.of(1000));
        bank.deposit("KZ0001", Money.of("1000.25"));
        bank.withdraw("KZ0002", Money.of(300));

        assertEquals(Money.of("700.25"), bank.totalBalance());
    }

    @Test
    @DisplayName("topByBalance: баланс кемуі бойынша, тең болса — нөмір бойынша; ең көбі limit")
    void topByBalance() {
        for (String owner : List.of("A", "B", "C", "D")) {
            bank.openAccount(owner, FeePolicy.none(), Money.of(1000));
        }
        bank.deposit("KZ0002", Money.of(500));
        bank.deposit("KZ0003", Money.of(900));
        bank.deposit("KZ0004", Money.of(500));
        bank.withdraw("KZ0001", Money.of(10));   // -10

        List<String> top3 = bank.topByBalance(3).stream().map(Account::getId).toList();
        assertEquals(List.of("KZ0003", "KZ0002", "KZ0004"), top3);
        assertEquals("KZ0001", bank.topByBalance(10).get(3).getId());
        assertEquals(4, bank.topByBalance(10).size());
    }
}
