package kz.learn.bank.event;

import kz.learn.bank.model.Account;
import kz.learn.bank.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Бақылаушы №1: әр транзакцияны журналға бір жол етіп жазады.
 * Өріс: lines — List<String>.
 */
public class AuditLog implements TransactionListener {

    private final List<String> lines = new ArrayList<>();

    /** format(account, tx) жолын журналға қос. */
    @Override
    public void onTransaction(Account account, Transaction tx) {
        lines.add(format(account, tx));
    }

    /** Жазылған жолдар, ретімен. Сырттан өзгертуге болмайды. Кеңес: List.copyOf(...) */
    public List<String> lines() {
        return List.copyOf(lines);
    }

    /**
     * ДАЙЫН. Пішім:
     *   "KZ0001 #1 DEPOSIT +1000.00 ₸, balance 1000.00 ₸"
     *   "KZ0001 #3 FEE -100.00 ₸ (fee for #2), balance 600.00 ₸"
     */
    static String format(Account account, Transaction tx) {
        String sign = tx.type().isCredit() ? "+" : "";
        String note = tx.description().isEmpty() ? "" : " (" + tx.description() + ")";
        return account.getId() + " #" + tx.id() + " " + tx.type() + " " + sign + tx.signedAmount()
                + note + ", balance " + tx.balanceAfter();
    }
}
