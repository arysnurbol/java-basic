# Жоба 1 — ToDo List (таза Java, ООП)

Консольдік тапсырмалар менеджері. Кішкентай, бірақ «нағыз» қосымшаның құрылымымен:
**model → repository → service → UI**. Дәл осы қабаттар кейін Spring Boot-та да болады.

## ООП-тың қайсысы қай жерде

| Ұғым | Қай жерде |
|---|---|
| Инкапсуляция | `Task` — private өрістер, `setStatus()` жоқ, күй тек ереже арқылы өзгереді |
| Абстракция | `abstract class Task` — `isOverdue()`, `details()` |
| Мұрагерлік | `SimpleTask`, `DeadlineTask` extends `Task` |
| Полиморфизм | `TaskService.findOverdue()` тапсырманың типін білмейді |
| Интерфейс | `TaskRepository` → `InMemoryTaskRepository` |
| Композиция + DI | `TaskService` конструктор арқылы `TaskRepository` алады |
| Template Method | `Task.describe()` (final) + `details()` (ұрпақта) |
| enum өрісімен | `Priority` (дайын үлгі), `Status.canMoveTo()` |
| Өз exception-ың | `InvalidTaskException`, `TaskNotFoundException` |
| `equals/hashCode` | `Task` — тек `id` бойынша |

## Құрылым

```
src/main/java/kz/learn/todo/
├── model/        Priority (дайын), Status, Task, SimpleTask, DeadlineTask
├── exception/    InvalidTaskException, TaskNotFoundException
├── repository/   TaskRepository (дайын интерфейс), InMemoryTaskRepository
├── service/      TaskService
└── ConsoleApp    (дайын консольдік мәзір)
src/test/java/...  Step1…Step6 тесттері — ӨЗГЕРТПЕ, бірақ ОҚЫ
```

Сен тек `// TODO` тұрған жерлерді толтырасың:
`throw new UnsupportedOperationException("TODO");` жолын өшіріп, орнына өз кодыңды жаз.
`// TODO: өрістерді жаз` — сол жерге класс өрістерін жаз.

## Жүгірту

Maven бөлек орнатудың қажеті жоқ — жобада Maven Wrapper (`mvnw`) бар.
Бірінші жүгіртуде Maven пен JUnit жүктеледі (интернет керек).

```powershell
cd projects\01-todo

.\mvnw.cmd test                          # барлық тест
.\mvnw.cmd test "-Dtest=Step1*"          # тек 1-қадам
.\mvnw.cmd test "-Dtest=Step1*,Step2*"   # бірнеше қадам
```

Git Bash: `./mvnw test -Dtest='Step3*'`

IntelliJ: `projects/01-todo/pom.xml` → **Open as Project** (немесе оң батырма → *Add as Maven Project*),
содан кейін тест класының жанындағы жасыл ▶ батырмасы.

Барлығы жасыл болғанда — қосымшаны іске қос:

```powershell
.\mvnw.cmd -q compile exec:java
```

> Консольде қазақ әріптері бұзылса: алдымен `chcp 65001` жаз, немесе IntelliJ-тен `ConsoleApp`-ты жүгірт.

---

## Қадам 1 — `Status.canMoveTo()` (enum ішіндегі логика)

**Файл:** `model/Status.java` · **Тест:** `Step1StatusTest`

Тапсырманың өмірлік циклі:

```
TODO ──start──▶ IN_PROGRESS ──complete──▶ DONE
  ▲                 │
  └────── moveTo ───┘
```

`canMoveTo(next)` осы суреттегі көрсеткі бар болса ғана `true` қайтарсын.
`DONE` — соңғы күй. `null` → `false`.

**Неге enum ішінде:** ереже күйдің өзіне тиесілі. Оны `Task`-қа немесе сервиске жазсаң,
жаңа күй қосқанда бірнеше жерді өзгертуге тура келеді. Алдымен `Priority.java`-ны оқы —
enum-ның өрісі, конструкторы және методы болатынын көресің.

## Қадам 2 — Өз exception-дарың

**Файлдар:** `exception/*.java` · **Тест:** `Step2ExceptionsTest`

* `InvalidTaskException(String message)` — хабарламаны ата-класқа беру (`super(...)`).
* `TaskNotFoundException(long taskId)` — `taskId` өрісін сақта, хабарлама: `"Task not found: id=5"`.

**Өзіңе сұрақ:** неге `RuntimeException`, ал `Exception` емес? (checked vs unchecked —
3-главаны есіңе түсір.) Spring-те exception-дардың көбі неге unchecked?

