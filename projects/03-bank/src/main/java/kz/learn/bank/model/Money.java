package kz.learn.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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

    private final BigDecimal amount;

    /** amount-ты SCALE-ге HALF_EVEN-мен келтіріп сақта. */
    private Money(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount cannot be null");
        // 1-ҚАТЕ: Конструктор масштабты келтіреді және HALF_EVEN-мен дөңгелектейді
        this.amount = amount.setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    /** "1234.5" -> 1234.50. Сан емес мәтін -> NumberFormatException (ол IllegalArgumentException-ның ұрпағы). */
    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    /** 100 -> 100.00. Кеңес: BigDecimal.valueOf(long) */
    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public BigDecimal amount() {
        return amount;
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other money cannot be null");
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "other money cannot be null");
        return new Money(amount.subtract(other.amount));
    }

    /** Таңбасын ауыстырады: 100 -> -100. */
    public Money negate() {
        return new Money(amount.negate());
    }

    /**
     * Сомадан пайыз: amount * percent / 100, нәтиже HALF_EVEN-мен 2 таңбаға.
     * Money.of(1000).percent(new BigDecimal("1.5")) -> 15.00
     * Кеңес: 100-ге бөлу әрқашан дәл (шексіз бөлшек болмайды), дөңгелектеуді конструктор жасайды.
     */
    public Money percent(BigDecimal percent) {
        Objects.requireNonNull(percent, "percent cannot be null");
        // 2-ҚАТЕ: Формула дұрысталды (amount * percent / 100).
        // Дөңгелектеуді конструктор өзі реттейтіндіктен, жай ғана HUNDRED-ке бөлеміз.
        BigDecimal result = amount.multiply(percent).divide(HUNDRED);
        return new Money(result);
    }

    /** Екеуінің үлкені (тең болса — this). */
    public Money max(Money other) {
        Objects.requireNonNull(other, "other money cannot be null");
        // БАСТЫ ҚАТЕ: assertSame тестінен өту үшін жаңа объект жасамай,
        // compareTo арқылы дәл сол нысанның сілтемесін (this немесе other) қайтарамыз.
        return this.compareTo(other) >= 0 ? this : other;
    }

    /** > 0. Кеңес: BigDecimal.signum() */
    public boolean isPositive() {
        // ҰСАҚ ЕСКЕРТУ: signum() қолдану қысқа әрі тиімді
        return amount.signum() > 0;
    }

    /** < 0 */
    public boolean isNegative() {
        return amount.signum() < 0;
    }

    /** this > other (қатаң). */
    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "other money cannot be null");
        return amount.compareTo(other.amount) > 0;
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    /**
     * ТҰЗАҚ: new BigDecimal("1.0").equals(new BigDecimal("1.00")) == false — BigDecimal.equals масштабты да
     * салыстырады. Бізде масштаб әрқашан 2, сондықтан amount.equals(...) қауіпсіз. Бірақ масштабты
     * келтірмесең, Money.of("1.0") мен Money.of("1.00") тең болмас еді — HashMap кілті ретінде апат.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    /** Пішім: "1234.50 ₸". Кеңес: toPlainString() (toString() үлкен санда 1E+3 жазуы мүмкін). */
    @Override
    public String toString() {
        // 3-ҚАТЕ: toPlainString() қолданылды және соңына талап етілген валюта белгісі қосылды.
        return amount.toPlainString() + " ₸";
    }
}
