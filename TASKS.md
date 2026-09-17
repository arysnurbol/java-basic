# Глава 2 — ООП практикасы: 16 тапсырма

Әр тапсырманың жанында кітаптағы бет нөмірі тұр. Тексеру: `.\check.ps1 <нөмір>`

| # | Тақырып | Кітап | Пакет |
|---|---------|-------|-------|
| 01 | Класс және объект | 69–74 | `t01_class_object` |
| 02 | Статикалық мүшелер | 75–76 | `t02_static` |
| 03 | Конструктор түрлері | 77–79 | `t03_constructors` |
| 04 | Конструкторларды тізбектеу | 78–80 | `t04_chaining` |
| 05 | Методтарды жүктеу (overloading) | 84 | `t05_overloading` |
| 06 | Параметрлерді беру | 85–86 | `t06_parameters` |
| 07 | Varargs | 86 | `t07_varargs` |
| 08 | Мұрагерлік және `super` | 87–91 | `t08_inheritance` |
| 09 | `Object` методтары, көпдеңгейлі мұрагерлік | 90–93 | `t09_object_methods` |
| 10 | Полиморфизм | 93–96 | `t10_polymorphism` |
| 11 | `instanceof`, ковариантты типтер | 97–98 | `t11_instanceof` |
| 12 | Абстрактілі класс | 99–100 | `t12_abstract` |
| 13 | Интерфейс | 101–103 | `t13_interface` |
| 14 | Инкапсуляция | 104–107 | `t14_encapsulation` |
| 15 | Композиция vs мұрагерлік | 108–109 | `t15_composition` |
| 16 | Singleton және Factory | 110–112 | `t16_patterns` |

---

## Тапсырма 01 — Класс және объект (`Book`)

**Мақсат:** өріс, конструктор, метод — класстың үш негізгі бөлігін қолмен жазып шығу.

`Book` класын толтыр:

* үш **private** өріс: `title` (String), `author` (String), `year` (int);
* үш аргументті конструктор (`this.` арқылы меншікте);
* `describe()` → `"Java (Bachina, 2027)"` пішімі;
* `isOlderThan(int otherYear)` → кітап сол жылдан **бұрын** шықса `true`
  (дәл сол жыл болса — `false`);
* `ageIn(int currentYear)` → жасы.

**Неге private:** өрісті сыртқа ашсаң, кез келген код оны бақылаусыз өзгерте алады.
Тексеріс өрістердің private екенін де қарайды.

---

## Тапсырма 02 — Статикалық мүшелер (`Student`)

**Мақсат:** `static` өріс класқа тиесілі, ал қарапайым өріс — объектіге тиесілі
екенін іс жүзінде көру.

`Student` класын толтыр:

* `private static int count` — жасалған студенттердің **ортақ** санағышы;
* `private final int id` — әр объектінің **өз** нөмірі;
* конструктор `count`-ты өсіріп, жаңа мәнді `id`-ге береді
  (бірінші студент → 1, екінші → 2, …);
* `getId()`, `getName()`, `static getCount()`, `static resetCount()`.

**Өзіңе сұрақ:** `getCount()` неге `static`? Ол бірде-бір объект жасалмай тұрып та
шақырыла алуы керек пе?

---

## Тапсырма 03 — Конструктор түрлері (`Rectangle`)

**Мақсат:** бір класста бірнеше конструктор болатынын және олардың параметрлері
бойынша ажыратылатынын меңгеру.

`Rectangle` класына төрт конструктор жаз:

| Конструктор | Нәтиже |
|---|---|
| `Rectangle()` | 1 × 1 |
| `Rectangle(double side)` | шаршы: side × side |
| `Rectangle(double w, double h)` | w × h |
| `Rectangle(Rectangle other)` | **көшірме конструкторы** — басқа объектінің көшірмесі |

Плюс `getWidth()`, `getHeight()`, `area()`, `perimeter()`.

Көшірме конструкторы **жаңа** объект жасауы керек — сол сілтемені қайтармауы керек.

---

## Тапсырма 04 — Конструкторларды тізбектеу (`Pizza`)

**Мақсат:** `this(...)` арқылы бір конструктордан екіншісін шақыру және
инициализация логикасын бір жерде ұстау.

