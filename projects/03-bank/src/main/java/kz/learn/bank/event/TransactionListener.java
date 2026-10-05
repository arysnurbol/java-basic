package kz.learn.bank.event;

import kz.learn.bank.model.Account;
import kz.learn.bank.model.Transaction;

/**
 * OBSERVER — бақылаушы. ДАЙЫН.
 *
 * Бір ғана абстрактілі метод — @FunctionalInterface, сондықтан оның орнына лямбда беруге болады:
 *   service.subscribe((account, tx) -> System.out.println(tx));
 */
@FunctionalInterface
public interface TransactionListener {

    void onTransaction(Account account, Transaction tx);
}
