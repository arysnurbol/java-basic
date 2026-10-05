package kz.learn.bank.service;

import kz.learn.bank.event.AuditLog;
import kz.learn.bank.exception.AccountNotFoundException;
import kz.learn.bank.exception.BankException;
import kz.learn.bank.exception.InsufficientFundsException;
import kz.learn.bank.fee.FeePolicy;
import kz.learn.bank.fee.FixedFee;
import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.model.Transaction;
import kz.learn.bank.model.TransactionType;
import kz.learn.bank.repository.InMemoryRepository;
import kz.learn.bank.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 6 — BankService: шот ашу, салу, шешу, аудару, оқиғалар")
class Step6ServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 10, 5, 10, 0);

    private Repository<Account, String> repo;
    private BankService bank;
    private final List<String> events = new ArrayList<>();

    @BeforeEach
    void setUp() {
        repo = new InMemoryRepository<>();
        bank = new BankService(repo, new TestClock(NOW));
        bank.subscribe((account, tx) -> events.add(account.getId() + ":" + tx.type()));
    }

    private Account open(String owner) {
        return bank.openAccount(owner, FeePolicy.none(), Money.ZERO);
    }

    @Test
    @DisplayName("openAccount: нөмірлер KZ0001, KZ0002, ... және репозиторийге сақталады")
    void open() {
        Account a = open("Aru");
        Account b = bank.openAccount("Daniyar", new FixedFee(Money.of(100)), Money.of(5000));

        assertEquals("KZ0001", a.getId());
        assertEquals("KZ0002", b.getId());
        assertEquals(Money.of(5000), b.getOverdraftLimit());
        assertEquals(List.of(a, b), repo.findAll());
    }

    @Test
    @DisplayName("openAccount: сәтсіз ашу нөмірді «жемейді»")
    void openFailureKeepsNumber() {
        assertThrows(IllegalArgumentException.class, () -> open(" "));
        assertEquals("KZ0001", open("Aru").getId());
    }

    @Test
    @DisplayName("getAccount: жоқ болса AccountNotFoundException")
    void notFound() {
        Account a = open("Aru");

        assertSame(a, bank.getAccount("KZ0001"));
        AccountNotFoundException e = assertThrows(AccountNotFoundException.class, () -> bank.getAccount("KZ0404"));
        assertEquals("KZ0404", e.getNumber());
        assertThrows(AccountNotFoundException.class, () -> bank.deposit("KZ0404", Money.of(1)));
    }

    @Test
    @DisplayName("deposit: уақыт Clock-тан алынады, оқиға жарияланады")
    void deposit() {
        open("Aru");
        Transaction tx = bank.deposit("KZ0001", Money.of(1000));

        assertEquals(NOW, tx.at(), "уақыт LocalDateTime.now(clock) арқылы");
        assertEquals(Money.of(1000), bank.getAccount("KZ0001").getBalance());
        assertEquals(List.of("KZ0001:DEPOSIT"), events);
    }

    @Test
    @DisplayName("withdraw: комиссиямен екі транзакция, екі оқиға")
    void withdraw() {
        bank.openAccount("Aru", new FixedFee(Money.of(100)), Money.ZERO);
        bank.deposit("KZ0001", Money.of(1000));
        List<Transaction> txs = bank.withdraw("KZ0001", Money.of(300));

        assertEquals(2, txs.size());
        assertEquals(Money.of(600), bank.getAccount("KZ0001").getBalance());
        assertEquals(List.of("KZ0001:DEPOSIT", "KZ0001:WITHDRAWAL", "KZ0001:FEE"), events);
    }

    @Test
    @DisplayName("transfer: екі шот та өзгереді, комиссия тек жіберушіде, 3 оқиға")
    void transfer() {
        bank.openAccount("Aru", new FixedFee(Money.of(100)), Money.ZERO);
        open("Daniyar");
        bank.deposit("KZ0001", Money.of(1000));
        events.clear();

        bank.transfer("KZ0001", "KZ0002", Money.of(400));

        Account from = bank.getAccount("KZ0001");
        Account to = bank.getAccount("KZ0002");
        assertEquals(Money.of(500), from.getBalance());
        assertEquals(Money.of(400), to.getBalance());
        assertEquals("to KZ0002", from.history().get(1).description());
        assertEquals(TransactionType.TRANSFER_IN, to.history().get(0).type());
        assertEquals("from KZ0001", to.history().get(0).description());
        assertEquals(List.of("KZ0001:TRANSFER_OUT", "KZ0001:FEE", "KZ0002:TRANSFER_IN"), events);
    }

    @Test
    @DisplayName("transfer: өз шотына -> BankException")
    void transferToSelf() {
        open("Aru");
        bank.deposit("KZ0001", Money.of(1000));

        BankException e = assertThrows(BankException.class, () -> bank.transfer("KZ0001", "KZ0001", Money.of(1)));
        assertEquals("Cannot transfer to the same account", e.getMessage());
        assertEquals(Money.of(1000), bank.getAccount("KZ0001").getBalance());
    }

    @Test
    @DisplayName("transfer сәтсіз болса — ЕКІ шот та өзгермейді, оқиға жоқ")
    void transferAtomic() {
        open("Aru");
        open("Daniyar");
        bank.deposit("KZ0001", Money.of(100));
        events.clear();

        assertThrows(InsufficientFundsException.class, () -> bank.transfer("KZ0001", "KZ0002", Money.of(101)));
        assertThrows(AccountNotFoundException.class, () -> bank.transfer("KZ0001", "KZ0404", Money.of(50)));
        assertThrows(IllegalArgumentException.class, () -> bank.transfer("KZ0001", "KZ0002", Money.ZERO));

        assertEquals(Money.of(100), bank.getAccount("KZ0001").getBalance());
        assertEquals(1, bank.getAccount("KZ0001").history().size());
        assertTrue(bank.getAccount("KZ0002").history().isEmpty());
        assertTrue(events.isEmpty(), "сәтсіз операция туралы хабар жоқ");
    }

    @Test
    @DisplayName("Observer: сервис AuditLog туралы ештеңе білмейді, бірақ ол бәрін жазады")
    void auditLogIntegration() {
        AuditLog log = new AuditLog();
        bank.subscribe(log);
        open("Aru");
        open("Daniyar");

        bank.deposit("KZ0001", Money.of(1000));
        bank.transfer("KZ0001", "KZ0002", Money.of(250));

        assertEquals(List.of(
                "KZ0001 #1 DEPOSIT +1000.00 ₸, balance 1000.00 ₸",
                "KZ0001 #2 TRANSFER_OUT -250.00 ₸ (to KZ0002), balance 750.00 ₸",
                "KZ0002 #1 TRANSFER_IN +250.00 ₸ (from KZ0001), balance 250.00 ₸"), log.lines());
    }
}
