package kz.learn.bank.fee;

import kz.learn.bank.model.Money;

/**
 * STRATEGY: шешу/аудару комиссиясын есептеу ережесі.
 *
 * ToDo мен кітапханада мінез-құлық МҰРАГЕРЛІК арқылы өзгерді (StudentMember.applyDiscount override).
 * Мұнда басқа жол — КОМПОЗИЦИЯ: Account-тың өзінде FeePolicy өрісі бар, ережені сырттан береміз.
 *   + бір Account класы — кез келген тариф; жаңа тариф = жаңа класс немесе тіпті лямбда;
 *   + тарифті тексеру оңай (Account-сыз);
 *   + «тегін + овердрафт», «пайыз + овердрафтсыз» — комбинациялар үшін ұрпақтар саны жарылмайды.
 *
 * @FunctionalInterface — бір абстрактілі метод, сондықтан: FeePolicy p = amount -> Money.of(50);
 */
@FunctionalInterface
public interface FeePolicy {

    /** Осы сомаға комиссия. Ешқашан null емес, теріс емес. */
    Money feeFor(Money amount);

    /** Комиссиясыз тариф. Кеңес: лямбда қайтар. */
    static FeePolicy none() {
        return amount -> Money.ZERO;
    }
}
