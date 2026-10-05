package kz.learn.bank.fee;

import kz.learn.bank.model.Money;

/**
 * Тұрақты комиссия: сомаға қарамай әрқашан fee. Стратегия — record (immutable, күйі жоқ).
 * record интерфейсті жүзеге асыра алады, бірақ класстан мұралана алмайды (ол java.lang.Record-тан мұраланған).
 */
public record FixedFee(Money fee) implements FeePolicy {

    /** fee null болмайды (NullPointerException); теріс болса — IllegalArgumentException("fee must not be negative"). */
    public FixedFee {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Money feeFor(Money amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
