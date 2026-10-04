# Жоба 2 — Кітапхана жүйесі (таза Java: иерархиялар, generics, Collections)

Консольдік кітапхана: экземплярлар (кітап, журнал, DVD), оқырмандар (студент, кәдімгі),
беру/қайтару, кешіккені үшін айыппұл және есептер. Қабаттар ToDo-дағыдай:
**model → repository → service → UI**.

ToDo-дан не жаңа:

* **екі** мұрагерлік иерархиясы, және олар `Loan`-да бір-бірімен кездеседі;
* бір **generic** репозиторий үш түрлі сущностьқа (`Long` және `String` id-мен);
* **generic методтар** және `? super T` wildcard;
* интерфейстегі **`default` методтар**;
* **exception иерархиясы** (ортақ ата-класс);
* **immutable** класс, **утилита-класс**;
* Collections: `TreeMap`, `groupingBy` + `counting`/`summingLong`, `Map.Entry` сұрыптау.

## ООП-тың қайсысы қай жерде

| Ұғым | Қай жерде |
|---|---|
| Exception иерархиясы | `LibraryException` ← `NotFoundException`, `ItemNotAvailableException`, `LoanLimitExceededException` |
| Утилита-класс | `Check` — `final`, `private` конструктор, тек `static` |
| 1-иерархия, immutable | `Item` ← `Book`, `Magazine`, `Dvd` — барлық өрістер `private final` |
| 2-иерархия, hook-метод | `Member` ← `StudentMember`, `RegularMember` — `maxLoans()` abstract, `applyDiscount()` әдепкімен |
| Generic интерфейс | `Identifiable<ID>`, `Repository<T extends Identifiable<ID>, ID>` |
| `default` метод, `? super T` | `Repository.existsById()`, `Repository.findWhere(Predicate<? super T>)` |
| Generic класс | `InMemoryRepository<T, ID>` — бір класс, үш репозиторий |
| Generic метод | `LibraryService.saveItem()`, `LibraryService.getOrThrow()` |
| Екі иерархия бірге (полиморфизм) | `Loan.fine()` — мерзім мен тарифті `Item`, жеңілдікті `Member` береді |
| Композиция + DI | `LibraryService` конструктор арқылы үш `Repository` алады |
| Collections + Stream | `finesByMember()` (`TreeMap`), `mostPopular()` (`Map<Item, Long>`) |

## Ережелер

| Түрі | Мерзімі | Айыппұл / күн |
|---|---|---|
| `Book` | 14 күн | 50 ₸ |
| `Magazine` | 7 күн | 20 ₸ |
| `Dvd` | 3 күн | 200 ₸ |

| Оқырман | Бір уақытта | Айыппұлға жеңілдік |
|---|---|---|
| `StudentMember` | 3 | 50% (бүтін бөлу) |
| `RegularMember` | 5 | жоқ |

Айыппұл = кешіккен күн × тариф, содан кейін оқырманның жеңілдігі. `dueDate` күнінің өзінде — әлі кешікпеген.

## Құрылым

```
src/main/java/kz/learn/library/
├── exception/   LibraryException, NotFoundException, ItemNotAvailableException, LoanLimitExceededException
├── model/       Identifiable (дайын), Check, Item, Book, Magazine, Dvd,
│                Member, StudentMember, RegularMember, Loan
├── repository/  Repository (келісімшарт дайын, default методтар — сенікі), InMemoryRepository
├── service/     LibraryService
└── ConsoleApp   (дайын консольдік мәзір + демо-деректер)
src/test/java/...  Step1…Step7 тесттері — ӨЗГЕРТПЕ, бірақ ОҚЫ
```

Сен тек `// TODO` тұрған жерлерді толтырасың:
`throw new UnsupportedOperationException("TODO");` жолын өшіріп, орнына өз кодыңды жаз.
`// TODO: өрістерді жаз` — сол жерге класс өрістерін жаз.
`super("TODO");` — дұрыс хабарламаны құрастырып бер.

## Жүгірту

```powershell
cd projects\02-library

.\mvnw.cmd test                          # барлық тест
.\mvnw.cmd test "-Dtest=Step1*"          # тек 1-қадам
.\mvnw.cmd -q compile exec:java          # бәрі жасыл болғанда — қосымша
```

Git Bash: `./mvnw test -Dtest='Step4*'`. IntelliJ: `projects/02-library/pom.xml` → **Open as Project**.

> Бірнеше тест (құрылымды тексеретіндер) басынан-ақ жасыл болуы мүмкін — бұл қалыпты.

---

## Қадам 1 — Exception иерархиясы

**Файлдар:** `exception/*.java` · **Тест:** `Step1ExceptionsTest`

* `LibraryException(String message)` — хабарламаны ата-класқа бер.
* Қалған үшеуі `LibraryException`-нан мұраланады: өрістерін `private final` сақта,
  хабарламаны `super(...)` арқылы құрастыр (пішімі Javadoc-та).

**Неге ортақ ата-класс:** `ConsoleApp` бір `catch (LibraryException e)` арқылы барлық бизнес-қатені
ұстайды, ал бағдарламашы қателерін (`NullPointerException`) ұстамайды. Spring-тегі
`@ExceptionHandler(LibraryException.class)` дәл осылай жұмыс істейді.

**Өзіңе сұрақ:** `NotFoundException`-да `id` өрісінің типі неге `Object`?

## Қадам 2 — `Check` және `Item` иерархиясы