## Қадам 3 — `Task` абстрактілі класы

**Файл:** `model/Task.java` · **Тест:** `Step3TaskTest`

Ең үлкен қадам. Талаптар Javadoc-та тұр, қысқаша:

* өрістер: `id` (final), `title`, `priority`, `status` — бәрі `private`;
* конструктор тексереді (бос атау, `null` priority → `InvalidTaskException`), `title`-ды `trim()` етеді;
* `moveTo()` ережені `Status.canMoveTo()`-дан сұрайды — өзі `if`-тер жазбайды;
* `start()` / `complete()` — `moveTo()`-ны шақырады, кодты қайталамайды;
* `describe()` — `final`, соңына `details()` қосады;
* `equals/hashCode` — тек `id`.

**Тест қалай жұмыс істейді:** `Task` абстрактілі — оның объектісін жасай алмайсың. Сондықтан тест
ішінде кішкентай `TestTask extends Task` бар. Ашып оқы.

**Өзіңе сұрақ:** `describe()` неге `final`? Ұрпақ оны override етсе не бұзылады?

## Қадам 4 — `SimpleTask`, `DeadlineTask`

**Файлдар:** `model/SimpleTask.java`, `model/DeadlineTask.java` · **Тест:** `Step4TaskTypesTest`

* `SimpleTask` — ешқашан кешікпейді, `details()` → `""`.
* `DeadlineTask` — `dueDate` (`private final`, `null` болмайды);
  кешікті = аяқталмаған **және** `today` > `dueDate`; `details()` → `", due 2026-10-05"`.

**Назар аудар:** `DeadlineTask` `isDone()`-ды ата-кластан қолданады — `status` өрісіне тікелей
қол жеткізе алмайды (private). Бұл — дұрыс.

**Неге `isOverdue(LocalDate today)` параметрмен, `LocalDate.now()` емес:** әйтпесе тест
бүгінгі күнге байланып қалады. Уақытты сырттан беру — тестілеуге болатын код жазудың негізгі тәсілі.

## Қадам 5 — `InMemoryTaskRepository`

**Файл:** `repository/InMemoryTaskRepository.java` · **Тест:** `Step5RepositoryTest`

`TaskRepository` интерфейсін жүзеге асыр. `Map<Long, Task>` қолдан.

* `findAll()` **қосылған ретімен** қайтарады — қай `Map` ретті сақтайды?
* `findAll()` ішкі коллекцияны сыртқа бермесін — көшірме қайтар.
* `findById()` ешқашан `null` қайтармайды — `Optional`.

**Өзіңе сұрақ:** ертең тапсырмаларды файлға сақтағың келсе, `TaskService`-те нені өзгертуге тура келеді?
(Дұрыс жауап: ештеңені — тек жаңа `FileTaskRepository` жазып, `ConsoleApp`-та ауыстырасың.)

## Қадам 6 — `TaskService`

**Файл:** `service/TaskService.java` · **Тест:** `Step6ServiceTest`

* репозиторийді конструктор арқылы ал, `final` өрісте сақта;
* id-ды сервис береді: 1, 2, 3… (өшірілгені қайта берілмейді);
* табылмаған id → `TaskNotFoundException` (`Optional.orElseThrow` ыңғайлы);
* сүзу/сұрыптау — Stream API: `filter`, `sorted`, `Comparator.comparing(...).reversed().thenComparing(...)`.

**Назар аудар:** `findOverdue()` ішінде `instanceof DeadlineTask` **жазба**. Әр тапсырманың
`isOverdue()`-ын шақыр — қалғанын полиморфизм шешеді.

## Қадам 7 — Іске қос

`ConsoleApp`-ты жүгіртіп, барлық мәзір пунктін байқап көр. Әдейі қате енгіз
(бос атау, жоқ id, `DONE` тапсырманы бастау) — қосымша құламай, хабарлама шығаруы керек.

---

## Қосымша (міндетті емес, тест жоқ)

1. **`RecurringTask`** — `complete()` болғанда келесі мерзімге жаңа тапсырма жасалатын тип.
   Қай класқа қандай өзгеріс керек болды? `TaskService`-ті өзгертуге тура келді ме?
2. **`FileTaskRepository`** — тапсырмаларды CSV-файлға сақтау. `ConsoleApp`-та бір жолды ауыстыр.
3. Мәзірге **«Атауын өзгерту»** пунктін қос (`TaskService.rename(id, newTitle)`).
