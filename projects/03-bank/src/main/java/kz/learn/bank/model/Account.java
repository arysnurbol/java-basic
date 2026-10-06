package kz.learn.bank.model;

import kz.learn.bank.exception.InsufficientFundsException;
import kz.learn.bank.fee.FeePolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Банк шоты. Item/Transaction-нан айырмашылығы — ӨЗГЕРМЕЛІ (баланс өзгереді), бірақ ИНКАПСУЛЯЦИЯЛАНҒАН:
 * балансты тікелей орнататын сеттер ЖОҚ, ол тек операциялар арқылы өзгереді,
 * және әр өзгеріс тарихта Transaction болып қалады. Баланс = тарихтың қорытындысы.
 *
 * Өрістер:
 *   number, owner, feePolicy, overdraftLimit — final;
 *   history — final List<Transaction> (сілтеме final, ішіндегісі өседі);
 *   balance — Money.ZERO-дан басталады, final ЕМЕС.
 *
 * feePolicy — STRATEGY: Account комиссияның қалай есептелетінін білмейді, тек feeFor(...) шақырады.
 * overdraftLimit — баланс қаншаға дейін теріс бола алады (0 = овердрафт жоқ).
 */
public class Account implements Identifiable<String> {

    private final String number;
    private final String owner;
    private final FeePolicy feePolicy;
    private final Money overdraftLimit;
    private final List<Transaction> history;
    private Money balance = Money.ZERO;

    /**
     * number, owner — requireText(...) арқылы (бос болмайды, strip).
     * feePolicy, overdraftLimit — null болмайды: Objects.requireNonNull(x, "feePolicy").
     * overdraftLimit < 0 -> IllegalArgumentException("overdraftLimit must not be negative").
     */
    public Account(String number, String owner, FeePolicy feePolicy, Money overdraftLimit) {
        this.number = requireText(number, "number");
        this.owner = requireText(owner, "owner");
        this.feePolicy = Objects.requireNonNull(feePolicy, "feePolicy");
        this.overdraftLimit = Objects.requireNonNull(overdraftLimit, "overdraftLimit");
        if (overdraftLimit.isNegative()) {
            throw new IllegalArgumentException("overdraftLimit must not be negative");
        }
        this.history = new ArrayList<>();
    }

    /** Шот нөмірі. */
    @Override
    public String getId() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public Money getBalance() {
        return balance;
    }

    public Money getOverdraftLimit() {
        return overdraftLimit;
    }

    /** Жұмсауға болатын ең көп сома: balance + overdraftLimit. */
    public Money available() {
        return balance.plus(overdraftLimit);
    }

    /**
     * Тарих, жасалу ретімен. Сырттан ӨЗГЕРТУГЕ БОЛМАЙДЫ (add/clear -> UnsupportedOperationException):
     * әйтпесе кез келген адам history().add(...) арқылы баланс пен тарихты сәйкессіз етеді.
     * Кеңес: Collections.unmodifiableList(...) — көшірмесіз «тек оқуға» терезе.
     */
    public List<Transaction> history() {
        return Collections.unmodifiableList(history);
    }

    /** Салу. requirePositive, содан record(DEPOSIT, amount, "", at). Комиссия жоқ. */
    public Transaction deposit(Money amount, LocalDateTime at) {
        requirePositive(amount);
        return record(TransactionType.DEPOSIT, amount, "", at);
    }

    /** Шешу: debit(WITHDRAWAL, amount, "", at). */
    public List<Transaction> withdraw(Money amount, LocalDateTime at) {
        return debit(TransactionType.WITHDRAWAL, amount, "", at);
    }

    /** Аударым жіберу: debit(TRANSFER_OUT, amount, "to KZ0002", at). */
    public List<Transaction> transferOut(Money amount, String toNumber, LocalDateTime at) {
        return debit(TransactionType.TRANSFER_OUT, amount, "to " + toNumber, at);
    }

    /** Аударым қабылдау: requirePositive, record(TRANSFER_IN, amount, "from KZ0001", at). Комиссия жоқ. */
    public Transaction transferIn(Money amount, String fromNumber, LocalDateTime at) {
        requirePositive(amount);
        return record(TransactionType.TRANSFER_IN, amount, "from " + fromNumber, at);
    }

    /**
     * Ақша шығаратын операциялардың ортақ логикасы:
     *  1) requirePositive(amount);
     *  2) fee = feePolicy.feeFor(amount); total = amount + fee;
     *  3) total > available() болса -> InsufficientFundsException(number, total, available());
     *  4) ТЕК СОДАН КЕЙІН өзгерт: record(type, ...) — негізгі транзакция;
     *     fee > 0 болса — тағы record(FEE, fee, "fee for #<негізгісінің id>", at).
     * Қайтарады: List.of(негізгі) немесе List.of(негізгі, комиссия).
     *
     * Назар аудар: алдымен БАРЛЫҚ тексеріс, сосын өзгеріс («validate, then mutate»).
     * Тексеріс ортасында құласа, шот жартылай өзгеріп қалмайды.
     */
    private List<Transaction> debit(TransactionType type, Money amount, String description, LocalDateTime at) {
        requirePositive(amount);
        Money fee = feePolicy.feeFor(amount);
        Money total = amount.plus(fee);
        if (total.isGreaterThan(available())) {
            throw new InsufficientFundsException(number, total, available());
        }

        Transaction main = record(type, amount, description, at);
        if (!fee.isPositive()) {
            return List.of(main);
        }
        Transaction feeTx = record(TransactionType.FEE, fee, "fee for #" + main.id(), at);
        return List.of(main, feeTx);
    }

    /**
     * Бір транзакцияны жасап, тарихқа қосып, балансты жаңартады.
     * id = history.size() + 1 (әр шотта 1, 2, 3, ...).
     * Жаңа баланс: type.isCredit() ? balance + amount : balance - amount.
     * Кеңес: Transaction-ды балансты өзгертпес БҰРЫН жаса — оның конструкторы тексеріс жасайды.
     */
    private Transaction record(TransactionType type, Money amount, String description, LocalDateTime at) {
        long id = history.size() + 1;
        Money newBalance = type.isCredit() ? balance.plus(amount) : balance.minus(amount);
        Transaction tx = new Transaction(id, type, amount, newBalance, at, description);
        history.add(tx);
        balance = newBalance;
        return tx;
    }

    /** ДАЙЫН. */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }

    /** ДАЙЫН. amount null -> NullPointerException; <= 0 -> IllegalArgumentException. */
    private static void requirePositive(Money amount) {
        Objects.requireNonNull(amount, "amount");
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    /** Тек number бойынша. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return number.equals(account.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    /** ДАЙЫН. Пішім: "KZ0001 (Aru): 1500.00 ₸" */
    @Override
    public String toString() {
        return getId() + " (" + getOwner() + "): " + getBalance();
    }
}
