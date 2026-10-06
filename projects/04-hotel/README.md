# Жоба 4 — Қонақүй брондау (уақыт, интервалдар, Singleton, Factory)

Консольдік қонақүй: бөлме қосу, бос бөлме іздеу, брондау, болдырмау (ақшаны қайтару ережесімен),
толу пайызы және табыс есептері. Қабаттар бұрынғыдай: **model → repository → service → UI**.

Банктен не жаңа:

* **`java.time` терең:** `LocalDate`, `ChronoUnit.DAYS.between`, `datesUntil`, `DayOfWeek`;
* **интервал логикасы:** жартылай ашық `[checkIn, checkOut)` кезең, `overlaps`-тың бір жолдық формуласы;
* **Singleton:** `HotelPolicy`. Қашан қауіпсіз, қашан «жасырын глобал айнымалы» болып кетеді;
* **Factory:** `RoomFactory`. Клиент нақты кластарды білмейді, олардың конструкторы жабық;
* **абстрактілі класс + Template Method:** `Room.priceFor` абстрактілі `priceForNight`-ты шақырады;
* **Strategy пен мұрагерлікті салыстыру:** банкте тариф композиция арқылы берілді, мұнда бөлме түрі мұрагерлік арқылы берілді. Неге екенін түсіну керек.

`Money`, `Repository`, `InMemoryRepository`, `Identifiable` және `TestClock` **дайын**, 03-bank-тан өзгеріссіз алынды.

## Үлгілер мен ұғымдар қай жерде

| Ұғым | Қай жерде |
|---|---|
| Жартылай ашық интервал | `DateRange` (record): `nights`, `contains`, `overlaps`, `nightDates` |
| Статикалық фабрика | `DateRange.of(checkIn, nights)` |
| Өрісі бар enum (екі өріс) | `RoomType(capacity, basePrice)` |
| **Singleton** (eager, immutable) | `HotelPolicy.getInstance()` |
| Абстрактілі класс, полиморфизм | `Room` ← `StandardRoom`, `DeluxeRoom`, `Suite` |
| Template Method (қарапайым) | `Room.priceFor` → `priceForNight` (әр ұрпақта өзінше), `Suite` `priceFor`-ды толықтырады |
| **Factory** | `RoomFactory.create(RoomType, number)`, `create("deluxe", number)` |
| package-private конструктор | ұрпақтарды `room` пакетінен тыс `new` арқылы жасау мүмкін емес |
| Инкапсуляция (өзгермелі объект) | `Booking`: күйі тек `cancel(...)` арқылы өзгереді, баға бір рет есептеледі |
| DI + тестіленетін уақыт | `HotelService(Repository, Repository, Clock)`, тестте `TestClock` |
| Collections + Stream | `findAvailable` (екі деңгейлі сұрыптау), `revenueByType` (`EnumMap` + `reducing`) |

## Ережелер

| Бөлме | Қонақ | Түн бағасы | Ерекшелігі |
|---|---|---|---|
| `STANDARD` | 2 | 20 000 ₸ | — |
| `DELUXE` | 3 | 35 000 ₸ | жұма мен сенбі **түндері** +25% (43 750 ₸) |
| `SUITE` | 4 | 60 000 ₸ | 7 түн және одан көп болса, **бүкіл** сомаға −10% |

* Кезең `[checkIn, checkOut)`, ақы **түн** үшін алынады. 10-нан 13-ке дейін — 3 түн. 13-і кету күні, сол күні бөлмеге келесі қонақ кіре алады.
* Бір бронь ең көбі 30 түн. Өткен күнге брондауға болмайды, бүгінге болады.
* Болдырмау: келуге **7 күн және одан көп** қалса, ақша толық қайтарылады; **1–6 күн** қалса, жартысы қайтарылады; келу күні және одан кейін ештеңе қайтарылмайды.
* Болдырылмаған бронь бөлмені бөгемейді. Қайтарылмаған бөлігі қонақүйдің табысы болып саналады (`revenue()`).

## Құрылым

```
src/main/java/kz/learn/hotel/
├── exception/   HotelException, RoomNotFoundException, BookingNotFoundException,
│                RoomNotAvailableException
├── model/       Identifiable, Money (дайын), DateRange, BookingStatus (дайын), Booking
├── policy/      HotelPolicy                                     ← Singleton
├── room/        RoomType, Room, StandardRoom, DeluxeRoom, Suite,
│                RoomFactory                                     ← Factory
├── repository/  Repository, InMemoryRepository (дайын)
├── service/     HotelService
└── ConsoleApp   (дайын)
src/test/java/...  Step1…Step7 тесттері + TestClock — ӨЗГЕРТПЕ, бірақ ОҚЫ
```

