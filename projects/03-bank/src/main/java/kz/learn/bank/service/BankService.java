package kz.learn.bank.service;

import kz.learn.bank.event.TransactionListener;
import kz.learn.bank.event.TransactionPublisher;
import kz.learn.bank.exception.AccountNotFoundException;
import kz.learn.bank.exception.BankException;
import kz.learn.bank.fee.FeePolicy;
import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.model.Transaction;
import kz.learn.bank.model.TransactionType;
import kz.learn.bank.repository.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Банктің бизнес-логикасы.
 *
 * Тәуелділіктер конструктор арқылы (DI): репозиторий және Clock.
 * Неге Clock: LocalDateTime.now() ішінде жасырын «жүйелік сағат» бар — тест оны басқара алмайды.
 * Сағатты сырттан берсек, тест уақытты тоқтатып та, жылжытып та қоя алады (TestClock-ты қара).
 * Уақытты әрқашан LocalDateTime.now(clock) арқылы ал.
 *
 * TransactionPublisher — ішкі өріс (композиция); subscribe() оған жай тапсырады (delegation).
 *
 * Шот нөмірлері: "KZ0001", "KZ0002", ... — String.format("KZ%04d", n).
 *
 * Ескерту: Account объектісі репозиторийдің ішінде — сол объект. Оны өзгерткен соң қайта save() қажет емес.
 * (JPA-да да осындай: транзакция ішінде жүктелген entity-дің өзгерісі өзі сақталады — dirty checking.)
 */
public class BankService {

    private final Repository<Account, String> accounts;
    private final Clock clock;
    private final TransactionPublisher publisher = new TransactionPublisher();
    private int counter;

    /** accounts, clock — null болмайды. */
    public BankService(Repository<Account, String> accounts, Clock clock) {
        this.accounts = Objects.requireNonNull(accounts, "accounts");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /** Бақылаушы тіркеу — publisher-ге тапсыр. */
    public void subscribe(TransactionListener listener) {
        publisher.subscribe(listener);
    }

    // ---------------------------------------------------------------- Қадам 6: операциялар

    /**
     * Жаңа шот ашады, сақтайды, қайтарады. Баланс 0.
     * Сәтсіз ашу (бос owner) нөмірді «жемеуі» керек: келесі сәтті шот бәрібір KZ0001 болсын.
     * Кеңес: есептегішті Account сәтті жасалғаннан КЕЙІН арттыр.
     */
    public Account openAccount(String owner, FeePolicy feePolicy, Money overdraftLimit) {
        Account account = new Account(String.format("KZ%04d", counter + 1), owner, feePolicy, overdraftLimit);
        counter++;
        return accounts.save(account);
    }

    /** Табылмаса — AccountNotFoundException(number). Кеңес: findById(...).orElseThrow(...) */
    public Account getAccount(String number) {
        return accounts.findById(number).orElseThrow(() -> new AccountNotFoundException(number));
    }

    /** Салу + оқиғаны жариялау. */
    public Transaction deposit(String number, Money amount) {
        Account account = getAccount(number);
        Transaction tx = account.deposit(amount, now());
        publisher.publish(account, List.of(tx));
        return tx;
    }

    /** Шешу + жариялау (комиссия болса — екі оқиға). */
    public List<Transaction> withdraw(String number, Money amount) {
        Account account = getAccount(number);
        List<Transaction> txs = account.withdraw(amount, now());
        publisher.publish(account, txs);
        return txs;
    }

    /**
     * Аударым. Рет:
     *  1) екі шотты да тап (жоқ болса AccountNotFoundException);
     *  2) бір шот болса — BankException("Cannot transfer to the same account");
     *  3) from.transferOut(...) — ақша жетпесе ОСЫ ЖЕРДЕ құлайды, ештеңе өзгермеген;
     *  4) to.transferIn(...) — бұл құламайды (сома оң екені 3-те тексерілді);
     *  5) ТЕК СОДАН КЕЙІН жариялау: алдымен from-ның транзакциялары, сосын to-ныкі.
     * Екі транзакцияның уақыты бірдей болсын — now()-ды бір рет шақыр.
     *
     * Назар аудар: 3-қадам құласа, 4-қадамға жетпейміз — «жартылай аударым» болмайды.
     * Бұл ретпен ғана қамтамасыз етілген атомарлық; ДҚ-да оны @Transactional береді.
     */
    public void transfer(String fromNumber, String toNumber, Money amount) {
        Account from = getAccount(fromNumber);
        Account to = getAccount(toNumber);
        if (from.equals(to)) {
            throw new BankException("Cannot transfer to the same account");
        }
        LocalDateTime at = now();
        List<Transaction> outs = from.transferOut(amount, to.getId(), at);
        Transaction in = to.transferIn(amount, from.getId(), at);
        publisher.publish(from, outs);
        publisher.publish(to, List.of(in));
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    // ---------------------------------------------------------------- Қадам 7: есептер

    /**
     * Шот көшірмесі: tx.at() КҮНІ [from, to] аралығында (екі шекара да қоса), тарих ретімен.
     * from > to -> IllegalArgumentException("from must not be after to").
     * Кеңес: tx.at().toLocalDate(), isBefore/isAfter.
     */
    public List<Transaction> statement(String number, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from must not be after to");
        }
        return getAccount(number).history().stream()
                .filter(tx -> {
                    LocalDate day = tx.at().toLocalDate();
                    return !day.isBefore(from) && !day.isAfter(to);
                })
                .toList();
    }

    /**
     * Шоттың әр транзакция түрі бойынша жиынтық сомасы (amount, таңбасыз).
     * Тек кездескен түрлер. EnumMap — кілттер enum жариялау ретімен, HashMap-тан жылдам әрі ықшам.
     * Кеңес: groupingBy(Transaction::type, () -> new EnumMap<>(TransactionType.class),
     *                   Collectors.reducing(Money.ZERO, Transaction::amount, Money::plus))
     * summingLong мұнда жарамайды — Money long емес. reducing — кез келген «қосу» үшін.
     */
    public Map<TransactionType, Money> totalsByType(String number) {
        return getAccount(number).history().stream()
                .collect(Collectors.groupingBy(Transaction::type,
                        () -> new EnumMap<>(TransactionType.class),
                        Collectors.reducing(Money.ZERO, Transaction::amount, Money::plus)));
    }

    /** Барлық шоттың баланстарының қосындысы (теріс балансы да). Шот жоқ — Money.ZERO. Кеңес: reduce. */
    public Money totalBalance() {
        return accounts.findAll().stream()
                .map(Account::getBalance)
                .reduce(Money.ZERO, Money::plus);
    }

    /**
     * Баланс бойынша кему ретімен, тең болса — нөмір бойынша өсу ретімен; ең көбі limit дана.
     * Money Comparable болғандықтан: Comparator.comparing(Account::getBalance) жұмыс істейді.
     */
    public List<Account> topByBalance(int limit) {
        return accounts.findAll().stream()
                .sorted(Comparator.comparing(Account::getBalance).reversed()
                        .thenComparing(Account::getId))
                .limit(limit)
                .toList();
    }
}