`Pizza` класында 4 конструктор бар. Ең толығы ғана өрістерді меншіктеуі керек,
қалғандары `this(...)` арқылы соған жүгінеді:

```
Pizza()                          -> this("M", true, false)
Pizza(size)                      -> this(size, true, false)
Pizza(size, cheese)              -> this(size, cheese, false)
Pizza(size, cheese, pepperoni)   <- меншіктеу ТЕК осында
```

`toString()` → `"Pizza[size=M, cheese=true, pepperoni=false]"`.

**Ұстап қалатын тұзақ:** `createdCount++` жолын ТЕК негізгі конструкторға қой.
Егер оны әр конструкторға қойсаң, тізбектеу салдарынан санағыш артық өседі —
тексеріс дәл соны ұстайды.

---

## Тапсырма 05 — Методтарды жүктеу (`MathUtils.max`)

**Мақсат:** overloading — бір ат, әртүрлі параметрлер (компиляция кезіндегі
полиморфизм).

Бес нұсқа жаз: `max(int,int)`, `max(double,double)`, `max(int,int,int)`,
`max(int[])`, `max(String,String)`.

* `max(int,int,int)` — өзің жазған `max(int,int)`-ті қайта қолдансын, логиканы көшірме;
* `max(int[])` — массив `null` немесе бос болса `IllegalArgumentException` лақтыр;
* `max(String,String)` — **ұзынырағын** қайтар, ұзындығы тең болса — бірінші аргументті.

---

## Тапсырма 06 — Параметрлерді беру (`ParamLab`)

**Мақсат:** «Java-да бәрі мән бойынша беріледі» дегеннің нақты мағынасын түсіну.

**Кодты жазбас бұрын** әр методтың javadoc-ындағы «Болжам: …» жолын өз жауабыңмен
толтыр. Содан кейін ғана кодты жазып, тексерісті жүгірт — болжамың дұрыс па?

Іске асыратын методтар: `increment(int)`, `incrementInside(Counter)`,
`reassign(Counter)`, `doubleAll(int[])`, `replaceArray(int[])`, `doubledCopy(int[])`.

**Негізгі айырма:** объектінің *ішін* өзгерту шақырушыға көрінеді,
ал *параметрдің өзіне* жаңа объект меншіктеу — көрінбейді.

---

## Тапсырма 07 — Varargs (`TextUtils`)

**Мақсат:** `int... numbers` синтаксисі және оның іс жүзінде массив екенін білу.

* `sum(int...)` — аргументсіз шақырса 0;
* `join(String separator, String... parts)` — `join("-", "a","b","c")` → `"a-b-c"`,
  бөлік болмаса → `""`;
* `average(double first, double... rest)` — **кемінде бір сан міндетті** болу үшін
  бірінші параметр бөлек тұр;
* `longest(String...)` — ештеңе берілмесе `null`.

**Ереже:** varargs параметрі әрқашан ең соңғы болуы керек, әрі методта біреу ғана.

---

## Тапсырма 08 — Мұрагерлік және `super` (`Employee` / `Manager`)

**Мақсат:** `extends`, `super(...)` конструкторы, `super.method()` шақыруы.

* `Employee` — `getMonthlySalary()` оклад қайтарады, `getInfo()` → `"Employee: Aisha"`;
* `Manager extends Employee` — `name`/`baseSalary`-ді **қайта жарияламай**,
  `super(...)` арқылы береді; `getMonthlySalary()` → `super.getMonthlySalary() + bonus`;
  `getInfo()` → `"Manager: Bolat"`.

Тексеріс `Manager`-де жалғыз ғана жаңа өріс (`bonus`) бар екенін қарайды — ата-класстың
өрістерін көшірме.

---

## Тапсырма 09 — `Object` методтары (`Animal` → `Dog` → `Puppy`)

**Мақсат:** әр класс `Object`-тен мұра алатынын және `toString`/`equals`/`hashCode`-ты
қалай дұрыс қайта жазуды меңгеру.

`Animal`:

* `toString()` → `"Animal(Rex)"`, бірақ `Dog` үшін автоматты түрде `"Dog(Rex)"` болуы
  керек. Класс атын қолмен жазба — `getClass().getSimpleName()` қолдан;