Бұрынғыдай: `throw new UnsupportedOperationException("TODO");` жолын өшіріп, өз кодыңды жаз;
`// TODO: өрістерді жаз` деген жерге өрістерді жаз; `super("TODO");` орнына дұрыс хабарламаны қой.

## Жүгірту

```powershell
cd projects\04-hotel

.\mvnw.cmd test                          # барлық тест
.\mvnw.cmd test "-Dtest=Step1*"          # тек 1-қадам
.\mvnw.cmd -q compile exec:java          # бәрі жасыл болғанда — қосымша
```

Git Bash: `./mvnw test -Dtest='Step4*'`. IntelliJ: `projects/04-hotel/pom.xml` → **Open as Project**.

> Тесттердегі күндер — 2026 жылғы қазан. **9-ы — жұма, 10-ы — сенбі.** Deluxe бағасы осыған байланысты.

---

## Қадам 1 — `DateRange`: жартылай ашық интервал

**Файл:** `model/DateRange.java` · **Тест:** `Step1DateRangeTest`

* Compact конструкторда екі тексеріс бар: `null` болмауы керек және `checkOut` `checkIn`-нен кейін болуы керек.
* `nights()` → `ChronoUnit.DAYS.between(...)`; `nightDates()` → `datesUntil(...)`.
* `overlaps` бір жолмен жазылады: `a.start < b.end && b.start < a.end`.

**Назар аудар:** `java.time` объектілерін `<`, `>` арқылы салыстыруға болмайды, тек `isBefore`, `isAfter` арқылы салыстырасың.
«Кіші немесе тең» жазу үшін `!isAfter(...)` қолданасың.

**Өзіңе сұрақ:** «қиылысады» дегенді тікелей жазу қиын (төрт жағдай бар). Ал «қиылыспайды» дегенді жазу оңай.
Сол жағдайды терістесең (де Морган ережесі), не шығады? Неге `[10, 13)` пен `[13, 15)` қиылыспайды?

## Қадам 2 — `RoomType` және exception-дар

**Файлдар:** `room/RoomType.java`, `exception/*.java` · **Тест:** `Step2TypesTest`

* `RoomType` банктегі `TransactionType` сияқты, бірақ өрісі екеу. Конструкторы `long` қабылдайды, ал өрісте `Money` сақталады.
* Exception-дар банктегідей жазылады.

**Назар аудар:** enum конструкторы класс жүктелгенде **әр константа үшін бір рет** шақырылады.
Оны жазбай тұрып, `RoomType`-қа тиетін кез келген тест (`Room` де, `Booking` де) `ExceptionInInitializerError` береді.
Банктегі `Money.ZERO` сияқты жағдай.

## Қадам 3 — Singleton: `HotelPolicy`

**Файл:** `policy/HotelPolicy.java` · **Тест:** `Step3HotelPolicyTest`

* Үш бөлігі бар: `private static final INSTANCE`, `private` конструктор және `getInstance()`.
* `refundFor` — болдырмау ережесі, ал `isWeekend` — демалыс түнін анықтау.

**Назар аудар:** тест конструктордың `private` екенін **reflection** арқылы тексереді
(`getDeclaredConstructors()`, `Modifier.isPrivate`). Фреймворктер (Spring, JUnit) кластарды осылай «көреді».

**Өзіңе сұрақ:**
1. `HotelPolicy` неге қауіпсіз singleton? Егер оған `private int bookingsCount` қосып, `HotelService` оны арттырса,
   тесттер неге бір-біріне кедергі келтіре бастайды?
2. Spring-те әр `@Service` әдепкі бойынша singleton. Онда неге «Singleton — антипаттерн» дейді?
   (Кеңес: кім объектіні жасайды және кім оны ауыстыра алады — `getInstance()` ме, әлде DI контейнері ме?)

## Қадам 4 — `Room` иерархиясы және `RoomFactory`

**Файлдар:** `room/Room.java`, `StandardRoom.java`, `DeluxeRoom.java`, `Suite.java`, `RoomFactory.java` · **Тест:** `Step4RoomsTest`

* `Room.priceFor` барлық түннің `priceForNight` бағасын қосады (`reduce`, банктегі `totalBalance` сияқты).
* `DeluxeRoom` демалыс түндеріне үстеме қосады, ал `Suite` `priceFor`-ды override етіп, `super.priceFor`-дың нәтижесіне жеңілдік береді.
* `RoomFactory.create` ішінде enum бойынша `switch` expression жазылады, `default` қажет емес.

**Назар аудар:** `create(String, String)`-тағы **тұзақ** Javadoc-та сипатталған. `catch (IllegalArgumentException e)` қатенің
бір түрін ұстаймын деп, басқасын да жұтып қоюы мүмкін. Тест мұны тексереді.

