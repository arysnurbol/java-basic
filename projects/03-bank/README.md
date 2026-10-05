# Жоба 3 — Банк жүйесі (BigDecimal, immutable, Strategy, Observer)

Консольдік банк: шот ашу, салу, шешу, аудару, комиссиялар, овердрафт, транзакция тарихы
және есептер. Қабаттар бұрынғыдай: **model → repository → service → UI**.

Кітапханадан не жаңа:

* **`BigDecimal`** — ақшаны `double`-сыз есептеу, масштаб, дөңгелектеу (`HALF_EVEN`);
* **immutable value object** (`Money`) және **`record`** (`Transaction`, `FixedFee`, `PercentFee`);
* **Strategy** — мінез-құлық мұрагерлікпен емес, **композициямен** беріледі (`FeePolicy`);
* **Observer** — сервис «оқиға болды» деп жариялайды, кім не істейтінін білмейді;
* **`Clock`** — уақытқа тәуелді кодты тестілеу;
* **«validate, then mutate»** — сәтсіз операция жартылай із қалдырмайды;
* Collections: `EnumMap`, `Collectors.reducing`, `Stream.reduce`, `Comparable` өз класыңда.

`Repository` / `InMemoryRepository` / `Identifiable` — **дайын**, 02-library-дан өзгеріссіз алынды.
Generic абстракцияның пайдасы осы: бір рет жазылды — қайта қолданылады.

## Үлгілер мен ұғымдар қай жерде

| Ұғым | Қай жерде |
|---|---|
| Immutable value object | `Money` — `final` класс, `private final BigDecimal`, әр операция жаңа объект |
| Статикалық фабрика | `Money.of(...)`, `FeePolicy.none()` — конструктордың орнына |
| `record` + compact конструктор | `Transaction`, `FixedFee`, `PercentFee` |
| Өрісі бар enum | `TransactionType(boolean credit)` |
| Exception иерархиясы | `BankException` ← `AccountNotFoundException`, `InsufficientFundsException` |
| **Strategy** | `FeePolicy` ← `FixedFee`, `PercentFee`, `none()`, кез келген лямбда |
| Инкапсуляция (өзгермелі объект) | `Account` — баланс тек операциялар арқылы, `history()` тек оқуға |
| **Observer** | `TransactionPublisher` (subject) → `TransactionListener` ← `AuditLog`, `LargeTransactionAlert` |
| Композиция + delegation | `BankService` ішінде `TransactionPublisher`, `subscribe()` оған тапсырады |
| DI + тестіленетін уақыт | `BankService(Repository, Clock)`, тестте `TestClock` |
| Collections + Stream | `totalsByType()` (`EnumMap` + `reducing`), `totalBalance()` (`reduce`), `topByBalance()` |

## Ережелер

| Тариф (консольде) | Комиссия (шешу және аудару жіберу) | Овердрафт |
|---|---|---|
| Тегін | жоқ — `FeePolicy.none()` | 0 |
| Ағымдағы | 100 ₸ — `FixedFee` | 0 |
| Кредиттік | 1.5%, кемі 200 ₸ — `PercentFee` | 50 000 ₸ |

* Салу (`DEPOSIT`) мен аударым қабылдау (`TRANSFER_IN`) — комиссиясыз.
* Комиссия — бөлек `FEE` транзакциясы (банк көшірмесіндегідей), негізгісінен кейін.
* Операцияға рұқсат: `сома + комиссия <= balance + overdraftLimit`.
* Барлық сома — 2 таңбаға дейін, `HALF_EVEN`.

## Құрылым

```
src/main/java/kz/learn/bank/
├── exception/   BankException, AccountNotFoundException, InsufficientFundsException
├── model/       Identifiable (дайын), Money, TransactionType, Transaction, Account
├── fee/         FeePolicy, FixedFee, PercentFee                 ← Strategy
├── event/       TransactionListener (дайын), TransactionPublisher,
│                AuditLog, LargeTransactionAlert                 ← Observer
├── repository/  Repository, InMemoryRepository (дайын)
├── service/     BankService
└── ConsoleApp   (дайын)
src/test/java/...  Step1…Step7 тесттері + TestClock — ӨЗГЕРТПЕ, бірақ ОҚЫ
```