* `equals(Object)` → **нақты типі бірдей ЖӘНЕ аты бірдей** болса `true`.
  `null`-мен және бөгде типпен салыстыруды ұмытпа;
* `hashCode()` → `name` негізінде.

`Dog`: `speak()` → `"Woof"`, `fetch()` → `"Rex доптың артынан жүгірді"`.

`Puppy extends Dog`: `speak()` → `"Yip"`, `speakWithParent()` → `"Yip (әкесі: Woof)"`
(`super.speak()` арқылы). `fetch()`-ті `Puppy`-де **қайта жазба** — ол Dog-тан мұраға келеді.

**Ереже:** `equals()` қайта жазылса, `hashCode()` да қайта жазылуы керек.

---

## Тапсырма 10 — Полиморфизм (`Shape`)

**Мақсат:** бір сілтеме типі арқылы әртүрлі ұрпақтармен жұмыс істеу.

`Circle`, `Square`, `Triangle` — үшеуі де `area()`-ды қайта жазады.
`Shape.describe()` — базалық класста **бір рет** жазылады және `area()`-ды шақырады;
ұрпақтарда `describe()`-ті қайта жазуға болмайды (тексеріс қарайды).

`ShapeUtils`:

* `totalArea(Shape[])` — жалпы аудан (`null` → 0);
* `largest(Shape[])` — ауданы ең үлкені (бос/`null` → `null`).

**Ең маңыздысы:** бұл екі методта бірде-бір `if (shape instanceof Circle)` болмауы керек.
Полиморфизмнің бүкіл мәні — нақты типті білмей-ақ жұмыс істеу.

`describe()` ішінде `String.format(java.util.Locale.ROOT, "%s: %.2f", …)` қолдан —
`Locale.ROOT` болмаса, Windows-тың орысша локалінде нүктенің орнына үтір шығады.

---

## Тапсырма 11 — `instanceof` және ковариантты типтер

**Мақсат:** полиморфизм жетпеген жерде нақты типті қауіпсіз тексеру.

`NotificationRouter.route(Notification)`:

```
EmailNotification -> "EMAIL -> a@b.kz: Сәлем"
SmsNotification   -> "SMS -> +77010000000: Сәлем"
басқасы / null    -> "UNKNOWN"
```

Java 16-дан бергі pattern matching түрін қолдан — бөлек cast жазудың қажеті жоқ:

```java
if (n instanceof EmailNotification email) {
    ... email.getAddress() ...
}
```

Плюс `countEmails(Notification[])`.

**Ковариантты қайтару типі:** `Notification.copy()` → `Notification` қайтарады,
ал `EmailNotification.copy()` → `EmailNotification` қайтарады. Соның арқасында
шақырған жақта `EmailNotification c = email.copy();` деп cast-сыз жазуға болады.

---

## Тапсырма 12 — Абстрактілі класс (`Vehicle`)

**Мақсат:** ортақ логиканы базалық класта қалдырып, әр ұрпаққа тән бөлікті
`abstract` метод ретінде беру.

* `fuelCostPer100Km()` — **abstract**, денесі жоқ;
* `tripCost(double km)` — abstract ЕМЕС: `km / 100 * fuelCostPer100Km()`;
* `describe()` → `"Toyota Camry: 100 км = 8000 ₸"` (`Math.round` арқылы).

`Car(model, litersPer100Km, pricePerLiter)` → `fuelCostPer100Km()` = литр × баға.
`Bicycle` → 0.

**Байқа:** `tripCost()` әлі жазылмаған методты шақырады. Бұл — абстракцияның мәні:
базалық класс *не* болатынын біледі, *қалай* болатынын ұрпақ шешеді.

---

## Тапсырма 13 — Интерфейс (`Payable`)

**Мақсат:** интерфейстің төрт түрлі мүшесін бір жерде көру: тұрақты, abstract метод,
`default` метод, `static` метод.

* `DEFAULT_TAX_RATE = 0.10` — интерфейстегі өріс автоматты түрде `public static final`;
* `grossPay()` — abstract;
* `default netPay()` → `grossPay() * (1 - DEFAULT_TAX_RATE)`;
* `static currency()` → `"KZT"`.