**Файлдар:** `model/Check.java`, `Item.java`, `Book.java`, `Magazine.java`, `Dvd.java` · **Тест:** `Step2ItemsTest`

* `Check.text` / `Check.positive` — `IllegalArgumentException` хабарламада өріс атымен.
* `Item` — **immutable**: барлық өрістер `private final`, сеттер жоқ. `equals/hashCode` — тек `id`.
* Ұрпақтар — мерзім, тариф, `details()`. Өз өрістерін `Check` арқылы тексер.

**Назар аудар:** қате дерек — `IllegalArgumentException`, `LibraryException` емес. Біріншісі —
«шақырушы дұрыс емес аргумент берді», екіншісі — «бизнес-ереже бұзылды».

## Қадам 3 — `Member` иерархиясы

**Файлдар:** `model/Member.java`, `StudentMember.java`, `RegularMember.java` · **Тест:** `Step3MembersTest`

* `getId()` — билет нөмірі (`String`).
* `maxLoans()` — **abstract**: әр түр міндетті түрде жазады.
* `applyDiscount()` — **hook**: `Member`-де әдепкі (жеңілдік жоқ), тек `StudentMember` override етеді.
  `RegularMember`-де оны **жазба** — тест мұны тексереді.

**Өзіңе сұрақ:** abstract метод пен әдепкі денесі бар метод — қайсысын қашан таңдайсың?

## Қадам 4 — Generic репозиторий

**Файлдар:** `repository/Repository.java` (тек `default` методтар), `InMemoryRepository.java` · **Тест:** `Step4RepositoryTest`

* `existsById` — `findById` арқылы; `findWhere` — `findAll()` + `stream().filter(...)`.
* `InMemoryRepository` — ToDo-дағы `InMemoryTaskRepository`-дің generic нұсқасы: `Map<ID, T>`.

**Назар аудар:** `InMemoryRepository` ішінде `Item`, `Member`, `Long` деген сөз **бірде-бір рет** кездеспеуі керек —
ол тек `T` мен `ID`-ды біледі. Тест `Long` және `String` id-мен бірдей класты тексереді.

**Өзіңе сұрақ:** `Predicate<? super T>` орнына `Predicate<T>` жазсаң, тесттегі `Predicate<Object>` жолы неге компиляцияланбайды?

## Қадам 5 — `Loan`: екі иерархия кездеседі

**Файл:** `model/Loan.java` · **Тест:** `Step5LoanTest`

* `dueDate = loanDate + item.loanDays()`; `null` аргумент → `Objects.requireNonNull` (`NullPointerException`).
* `overdueDays(today)` — қайтарылған болса `returnDate` бойынша «қатып қалады». Ешқашан теріс емес.
* `fine(today)` — `overdueDays * item.dailyFine()`, содан `member.applyDiscount(...)`.

**Назар аудар:** `fine()` ішінде бірде-бір `instanceof` жоқ. Жаңа `AudioBook` немесе `TeacherMember`
қосқанда `Loan` бір жолы да өзгермейді.

## Қадам 6 — `LibraryService`: беру және қайтару

**Файл:** `service/LibraryService.java` (6-қадам бөлімі) · **Тест:** `Step6ServiceTest`

* `addBook()` `Book` қайтарады (`Item` емес) — оған generic `saveItem()` көмектеседі.
* `getOrThrow()` — **бір** generic метод, `getItem` те, `getMember` те соны қолданады.
* `lend()` — тексеріс реті Javadoc-та; `returnItem()` айыппұлды қайтарады.
* id есептегіштері: экземплярларға және `Loan`-ға **бөлек**.

**Өзіңе сұрақ:** «Экземпляр қолда ма?» деген ақпаратты `Item`-ге `boolean onLoan` өрісі ретінде
қоспадық. Неге? (Кеңес: `Item` immutable; ақиқат — `Loan`-дарда.)

## Қадам 7 — Есептер: Collections + Stream

**Файл:** `service/LibraryService.java` (7-қадам бөлімі) · **Тест:** `Step7ReportsTest`

* `availableItems`, `search` — `findWhere` арқылы.
* `overdueLoans` — `Comparator.comparingLong(...).reversed().thenComparing(...)`.
* `finesByMember` — `groupingBy(..., TreeMap::new, summingLong(...))`.
* `mostPopular` — `groupingBy(Loan::getItem, counting())` → `Map<Item, Long>`, сосын `entrySet()`-ті сұрыпта.

**Назар аудар:** `Map<Item, Long>` дұрыс жұмыс істеуі — 2-қадамдағы `equals/hashCode`-тың арқасында.

## Қадам 8 — Іске қос

`ConsoleApp`-та демо-деректер бар (`S-1`, `R-1`, 4 экземпляр). Байқап көр: DVD-ны бүгін бер,
`Кешіккендер` пунктінде 10 күн кейінгі күнді енгіз; студентке 4-ші кітапты беріп көр;
бір экземплярды екі рет бер.

---

## Қосымша (міндетті емес, тест жоқ)

1. **`TeacherMember`** — 10 экземпляр, айыппұл жоқ. Қанша класс өзгерді?
2. **Айыппұлдың шегі:** айыппұл экземплярдың «құнынан» аспасын (`Item`-ге `abstract long price()`).
3. **Бұғаттау:** кешіктірілген экземпляры бар оқырманға жаңасын бермеу (`lend()`-қа 5-тексеріс және жаңа exception).
4. **`ReportService`** — 7-қадам методтарын бөлек класқа шығар. `LibraryService` жеңілдеді ме?
