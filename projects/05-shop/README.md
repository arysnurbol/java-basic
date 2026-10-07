# Жоба 5 — Интернет-дүкен (State, Strategy, Factory бірге)

Консольдік дүкен: каталог, қойма қалдығы, себет, промокод, жеткізу тәсілі және тапсырыстың өмірлік циклі
(жаңа → төленді → жіберілді → жеткізілді, не болдырылмады), сатылым есептері.
Қабаттар бұрынғыдай: **model → repository → service → UI**.

Қонақүйден не жаңа:

* **State:** тапсырыстың әр күйі — жеке класс. `Order` ішінде `if (status == ...)` жоқ, `state = state.ship()` деп жазылады;
* **Strategy екі түрлі:** `DiscountPolicy` — интерфейс және record-тар (банктегі `FeePolicy` сияқты), ал `DeliveryType` — **әр константасының өз денесі бар enum**;
* **Factory мәтіннен:** `DiscountFactory.fromCode("SALE10")`. Клиент мәтін береді, класты фабрика таңдайды;
* **Үш үлгі бірге:** сервис күйді, жеңілдікті немесе жеткізу ережесін білмейді, бәрін тиісті объектіге тапсырады;
* **интерфейстегі `private` әдіс** (Java 9+) және **«алдымен тексер, сосын өзгерт»** тәсілі екі объект арасында (қойма мен тапсырыс).

`Money` (жаңа `times(int)` әдісі қосылған), `Repository`, `InMemoryRepository`, `Identifiable`, `Category` және `OrderStatus` **дайын**.
Бұл жобада уақыт жоқ, сондықтан `Clock` пен `TestClock` да жоқ.

## Үлгілер мен ұғымдар қай жерде

| Ұғым | Қай жерде |
|---|---|
| `record` + compact конструктор | `Product`, `OrderLine`, `PercentDiscount`, `FixedDiscount` |
| Record `Identifiable`-ді жүзеге асырады | `Product.getId()` → `id()` |
| **Әр константаның денесі бар enum** | `DeliveryType`: `PICKUP { ... }`, `COURIER { ... }`, `POST { ... }` |
| **Strategy** (интерфейс + лямбда) | `DiscountPolicy` ← `PercentDiscount`, `FixedDiscount`, `none()` |
| **Factory** (утилита-класс) | `DiscountFactory.fromCode(String)` |
| **State** | `OrderState` ← `NewState`, `PaidState`, `ShippedState`, `DeliveredState`, `CancelledState` |
| `default` және `private` интерфейс әдістері | `OrderState`: әдепкі «рұқсат жоқ», ортақ `notAllowed(...)` |
| Context (State үлгісінде) | `Order`: күйді сақтайды, әрекетті күйге тапсырады |
| Инкапсуляция | `Order.getLines()` көшірме қайтарады, құрамы тек `NEW` күйінде өзгереді |
| «Алдымен тексер, сосын өзгерт» | `ShopService.pay`: қалдық → күй → енді ғана есептен шығару |
| Collections + Stream | `Map.merge`, `flatMap`, `groupingBy` + `EnumMap` + `counting` / `reducing` / `summingInt`, `Map.Entry` компараторлары |

## Ережелер

**Тапсырыс күйлері:**

```
        pay()          ship()           deliver()
  NEW ───────▶ PAID ────────▶ SHIPPED ──────────▶ DELIVERED
   │            │
   └─ cancel() ─┴──────────▶ CANCELLED
```

* Құрамы (тауарлар, промокод) тек `NEW` күйінде өзгереді. Бос тапсырысты төлеуге болмайды.
* Қалдық себетке салғанда **тексерілмейді**, тек төлегенде тексеріліп, есептен шығады.
* `PAID` күйіндегі тапсырыс болдырылса, тауар қоймаға қайтады, ақша толық қайтарылады. `SHIPPED` күйінен кейін болдыруға болмайды.

**Ақша:** `total = subtotal − жеңілдік + жеткізу`. Жеткізу құны **жеңілдіктен кейінгі** сомадан есептеледі.

| Жеткізу | Құны |
|---|---|
| `PICKUP` | тегін |
| `COURIER` | 1 500 ₸; тауар сомасы ≥ 20 000 ₸ болса — тегін |
| `POST` | 1 000 ₸ + әр данаға 200 ₸ |

