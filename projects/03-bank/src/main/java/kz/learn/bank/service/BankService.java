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

    // TODO: өрістерді жаз

    /** accounts, clock — null болмайды. */
    public BankService(Repository<Account, String> accounts, Clock clock) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Бақылаушы тіркеу — publisher-ге тапсыр. */
    public void subscribe(TransactionListener listener) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 6: операциялар

    /**
     * Жаңа шот ашады, сақтайды, қайтарады. Баланс 0.
     * Сәтсіз ашу (бос owner) нөмірді «жемеуі» керек: келесі сәтті шот бәрібір KZ0001 болсын.
     * Кеңес: есептегішті Account сәтті жасалғаннан КЕЙІН арттыр.
     */
    public Account openAccount(String owner, FeePolicy feePolicy, Money overdraftLimit) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — AccountNotFoundException(number). Кеңес: findById(...).orElseThrow(...) */
    public Account getAccount(String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Салу + оқиғаны жариялау. */
    public Transaction deposit(String number, Money amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Шешу + жариялау (комиссия болса — екі оқиға). */
    public List<Transaction> withdraw(String number, Money amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private LocalDateTime now() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 7: есептер

    /**
     * Шот көшірмесі: tx.at() КҮНІ [from, to] аралығында (екі шекара да қоса), тарих ретімен.
     * from > to -> IllegalArgumentException("from must not be after to").
     * Кеңес: tx.at().toLocalDate(), isBefore/isAfter.
     */
    public List<Transaction> statement(String number, LocalDate from, LocalDate to) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Шоттың әр транзакция түрі бойынша жиынтық сомасы (amount, таңбасыз).
     * Тек кездескен түрлер. EnumMap — кілттер enum жариялау ретімен, HashMap-тан жылдам әрі ықшам.
     * Кеңес: groupingBy(Transaction::type, () -> new EnumMap<>(TransactionType.class),
     *                   Collectors.reducing(Money.ZERO, Transaction::amount, Money::plus))
     * summingLong мұнда жарамайды — Money long емес. reducing — кез келген «қосу» үшін.
     */
    public Map<TransactionType, Money> totalsByType(String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Барлық шоттың баланстарының қосындысы (теріс балансы да). Шот жоқ — Money.ZERO. Кеңес: reduce. */
    public Money totalBalance() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Баланс бойынша кему ретімен, тең болса — нөмір бойынша өсу ретімен; ең көбі limit дана.
     * Money Comparable болғандықтан: Comparator.comparing(Account::getBalance) жұмыс істейді.
     */
    public List<Account> topByBalance(int limit) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