Бұрынғыдай: `throw new UnsupportedOperationException("TODO");` жолын өшіріп, өз кодыңды жаз;
`// TODO: өрістерді жаз` — сол жерге өрістер; `super("TODO");` — дұрыс хабарлама.

## Жүгірту

```powershell
cd projects\03-bank

.\mvnw.cmd test                          # барлық тест
.\mvnw.cmd test "-Dtest=Step1*"          # тек 1-қадам
.\mvnw.cmd -q compile exec:java          # бәрі жасыл болғанда — қосымша
```

Git Bash: `./mvnw test -Dtest='Step4*'`. IntelliJ: `projects/03-bank/pom.xml` → **Open as Project**.

> Екі тест (құрылымды тексеретіндер) басынан-ақ жасыл — бұл қалыпты.

---

## Қадам 1 — `Money`: ақша `double`-сыз

**Файл:** `model/Money.java` · **Тест:** `Step1MoneyTest`

* Конструктор: `amount.setScale(SCALE, RoundingMode.HALF_EVEN)` — осы бір жол барлық дөңгелектеуді шешеді.
* Арифметика: `add`, `subtract`, `negate`, `multiply`, `divide` — бәрі **жаңа** `BigDecimal` қайтарады,
  сондықтан `Money` да жаңасын қайтарады.
* Салыстыру — `compareTo`, ешқашан `==`.

**Назар аудар:** `ZERO` — `static final` өріс, ол класс жүктелгенде конструкторды шақырады.
Конструкторды жазбай тұрып, бүкіл класс жұмыс істемейді.

**Өзіңе сұрақ:** `new BigDecimal("0.1")` мен `new BigDecimal(0.1)` — айырмасы неде? Неге `Money.of(double)` жоқ?

## Қадам 2 — Exception-дар, `TransactionType`, `Transaction` (record)

**Файлдар:** `exception/*.java`, `model/TransactionType.java`, `model/Transaction.java` · **Тест:** `Step2TransactionTest`

* Exception-дар — кітапханадағыдай, қысқаша.
* `TransactionType` — өрісі бар enum: `DEPOSIT(true)` → `isCredit()`.
* `Transaction` — **record**. Тек compact конструктор мен `signedAmount()` жазасың;
  accessor-ларды, `equals`, `hashCode`, `toString`-ті компилятор жасайды.

**Назар аудар:** compact конструкторда `this.description = ...` жазбайсың — **параметрге** меншіктейсің
(`description = ...`), өріске компилятор өзі көшіреді.

**Өзіңе сұрақ:** `Transaction`-ды record етуге болады, ал `Account`-ты — жоқ. Неге?

## Қадам 3 — Strategy: `FeePolicy`

**Файлдар:** `fee/*.java` · **Тест:** `Step3FeePolicyTest`

* `none()` — лямбда қайтаратын статикалық метод.
* `FixedFee`, `PercentFee` — record-тар, тексеріс compact конструкторда.
* Тесттің соңғысы: жаңа тариф үшін класс та керек емес — лямбда жеткілікті.

**Өзіңе сұрақ:** кітапханада жеңілдікті `StudentMember extends Member` + override арқылы бердік.
Тарифтерді `FixedFeeAccount extends Account`, `PercentFeeAccount extends Account` деп жасасақ,
енді овердрафт та, «студенттік» те керек болса — неше класс шығады? Strategy-мен ше?

## Қадам 4 — `Account`

**Файл:** `model/Account.java` · **Тест:** `Step4AccountTest`