| Промокод | Жеңілдік |
|---|---|
| бос | жоқ |
| `SALE<n>` (n = 1..50) | n% |
| `MINUS<n>` (n > 0) | n ₸, бірақ тауар сомасынан аспайды |
| басқасы | `InvalidPromoCodeException` |

## Құрылым

```
src/main/java/kz/learn/shop/
├── exception/   ShopException, ProductNotFoundException, OrderNotFoundException,
│                OutOfStockException, OrderStateException, InvalidPromoCodeException
├── model/       Identifiable, Money, Category (дайын), Product, OrderLine, Order
├── delivery/    DeliveryType                                    ← Strategy (enum)
├── discount/    DiscountPolicy, PercentDiscount, FixedDiscount  ← Strategy
│                DiscountFactory                                 ← Factory
├── state/       OrderStatus (дайын), OrderState, NewState, PaidState,
│                ShippedState, DeliveredState, CancelledState    ← State
├── repository/  Repository, InMemoryRepository (дайын)
├── service/     ShopService
└── ConsoleApp   (дайын)
src/test/java/...  Step1…Step8 тесттері — ӨЗГЕРТПЕ, бірақ ОҚЫ
```

Бұрынғыдай: `throw new UnsupportedOperationException("TODO");` жолын өшіріп, өз кодыңды жаз;
`// TODO: өрістерді жаз` деген жерге өрістерді жаз; `super("TODO");` орнына дұрыс хабарламаны қой.

## Жүгірту

```powershell
cd projects\05-shop

.\mvnw.cmd test                          # барлық тест
.\mvnw.cmd test "-Dtest=Step1*"          # тек 1-қадам
.\mvnw.cmd -q compile exec:java          # бәрі жасыл болғанда — қосымша
```

Git Bash: `./mvnw test -Dtest='Step5*'`. IntelliJ: `projects/05-shop/pom.xml` → **Open as Project**.

---

## Қадам 1 — `Product`, `OrderLine` және exception-дар

**Файлдар:** `model/Product.java`, `model/OrderLine.java`, `exception/*.java` · **Тест:** `Step1ModelTest`

* Record-тың compact конструкторы `DateRange`-дегідей жазылады. Айырмасы: мұнда параметрді **қайта тағайындауға** болады (`id = requireText(id, "id")`), өріс сол жаңа мәнді алады.
* `OrderLine.total()` дайын `Money.times` әдісін қолданады.
* Exception-дар қонақүйдегідей жазылады.

**Өзіңе сұрақ:** қойма қалдығы неге `Product`-та емес? `Product` record болғандықтан, оған `stock` өрісін қосу үшін
`withStock(...)` сияқты әдіспен әр жолы жаңа объект жасау керек болар еді. Ал `Order`-дағы тауар сол сәтте ескі мәнді ұстап тұрады. Бұл жақсы ма, әлде жаман ба?

## Қадам 2 — `DeliveryType`: әр константаның өз денесі бар enum

**Файл:** `delivery/DeliveryType.java` · **Тест:** `Step2DeliveryTest`

* Әр константаның `{ ... }` денесіндегі `costFor` әдісін толтыр. `switch` қажет емес.
* `COURIER` үшін шекара **кіреді**: дәл 20 000 ₸ болса, жеткізу тегін. `Money`-ді `compareTo` арқылы салыстыр.

**Назар аудар:** тест `COURIER.getClass()` мен `COURIER.getDeclaringClass()` әртүрлі екенін тексереді.
Денесі бар константа — enum-ның **анонимді ұрпағы**. Сондықтан enum-ды `getClass()` арқылы салыстырма.

**Өзіңе сұрақ:** `RoomType`-та баға **өріс** ретінде берілді, ал мұнда **әдіс** ретінде берілді. Қашан өріс жеткілікті, қашан әр константаға өз әдісі керек?
Ал жеткізу тәсілдері жиі қосылып тұрса (жаңа серіктес, жаңа қала), enum ыңғайлы бола ма, әлде `DiscountPolicy` сияқты интерфейс жақсы ма?

## Қадам 3 — Strategy: `DiscountPolicy`

