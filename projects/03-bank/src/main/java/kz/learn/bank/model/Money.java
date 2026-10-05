package kz.learn.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Ақша сомасы (теңгемен) — IMMUTABLE VALUE OBJECT.
 *
 * Неге double емес: double екілік бөлшек, 0.1-ді дәл сақтай алмайды (0.1 + 0.2 = 0.30000000000000004).
 * Ақшада бір тиын қате де — қате. BigDecimal ондық санды дәл сақтайды.
 *
 * Ережелер:
 *  - масштаб (scale) әрқашан 2: конструкторда amount.setScale(SCALE, RoundingMode.HALF_EVEN);
 *  - HALF_EVEN («банкирлік» дөңгелектеу): дәл ортасы ең жақын ЖҰП цифрға: 0.125 -> 0.12, 0.135 -> 0.14.
 *    HALF_UP-қа қарағанда көп операцияда жүйелі түрде жоғары ауытқымайды;
 *  - immutable: final класс, private final өріс, әр операция ЖАҢА Money қайтарады (String сияқты);
 *  - объект тек статикалық фабрика арқылы: Money.of("10.50"), Money.of(100). Конструктор — private.
 *    Money.of(double) әдейі ЖОҚ: new BigDecimal(0.1) = 0.1000000000000000055511151231257827...
 *
 * Өріс: amount (BigDecimal).
 */
public final class Money implements Comparable<Money> {

    private static final int SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /** Конструктор дайын болғанда ғана жұмыс істейді (static өріс класс жүктелгенде жасалады). */
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    // TODO: өрісті жаз

    /** amount-ты SCALE-ге HALF_EVEN-мен келтіріп сақта. */
    private Money(BigDecimal amount) {
        // TODO
    }

    /** "1234.5" -> 1234.50. Сан емес мәтін -> NumberFormatException (ол IllegalArgumentException-ның ұрпағы). */
    public static Money of(String amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** 100 -> 100.00. Кеңес: BigDecimal.valueOf(long) */
    public static Money of(long amount) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public BigDecimal amount() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money plus(Money other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Money minus(Money other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Таңбасын ауыстырады: 100 -> -100. */
    public Money negate() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Сомадан пайыз: amount * percent / 100, нәтиже HALF_EVEN-мен 2 таңбаға.
     * Money.of(1000).percent(new BigDecimal("1.5")) -> 15.00
     * Кеңес: 100-ге бөлу әрқашан дәл (шексіз бөлшек болмайды), дөңгелектеуді конструктор жасайды.
     */
    public Money percent(BigDecimal percent) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Екеуінің үлкені (тең болса — this). */
    public Money max(Money other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** > 0. Кеңес: BigDecimal.signum() */
    public boolean isPositive() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** < 0 */
    public boolean isNegative() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** this > other (қатаң). */
    public boolean isGreaterThan(Money other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int compareTo(Money other) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * ТҰЗАҚ: new BigDecimal("1.0").equals(new BigDecimal("1.00")) == false — BigDecimal.equals масштабты да
     * салыстырады. Бізде масштаб әрқашан 2, сондықтан amount.equals(...) қауіпсіз. Бірақ масштабты
     * келтірмесең, Money.of("1.0") мен Money.of("1.00") тең болмас еді — HashMap кілті ретінде апат.
     */
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

    /** Пішім: "1234.50 ₸". Кеңес: toPlainString() (toString() үлкен санда 1E+3 жазуы мүмкін). */
    @Override
    public String toString() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