* Барлық ақша шығаратын операция — бір `debit(...)` арқылы, барлық жазу — бір `record(...)` арқылы.
* `debit`: алдымен **барлық тексеріс**, содан кейін ғана өзгеріс.
* `history()` — `Collections.unmodifiableList(...)`.

**Назар аудар:** `Account`-та бірде-бір `instanceof FixedFee` жоқ. Ол тек `feePolicy.feeFor(amount)` шақырады.

**Өзіңе сұрақ:** `history()` `List.copyOf(history)` қайтарса да тест өтеді. `unmodifiableList`-тен айырмасы неде?
(Кеңес: кейін жаңа транзакция қосылса, бұрын алынған тізімде не көрінеді?)

## Қадам 5 — Observer: оқиғалар

**Файлдар:** `event/TransactionPublisher.java`, `AuditLog.java`, `LargeTransactionAlert.java` · **Тест:** `Step5EventsTest`

* `TransactionPublisher` — тізім + екі қабат `for`.
* `AuditLog` — жолдарды жинайды (пішімдеу дайын).
* `LargeTransactionAlert` — шекті тексеріп, хабарды `Consumer<String>`-ке береді.

**Өзіңе сұрақ:** бақылаушылардың бірі exception лақтырса не болады? Қалғандары хабар ала ма?
Ал ақша қозғалысы кері қайта ма? (Кеңес: `publish` операциядан **кейін** шақырылады.)

## Қадам 6 — `BankService`: операциялар

**Файл:** `service/BankService.java` (6-қадам бөлімі) · **Тест:** `Step6ServiceTest`

* Уақыт — тек `LocalDateTime.now(clock)`.
* Әр сәтті операциядан кейін — `publisher.publish(...)`. Сәтсізінен кейін — ештеңе.
* `transfer` — ретін Javadoc-тағыдай сақта.

**Назар аудар:** `TestClock`-ты оқы. Ол `Clock`-тан мұраланған 30 жолдық класс — бірақ соның арқасында
7-қадамның тесті «4 түрлі күнді» бір секундта тексереді.

## Қадам 7 — Есептер

**Файл:** `service/BankService.java` (7-қадам бөлімі) · **Тест:** `Step7ReportsTest`

* `statement` — `filter` күн бойынша.
* `totalsByType` — `groupingBy` + `EnumMap` + `reducing`.
* `totalBalance` — `map(...).reduce(Money.ZERO, Money::plus)`.
* `topByBalance` — `Comparator.comparing(...).reversed().thenComparing(...)` + `limit`.

**Назар аудар:** `Money::plus` — `BinaryOperator<Money>`. Immutable объектінің арқасында `reduce` қауіпсіз:
аралық нәтиже ешқайда «ағып» кетпейді.

## Қадам 8 — Іске қос

`ConsoleApp`-та демо-деректер бар (`KZ0001` — Aru, тегін; `KZ0002` — Daniyar, 100 ₸ комиссия).
Байқап көр: `KZ0002`-ден 79 950 шешіп көр (комиссиямен жетпейді); кредиттік шот ашып, нөлден
40 000 шеш; `KZ0001`-ге 500 000 сал — ескертуді көресің.

---

## Қосымша (міндетті емес, тест жоқ)

1. **Пайыз есептеу:** `InterestPolicy` стратегиясы + `BankService.accrueInterest()` — жаңа `INTEREST` түрі.
   `Account`-та не өзгерді?
2. **SMS-хабарлама:** жаңа бақылаушы — тек `WITHDRAWAL` мен `TRANSFER_OUT` үшін. `BankService`-ке тидің бе?
3. **Көп ағынды аударым:** екі ағын бір мезгілде `KZ0001 → KZ0002` және `KZ0002 → KZ0001` аударса?
   `synchronized` қос, сосын deadlock-ты қалай болдырмайсың? (Кеңес: құлыптарды нөмір ретімен ал.)
4. **Валюта:** `Money`-ге `Currency` өрісі; әртүрлі валютаны қосу — exception.