**Файлдар:** `discount/DiscountPolicy.java`, `PercentDiscount.java`, `FixedDiscount.java` · **Тест:** `Step3DiscountTest`

* `none()` бір жолдық лямбдамен жазылады.
* `PercentDiscount` `Money.percent`-ке `BigDecimal.valueOf(percent)` береді.
* `FixedDiscount` жеңілдікті тауар сомасынан асырмайды. Тапсырыс сомасы теріс болмауы керек.

## Қадам 4 — Factory: `DiscountFactory`

**Файл:** `discount/DiscountFactory.java` · **Тест:** `Step4DiscountFactoryTest`

* Кодты алдымен қалыпқа келтір: `strip()` және `toUpperCase(Locale.ROOT)`. Сосын `startsWith` пен `substring` арқылы префиксті тексеріп, санды бөліп ал.
* Хабарламада **қалыпқа келген** код тұрады: `" hello "` → `"Invalid promo code: HELLO"`.

**Назар аудар:** бұл жерде қате **екі** жерден шығуы мүмкін. `Integer.parseInt("abc")` `NumberFormatException` береді,
ал `new PercentDiscount(99)` `IllegalArgumentException` береді. `NumberFormatException` — `IllegalArgumentException`-ның **ұрпағы**.
Бір `catch` екеуін де ұстай ала ма? Ал тек `catch (NumberFormatException e)` деп жазсаң, `SALE99` не береді?

**Өзіңе сұрақ:** `toUpperCase()` неге `Locale.ROOT`-пен жазылады? (Түрік тілінде `"i".toUpperCase()` не береді?)

## Қадам 5 — State: күйлер

**Файлдар:** `state/OrderState.java`, `NewState.java`, `PaidState.java`, `ShippedState.java`, `DeliveredState.java`, `CancelledState.java` · **Тест:** `Step5StateTest`

* `OrderState` интерфейсінде барлық `default` әдіс «рұқсат жоқ» деп `throw notAllowed("pay")` орындайды. `canEdit()` әдепкі бойынша `false` қайтарады.
* `notAllowed` exception-ды **қайтарады**, ал лақтыру `default` әдістің ісі. Неге бұлай жазылғанын ойла: `throw notAllowed(...)` жазылған соң, компилятор әдістің мұнда біткенін біледі.
* Әр күй тек **өзіне рұқсат етілген** әрекеттерді override етеді. Әрекет **келесі күйді қайтарады**, ал өзін өзгертпейді.

**Өзіңе сұрақ:**
1. Бұл ережелер 01-todo-да `Status.canMoveTo()` арқылы жазылған еді. Жаңа күй (мысалы, `RETURNED` — «қайтарылды») қосу керек болса,
   қай тәсілде қанша файлға тиесің? Ал жаңа **әрекет** (`return`) қосу керек болса ше?
2. Күйлердің ешқайсысында өріс жоқ. Әр жолы `new PaidState()` жасаудың қажеті бар ма? Бір дана жеткілікті болса,
   оларды singleton немесе тіпті **enum** етіп жазуға бола ма? Онда `DeliveryType` пен `OrderState` неге ұқсас болып шығады?

## Қадам 6 — `Order`: State үлгісінің context-і

**Файл:** `model/Order.java` · **Тест:** `Step6OrderTest`

* Әрбір өзгерту әрекеті алдымен `requireEditable()`-ді шақырады. Бұл тексеріс күйден сұрайды (`state.canEdit()`), ал күйдің атауын (`NEW`) өзі тексермейді.
* `addItem`: тауар бұрыннан бар болса, санын қос. Javadoc-тағы **тұзақты** оқы: `0` дана қосу да қате саналуы керек.
* Сомалар әр жолы қайта есептеледі (`stream` + `reduce`). Жеткізу құнына **жеңілдіктен кейінгі** сома беріледі.
* `pay()`: алдымен `state.pay()`, сосын бос емес екенін тексер, тек содан кейін `state`-ке меншікте.

**Назар аудар:** `ship()`, `deliver()` және `cancel()` бір жолмен жазылады. `Order`-да күйге қатысты бірде-бір `if` немесе `switch` болмауы керек.