`FullTimeEmployee` — `netPay()`-ді **қайта жазбайды** (default жеткілікті).
`Contractor` — салығы 20%, сондықтан `netPay()`-ді **қайта жазады**.

`PayrollUtils.totalNetPay(Payable[])` — екеуін де бірдей өңдейді.

**Өзіңе сұрақ:** мұнда неге абстрактілі класс емес, интерфейс? (Кеңес: бір класс
бір ғана класстан мұра ала алады, ал интерфейсті қалағанынша іске асыра алады.)

---

## Тапсырма 14 — Инкапсуляция (`BankAccount`, `Temperature`)

**Мақсат:** деректі жасырып, оны өзгертудің ережелерін кластың өзіне жүктеу.

`BankAccount` — өрістері private, **`setBalance()` жоқ**:

| Жағдай | Нәтиже |
|---|---|
| `owner` null/бос немесе `initialBalance < 0` | `IllegalArgumentException` |
| `deposit(amount)`, amount ≤ 0 | `IllegalArgumentException` |
| `withdraw(amount)`, amount ≤ 0 | `IllegalArgumentException` |
| `withdraw(amount)`, amount > balance | `IllegalStateException` |

Қате лақтырылған жағдайда баланс **өзгермеуі** керек.

`Temperature` — тек оқуға арналған (immutable) класс: барлық өрісі `final`,
бірде-бір setter жоқ. `plus(double delta)` объектіні өзгертпей, **жаңа**
`Temperature` қайтарады. `getFahrenheit()` → `c * 9 / 5 + 32`.

**Неге екі түрлі exception:** «сен дұрыс емес аргумент бердің» —
`IllegalArgumentException`, «объектінің қазіргі күйінде бұл операция мүмкін емес» —
`IllegalStateException`.

---

## Тапсырма 15 — Композиция vs мұрагерлік (`Car` + `Engine`)

**Мақсат:** «has-a» мен «is-a» айырмасын сезіну.

`Car extends Engine` деп жазу **қате** болар еді: көлік — қозғалтқыш емес, оның
*ішінде* қозғалтқыш бар. Сондықтан `Car` ішінде `Engine` өрісі болады да,
`Car.start()` жай ғана `engine.start()`-қа тапсырма береді (delegation).

* `Engine.start()` → `"V6 іске қосылды"`, `stop()` → `"V6 тоқтады"`, күйін жаңартады;
* `Car.getSpec()` → `"Toyota Camry [V6, 300 а.к.]"`;
* `Car.withEngine(Engine)` → қозғалтқышы ауыстырылған **жаңа** `Car`.

**Композицияның ұтысы:** `withEngine()`-ді мұрагерлікпен жасай алмас едің —
объектінің ата-класын орындалу кезінде ауыстыру мүмкін емес.

---

## Тапсырма 16 — Singleton және Factory

**Мақсат:** ең жиі кездесетін екі шаблонды өз қолыңмен жазу.

**Singleton — `AppConfig`:** бүкіл қолданбада бір ғана дана.

```java
private static AppConfig instance;   // жалғыз дана осында сақталады
private AppConfig() { }              // сырттан new жасауға болмайды
public static AppConfig getInstance() { ... }
```

Тексеріс конструктордың private екенін және екі шақыру да **дәл сол бір** объектіні
қайтаратынын қарайды.

**Factory — `NotifierFactory.create(String type)`:** шақырушы жақ нақты класты
білмейді, тек `"email"` немесе `"sms"` деп сұрайды. Регистр маңызды емес
(`"EMAIL"` да жүруі керек). Белгісіз тип немесе `null` → `IllegalArgumentException`.

**Өзіңе сұрақ:** Factory `Notifier` интерфейсін қайтарады, нақты классты емес.
Ертең `PushNotifier` қоссаң, шақырушы кодта не өзгереді?

---

## Бәрі өткеннен кейін

Глава 2 меңгерілді деуге болады. Кітап бойынша келесі қадам — 3-глава
«Расширенные возможности Java» (115-бет): коллекциялар, generics, lambda, streams.
