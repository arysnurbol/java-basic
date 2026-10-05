package kz.learn.bank.fee;

import kz.learn.bank.model.Money;
import java.util.Objects;

/**
 * Тұрақты комиссия: сомаға қарамай әрқашан fee. Стратегия — record (immutable, күйі жоқ).
 * record интерфейсті жүзеге асыра алады, бірақ класстан мұралана алмайды (ол java.lang.Record-тан мұраланған).
 */
public record FixedFee(Money fee) implements FeePolicy {

    /** fee null болмайды (NullPointerException); теріс болса — IllegalArgumentException("fee must not be negative"). */
    public FixedFee {
        Objects.requireNonNull(fee, "fee");
        if (fee.isNegative()) {
            throw new IllegalArgumentException("fee must not be negative");
        }
    }

    @Override
    public Money feeFor(Money amount) {
        return fee;
    }
}
