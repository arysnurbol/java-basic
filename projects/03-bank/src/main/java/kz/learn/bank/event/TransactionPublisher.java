package kz.learn.bank.event;

import kz.learn.bank.model.Account;
import kz.learn.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * OBSERVER — «жариялаушы» (subject). Бақылаушылар тізімін ұстайды және оқиғаны бәріне таратады.
 *
 * Мақсат — байланысты әлсірету: BankService аудит, SMS, антифрод туралы ЕШТЕҢЕ білмейді.
 * Ол тек «транзакция болды» деп жариялайды. Жаңа реакция қосу = жаңа TransactionListener,
 * BankService-ке бір жол да тимейді. Spring-тегі ApplicationEventPublisher / @EventListener,
 * Kafka-дағы producer/consumer — осы идеяның үлкен нұсқалары.
 *
 * Өріс: listeners — List<TransactionListener> (жазылу ретін сақтайды).
 */
public class TransactionPublisher {

    private final List<TransactionListener> listeners = new ArrayList<>();

    /** null -> NullPointerException (кейін publish кезінде емес, дәл қазір құласын). */
    public void subscribe(TransactionListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    /** Жазылымнан шығару. Бар болса — true. */
    public boolean unsubscribe(TransactionListener listener) {
        return listeners.remove(listener);
    }

    public int listenerCount() {
        return listeners.size();
    }

    /**
     * Әр транзакция үшін (ретімен) — әр бақылаушыға (жазылу ретімен) onTransaction(account, tx).
     * Яғни tx1: A, B; tx2: A, B.
     */
    public void publish(Account account, List<Transaction> transactions) {
        for (Transaction tx : transactions) {
            for (TransactionListener listener : listeners) {
                listener.onTransaction(account, tx);
            }
        }
    }
}