**Өзіңе сұрақ:** `pay()`-да тексерістердің ретін ауыстырсаң, `CANCELLED` күйіндегі бос тапсырыс қандай хабарлама береді? Пайдаланушыға қайсысы түсінікті?

## Қадам 7 — `ShopService`: операциялар

**Файл:** `service/ShopService.java` (7-қадам бөлімі) · **Тест:** `Step7ServiceTest`

* `addProduct` және `createOrder` қонақүйдегідей жазылады: алдымен жаса, сосын тексер. Есептегішті сәтті жасалғаннан кейін ғана арттыр.
* Қоймада `Map.merge(id, n, Integer::sum)` қолданылады: қосқанда `n`, есептен шығарғанда `-n`.
* `pay`: **барлық** жолдың қалдығын тексерген соң ғана біреуін азайтуға болады. Тест F1 жеткілікті, ал P1 жетпейтін жағдайды тексереді.
* `cancel`: күйін `order.cancel()`-ге **дейін** есте сақта, кейін ол `CANCELLED` болып кетеді.

**Назар аудар:** сервис `instanceof PaidState` немесе `new PercentDiscount(...)` деп жазбайды.
Күйді `Order` біледі, жеңілдікті фабрика жасайды. Сервис тек әрекеттердің ретін басқарады.

**Өзіңе сұрақ:** `pay` алдымен қалдықты, сосын күйді тексереді. Тапсырыс **бұрыннан** төленген болса, ал қалдық енді жетпесе,
пайдаланушы қандай қате көреді? Дұрыс хабарлама шығару үшін не өзгерту керек (`Order`-ға қандай әдіс қосар едің)?

## Қадам 8 — Есептер

**Файл:** `service/ShopService.java` (8-қадам бөлімі) · **Тест:** `Step8ReportsTest`

* `paidOrders()` — `findWhere` арқылы жазылатын көмекші әдіс. Есептердің үшеуі де соны қолданады.
* `salesByCategory`: `flatMap` арқылы тапсырыстардан жолдарға өт, сосын 04-hotel-дегі `revenueByType` сияқты жаз.
* `topProducts`: `Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())`.
  `<String, Integer>` типін жазбасаң, компилятор `reversed()`-тан кейін типті шығара алмайды. Байқап көр.

**Өзіңе сұрақ:** `salesByCategory` жеңілдікке **дейінгі** соманы, ал `revenue` жеңілдіктен **кейінгі** соманы есептейді. Неге
бұл есептердің қосындысы сәйкес келмейді? `SALE10` жеңілдігі 3 санаттағы тауарға қолданылса, оны санаттарға қалай бөлер едің?

## Қадам 9 — Іске қос

`ConsoleApp`-та демо-деректер бар: 5 тауар және Aru-дың төленген тапсырысы `ORD-0001`.
Мынаны байқап көр: жаңа тапсырыс жасап, қоймада екі-ақ дана бар ноутбукті (`P1`) 3 данамен төлеп көр;
`MINUS5000` промокодын арзан тапсырысқа қолданып көр; `ORD-0001`-ді жіберіп, сосын болдырмақ болып көр.

---

## Қосымша (міндетті емес, тест жоқ)

1. **Қайтару (RETURNED):** жеткізілгеннен кейін 14 күн ішінде тауарды қайтаруға болады. Қанша файлға тидің?
   Күнді қайдан аласың — `Clock` қайта керек бола ма? `DeliveredState` енді өріс ұстай ма (жеткізілген күн)?
2. **Күйлер — enum:** `OrderState`-ті `enum OrderStateEnum { NEW { ... }, PAID { ... } ... }` етіп қайта жаз. `OrderStatus` енді керек пе?
3. **Жеңілдіктерді біріктіру:** `DiscountPolicy.best(DiscountPolicy... options)` — ең үлкен жеңілдікті таңдайтын стратегия.
   Ал «алдымен 10%, сосын тағы 1 000 ₸» сияқты жеңілдіктерді тізбектеп қолдану керек болса ше? Бұл қай үлгіге ұқсайды (Composite? Decorator?)
4. **Observer қайта:** тапсырыс күйі өзгергенде хабарлама жіберу (банктегі `TransactionPublisher` сияқты). Хабарламаны кім
   жариялауы керек: `Order` ме, күй кластары ма, әлде `ShopService` пе?