**Өзіңе сұрақ:** банкте `FixedFeeAccount extends Account` жасаған жоқпыз, ал мұнда `DeluxeRoom extends Room` жасадық. Неге?
Бөлме түрі өмір бойы өзгере ме? Ал шоттың тарифі ше? «Демалыста қымбат», «ұзақ тұрсаң арзан» деген ережелерді
**комбинациялау** қажет болса (мысалы, Deluxe-қа да ұзақ мерзімге жеңілдік керек болса), қай тәсіл ыңғайлы болар еді?

## Қадам 5 — `Booking`

**Файл:** `model/Booking.java` · **Тест:** `Step5BookingTest`

* Конструктор тексерісті Javadoc-тағы ретпен жүргізеді, `total` бір рет есептеледі.
* `blocks(other)`: бронь белсенді болса **және** кезеңдері қиылысса, бөлмені бөгейді.
* `cancel(today)`: ережені `HotelPolicy`-ден алады, ал екінші рет болдырмау әрекеті `HotelException` береді.
* `revenue()` = `total - refund`, `if` қажет емес.

**Өзіңе сұрақ:** `cancel` неге `LocalDate.now()`-ды өзі шақырмай, `today`-ді параметр ретінде алады?
(Кеңес: `Account.deposit(amount, at)` қалай жазылғанын еске ал.)

## Қадам 6 — `HotelService`: операциялар

**Файл:** `service/HotelService.java` (6-қадам бөлімі) · **Тест:** `Step6ServiceTest`

* `addRoom` бөлмені тек `RoomFactory` арқылы жасайды, ал `book` тексерістерді Javadoc-тағы ретпен жүргізеді.
* `findAvailable` бөлмелерді **осы кезеңнің** бағасы бойынша сұрыптайды, базалық бағасы бойынша емес.
  Тест мұны 7 түндік кезеңмен тексереді: Deluxe базалық бағасы бойынша арзан, бірақ ұзақ мерзімде Suite-пен салыстыр.
* Бүгінгі күнді тек `LocalDate.now(clock)` арқылы ал.

**Назар аудар:** сервис бөлменің нақты класын ешқашан тексермейді (`instanceof Suite` деген жоқ).
Бағаны `room.priceFor(stay)` арқылы алады, ал қайсысы орындалатынын полиморфизм шешеді.

## Қадам 7 — Есептер

**Файл:** `service/HotelService.java` (7-қадам бөлімі) · **Тест:** `Step7ReportsTest`

* `bookingsOf` атты `equalsIgnoreCase` арқылы салыстырады және екі деңгейлі `Comparator` қолданады.
* `occupancyPercent` пайызды бүтін санмен береді. Бөлме жоқ болса, нөлге бөлу болмауы керек.
* `revenueByType` бөлігін банктегі `totalsByType`-ты еске алып жаз.
* `checkInsOn` жай `filter` + `sorted` арқылы жазылады.

## Қадам 8 — Іске қос

`ConsoleApp`-та демо-деректер бар: 101, 102 (Standard), 201 (Deluxe), 301 (Suite) бөлмелері.
Ертеңнен бастап 101-ді Aru, ал арғы күннен бастап 201-ді Daniyar брондаған.
Мынаны байқап көр: 201-ді Daniyar-дың кезеңіне брондап көр (бос емес); жұмадан бастап 2 түнге іздеп көр
(Deluxe қымбаттайды); Suite-ті 7 түнге брондап, бағасын қара; B0001-ді болдырмай, қанша қайтатынын қара.

---

## Қосымша (міндетті емес, тест жоқ)

1. **Маусым бағасы:** `SeasonalPricing` — жазда (маусым–тамыз) барлық бөлме +30%. Мұны қайда қосасың:
   әр ұрпаққа ма, `Room.priceFor`-ға ма, әлде жаңа Strategy ретінде ме? Қай нұсқада ең аз файлға тиесің?
2. **FamilyRoom:** жаңа түр (5 қонақ, 45 000 ₸, балалар тегін). Қанша файлға тидің? `HotelService` пен `ConsoleApp` өзгерді ме?
3. **Кешігіп кету (late checkout):** кету күні сағат 12:00-ден кейін кетсе, жарты түннің ақысы алынады. `DateRange` енді
   `LocalDate` жеткіліксіз — `LocalDateTime` және `Duration` қажет. Интервал логикасы өзгере ме?
4. **Enum singleton:** `HotelPolicy`-ді `enum HotelPolicy { INSTANCE; ... }` етіп қайта жаз. Тесттегі қай тексеріс
   өзгеруі керек? Reflection арқылы екінші дана жасап көр. Кәдімгі класта бұл мүмкін бе, ал enum-да ше?
