package kz.learn.bank.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Шоттағы бір операция — RECORD (Java 16+).
 *
 * record — immutable деректер класы: компилятор өзі жасайды
 *   private final өрістер, канондық конструктор, accessor-лар (id(), type(), ... — get-сіз!),
 *   equals/hashCode/toString.
 * Банкте өткен транзакция ешқашан өзгермейді — қате болса, жаңа (кері) транзакция жазылады.
 * Сондықтан record мұнда дәл келеді.
 *
 * balanceAfter — осы операциядан кейінгі баланс (банк көшірмесіндегідей).
 */
public record Transaction(long id, TransactionType type, Money amount, Money balanceAfter,
                          LocalDateTime at, String description) {

    /**
     * COMPACT конструктор: параметрлер тізімі жазылмайды, өрістерге меншіктеу де жазылмайды —
     * компилятор оны соңында өзі қосады. Мұнда тек тексеріс пен нормалау.
     *
     *  - type, amount, balanceAfter, at — null болмайды: Objects.requireNonNull(x, "type");
     *  - amount > 0, әйтпесе IllegalArgumentException("amount must be positive")
     *    (ақша бағытын amount таңбасы емес, type береді);
     *  - description: null -> "", әйтпесе strip(). Параметрге қайта меншікте: description = ...;
     */
    public Transaction {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(balanceAfter, "balanceAfter");
        Objects.requireNonNull(at, "at");
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("amount must be positive");
        }
        description = description == null ? "" : description.strip();
    }

    /** Таңбасы бар сома: кіріс — оң, шығыс — теріс. Кеңес: type.isCredit(), amount.negate(). */
    public Money signedAmount() {
        return type.isCredit() ? amount : amount.negate();
    }
}
