package kz.learn.bank.event;

import kz.learn.bank.model.Account;
import kz.learn.bank.model.Money;
import kz.learn.bank.model.Transaction;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Бақылаушы №2: ірі операция туралы ескерту (антифрод-тың қарапайым түрі).
 *
 * Хабарды ҚАЙДА жіберетінін білмейді — оны notifier (Consumer<String>) шешеді:
 * консольде System.out::println, тестте list::add, шын жүйеде SMS/email сервисі.
 * Бұл да композиция: мінез-құлық сырттан беріледі.
 *
 * Өрістер: threshold, notifier — final.
 */
public class LargeTransactionAlert implements TransactionListener {

    private final Money threshold;
    private final Consumer<String> notifier;

    /** threshold, notifier — null болмайды; threshold <= 0 -> IllegalArgumentException("threshold must be positive"). */
    public LargeTransactionAlert(Money threshold, Consumer<String> notifier) {
        this.threshold = Objects.requireNonNull(threshold, "threshold");
        this.notifier = Objects.requireNonNull(notifier, "notifier");
        if (!threshold.isPositive()) {
            throw new IllegalArgumentException("threshold must be positive");
        }
    }

    /**
     * tx.amount() >= threshold болса (кез келген түр) — notifier-ге хабар:
     *   "ALERT KZ0001: WITHDRAWAL 600000.00 ₸"
     * Назар аудар: шекараның өзі де кіреді (>=). Money-де isGreaterThan бар — оны қалай қолданасың?
     */
    @Override
    public void onTransaction(Account account, Transaction tx) {
        if (!threshold.isGreaterThan(tx.amount())) {
            notifier.accept("ALERT " + account.getId() + ": " + tx.type() + " " + tx.amount());
        }
    }
}
