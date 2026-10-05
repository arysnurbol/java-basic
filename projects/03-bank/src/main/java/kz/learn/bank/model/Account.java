package kz.learn.bank.model;

import kz.learn.bank.exception.InsufficientFundsException;
import kz.learn.bank.fee.FeePolicy;

import java.time.LocalDateTime;
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

    // TODO: өрістерді жаз

    /**
     * number, owner — requireText(...) арқылы (бос болмайды, strip).
     * feePolicy, overdraftLimit — null болмайды: Objects.requireNonNull(x, "feePolicy").
     * overdraftLimit < 0 -> IllegalArgumentException("overdraftLimit must not be negative").
     */
    public Account(String number, String owner, FeePolicy feePolicy, Money overdraftLimit) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Шот нөмірі. */
    @Override
    public String getId() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public String getOwner() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getBalance() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money getOverdraftLimit() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жұмсауға болатын ең көп сома: balance + overdraftLimit. */
    public Money available() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Тарих, жасалу ретімен. Сырттан ӨЗГЕРТУГЕ БОЛМАЙДЫ (add/clear -> UnsupportedOperationException):
     * әйтпесе кез келген адам history().add(...) арқылы баланс пен тарихты сәйкессіз етеді.
     * Кеңес: Collections.unmodifiableList(...) — көшірмесіз «тек оқуға» терезе.
     */
    public List<Transaction> history() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Салу. requirePositive, содан record(DEPOSIT, amount, "", at). Комиссия жоқ. */
    public Transaction deposit(Money amount, LocalDateTime at) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Шешу: debit(WITHDRAWAL, amount, "", at). */
    public List<Transaction> withdraw(Money amount, LocalDateTime at) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Аударым жіберу: debit(TRANSFER_OUT, amount, "to KZ0002", at). */
    public List<Transaction> transferOut(Money amount, String toNumber, LocalDateTime at) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Аударым қабылдау: requirePositive, record(TRANSFER_IN, amount, "from KZ0001", at). Комиссия жоқ. */
    public Transaction transferIn(Money amount, String fromNumber, LocalDateTime at) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Бір транзакцияны жасап, тарихқа қосып, балансты жаңартады.
     * id = history.size() + 1 (әр шотта 1, 2, 3, ...).
     * Жаңа баланс: type.isCredit() ? balance + amount : balance - amount.
     * Кеңес: Transaction-ды балансты өзгертпес БҰРЫН жаса — оның конструкторы тексеріс жасайды.
     */
    private Transaction record(TransactionType type, Money amount, String description, LocalDateTime at) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int hashCode() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** ДАЙЫН. Пішім: "KZ0001 (Aru): 1500.00 ₸" */
    @Override
    public String toString() {
        return getId() + " (" + getOwner() + "): " + getBalance();
    }
}
