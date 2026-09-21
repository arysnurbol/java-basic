# Глава 3 — Java-ның кеңейтілген мүмкіндіктері: 20 тапсырма

Бхаргав Бачина, «Java на практике», 3-глава «Расширенные возможности Java» (115–160 бет).

Тексеру: `.\check.ps1 ch3 <нөмір>` (Git Bash: `./check.sh ch3 <нөмір>`)

| # | Тақырып | Кітап | Пакет |
|---|---------|-------|-------|
| 01 | Обобщенный класс (`Box<T>`, `Pair<K,V>`) | 116–117 | `t01_generic_class` |
| 02 | Обобщенные методы | 117–118 | `t02_generic_methods` |
| 03 | Ограниченные типы және wildcard-тар | 118–119 | `t03_bounded_wildcards` |
| 04 | Обобщенный интерфейс, стирание типов | 119–120 | `t04_generic_interface` |
| 05 | Лямбда-выражения | 121–122 | `t05_lambda_basics` |
| 06 | Функционалдық интерфейстер | 123 | `t06_functional_interfaces` |
| 07 | Ссылки на методы | 124 | `t07_method_refs` |
| 08 | Коллекциялар: List, Set | 125–129 | `t08_list_set` |
| 09 | Map | 126–129 | `t09_map` |
| 10 | Queue, Deque, PriorityQueue | 128–129 | `t10_queue_deque` |
| 11 | Stream API: негізгі операциялар | 131–134 | `t11_stream_ops` |
| 12 | Коллекторлар | 135 | `t12_collectors` |
| 13 | Примитивті, жалқау, параллель ағындар | 132, 136 | `t13_streams_advanced` |
| 14 | Ағындар: құру, іске қосу, күту | 137–139 | `t14_threads` |
| 15 | Синхронизация және race condition | 139–141 | `t15_synchronization` |
| 16 | Executors, Callable, Future | 141–142 | `t16_executors` |
| 17 | Рефлексия | 143–146 | `t17_reflection` |
| 18 | Пайдаланушы аннотациялары | 146–148 | `t18_annotations` |
| 19 | Файлдармен жұмыс (java.io, java.nio) | 149–154 | `t19_file_io` |
| 20 | Ерекше жағдайлар | 155–157 | `t20_exceptions` |

Барлығы **366 тексеріс**. Реті бойынша істе — кейінгілері алдыңғыларға сүйенеді.

---

## Тапсырма 01 — Обобщенный класс (`Box`, `Pair`)

**Мақсат:** тип параметрі `<T>` дегеннің не екенін және ол cast-тан қалай құтқаратынын көру.

* `Box<T>` — бір мән сақтайтын жәшік: екі конструктор, `of()` статикалық фабрикасы,
  `get`/`set`/`isEmpty`/`getOrDefault`/`describe`;
* `Pair<K, V>` — өзгермейтін жұп, `swap()` методы **типтердің орнын да ауыстырады**
  (`Pair<K,V>` → `Pair<V,K>`).

**Байқа:** `static <T> Box<T> of(T value)` дегендегі бірінші `<T>` — методтың өз параметрі.
Статикалық метод класстың `T`-сын көре алмайды, сондықтан өзінікін жариялауы керек.

**Тексеріс сонымен бірге:** `Box<String>` мен `Box<Integer>` орындалу кезінде **бір ғана класс**
екенін көрсетеді — бұл *стирание типов*.

---

## Тапсырма 02 — Обобщенные методы (`ArrayUtils`)

**Мақсат:** класс генерик болмаса да, жеке метод генерик бола алатынын меңгеру.

`join`, `firstOrDefault`, `swap`, `countEquals`, `toList`, `sameElements`.

* `countEquals` `null`-мен де жұмыс істеуі керек → `==` емес, `Objects.equals`;
* `toList` нәтижесіне `add` жасауға болуы керек → `Arrays.asList` **жарамайды**
  (ол тіркелген өлшемді тізім қайтарады).

---

## Тапсырма 03 — Ограниченные типы және wildcard-тар (`Bounds`)

**Мақсат:** `extends`/`super` шектеулерінің қашан керек екенін іс жүзінде түсіну.

| Қолтаңба | Мағынасы |
|---|---|
| `<T extends Comparable<T>>` | ішінде `compareTo` жазуға рұқсат |
| `List<? extends Number>` | **оқуға** болады, жазуға болмайды |
| `List<? super Integer>` | **жазуға** болады, оқығанда `Object` шығады |
| `<T extends Number & Comparable<T>>` | екі шектеу қатар |

**PECS:** Producer Extends, Consumer Super.

**Міндетті тәжірибе:** `sumOf`-тың қолтаңбасын `List<Number>` қылып өзгертіп, жүгіртіп көр.
Компиляция құлайды — `List<Integer>` `List<Number>`-дің **ұрпағы емес**. Сол қатені оқы,
сосын кері қайтар. Осы тәжірибесіз wildcard түсініксіз болып қалады.

---

## Тапсырма 04 — Обобщенный интерфейс (`Repository<T, ID>`)

**Мақсат:** екі тип параметрі бар интерфейсті іске асыру (Spring Data-ның шағын үлгісі).

`InMemoryRepository`-де `LinkedHashMap<ID, T>` ұста — сонда `findAll()` қосылу ретін сақтайды.

**Стирание типов:** тексеріс `InMemoryRepository<String,Integer>` мен
`InMemoryRepository<Integer,String>`-тің `getClass()` нәтижесі **бірдей** екенін көрсетеді.
Сондықтан `new T()` жазу да, `x instanceof T` тексеру де мүмкін емес.

---

## Тапсырма 05 — Лямбда-выражения (`LambdaBasics`)

**Мақсат:** анонимді класстан лямбдаға өту.

Файлда `LEGACY_BY_LENGTH` — Java 8-ге дейінгі 5 жолдық анонимді класс. Оны **өзгертпе**:
сен жазатын `byLength()` дәл сол логиканы бір жолға сыйдыруы керек. Тексеріс екеуінің
нәтижесін салыстырады.

Одан әрі: `byLengthThenAlpha()` (`comparingInt(...).thenComparing(...)`),
`sortedByLength` / `sortedDescending` (**жаңа** тізім), `sortInPlaceByLength` (тізімнің **өзін**),
және `greeter(StringBuilder, String)` — лямбданың сыртқы айнымалыны ұстауы (closure).

**Ереже:** лямбда ұстайтын жергілікті айнымалы *effectively final* болуы керек.
Сондықтан санақ жүргізу үшін `int` емес, `StringBuilder`/`AtomicInteger` ұсталады.

---

## Тапсырма 06 — Функционалдық интерфейстер (`Transformer`, `FunctionalLab`)

**Мақсат:** `java.util.function`-дағы бестікті еркін қолдану.

```
Predicate<T>       T -> boolean      .and() .or() .negate()
Function<T,R>      T -> R            .andThen() .compose()
Supplier<T>        () -> T
Consumer<T>        T -> ()
BiFunction<T,U,R>  T,U -> R
```

Плюс өзіңнің `@FunctionalInterface Transformer` интерфейсің: `default andThen(...)` және
`static identity()` методтарын жаз.

**Шатастырма:** `f.andThen(g)` — алдымен `f`; `f.compose(g)` — алдымен `g`.
Тексерісте `trim` мен `exclaim`-ді екі ретпен қолданып, нәтиже шынымен әр түрлі екенін көресің.

---

## Тапсырма 07 — Ссылки на методы (`MethodRefLab`)

**Мақсат:** төрт түрін де ажырату.

| Түрі | Мысал | Қандай интерфейске |
|---|---|---|
| статикалық метод | `Integer::parseInt` | `Function<String,Integer>` |
| **нақты** объектінің методы | `text::length` | `Supplier<Integer>` |
| **кез келген** объектінің методы | `String::toUpperCase` | `Function<String,String>` |
| конструктор | `ArrayList::new` | `Supplier<List<String>>` |

**Басты шатасу:** `text::length` объектіні **ұстап қалады** (параметр қалмайды),
ал `String::length` объектіні **параметрге айналдырады**.

Әр методты алдымен толық лямбдамен жаз, сосын қысқарт — айырмасын сонда сезесің.

---

## Тапсырма 08 — List және Set (`CollectionLab`)

**Мақсат:** қай реализацияны қашан алу керегін білу.

`ArrayList` / `LinkedHashSet` / `TreeSet`, `List.copyOf`, `retainAll`, `removeAll`.

**Басты тұзақ — `removeShorterThan`:** for-each ішінде `list.remove()` шақырсаң
`ConcurrentModificationException` аласың. Дұрысы: `Iterator.remove()` немесе `removeIf()`.
Екеуін де жазып көр — бұл сұхбатта жиі сұралады.

**Екінші тұзақ:** `List.of(...)` — **өзгермейтін** тізім. Оған `add` жасасаң
`UnsupportedOperationException`. Тексеріс соны арнайы тексереді.

---

## Тапсырма 09 — Map (`MapLab`)

**Мақсат:** `null`-ді тексеретін `if`-тердің орнына Map-тың дайын методтарын қолдану.

```java
map.getOrDefault(key, 0)
map.putIfAbsent(key, value)
map.computeIfAbsent(key, k -> new ArrayList<>())
map.merge(key, 1, Integer::sum)
```

Тапсырмалар: `wordCount` (**`merge` арқылы жаз**), `groupByFirstLetter`
(**`computeIfAbsent`**), `describeSorted`, `totalOf`, `keyWithMaxValue`, `invert`.

`HashMap` реті **жоқ** — сұрыпталған нәтиже керек болса `TreeMap` ал.

---

## Тапсырма 10 — Queue, Deque, PriorityQueue (`QueueLab`)

**Мақсат:** кезек пен стектің айырмасы, әрі `PriorityQueue`-дің мінезі.

|  | exception лақтырады | арнайы мән қайтарады |
|---|---|---|
| қосу | `add(e)` | `offer(e)` → `false` |
| алу | `remove()` | `poll()` → `null` |
| қарау | `element()` | `peek()` → `null` |

**`PriorityQueue` туралы:** `toString()` сұрыпталған **көрінбейді** — тек `poll()` реті дұрыс.
Сондықтан оны әрқашан босатып (drain) тексер.

Соңғы екі тапсырма — `Deque`-ті **стек** ретінде қолдану: жолды аудару және
жақшалардың теңгерімін тексеру. Ескі `Stack` класын қолданба.

---

## Тапсырма 11 — Stream API: негізгі операциялар (`StreamOps`)

**Мақсат:** құбырдың үш бөлігін еркін құрастыру.

```
дерек көзі  ->  аралық операциялар  ->  терминалдық операция
list.stream()   .filter().map()         .toList()
```

`filter`, `map`, `sorted`, `distinct`, `limit`, `skip` + `toList`, `count`, `reduce`,
`min`/`max`, `anyMatch`/`allMatch`, `findFirst`.

**Екі ереже:**
1. Терминалдық операциясыз ештеңе орындалмайды.
2. Бір stream-ді екі рет қолдануға болмайды → `IllegalStateException`.

**Байқа:** `allMatch` бос тізімде `true`, ал `anyMatch` — `false`. Тексеріс соны нақты көрсетеді.
Табылмаған нәтижені `null`-мен емес, `Optional`-мен қайтар.

---

## Тапсырма 12 — Коллекторлар (`CollectorLab`)

**Мақсат:** `collect(...)` арқылы кез келген пішінге жинау.

`joining`, `groupingBy`, `groupingBy + counting`, `partitioningBy`, `averagingInt`, `toMap`, `toSet`.

**Тұзақ 1:** `toMap()` қайталанған кілтте `IllegalStateException` лақтырады.
Үшінші аргумент — біріктіру функциясы — соны шешеді: `(a, b) -> a`.

**Тұзақ 2:** `partitioningBy` **әрқашан** `true`/`false` екі кілтін қалдырады
(біреуі бос тізім болса да), ал `groupingBy` бос топ мүлде жасамайды.

---

## Тапсырма 13 — Примитивті, жалқау, параллель ағындар (`StreamAdvanced`)

**Мақсат:** ағынның ішінде не болып жатқанын көру.

* `IntStream.rangeClosed`, `IntStream.of(...).average()`, `mapToInt`, `boxed` —
  қораптауды (boxing) болдырмау;
* `flatMap` — тізімдер тізімін жаю;
* `Stream.iterate(...).limit(n)` — шексіз ағын;
* `parallelStream()` — тек **ассоциативті** операцияларға.

**Ең қызықты екі метод — жалқаулық тәжірибесі:**

1. `lazyNoTerminal` — `peek(log::add)` бар, бірақ терминалдық операция жоқ.
   Дұрыс жазсаң `log` **бос** қалады.
2. `firstLongEnough` — сол құбыр + `findFirst()`. `log`-та **бүкіл тізім емес**,
   тек қаралған элементтер болады: ағын тапқан бойда тоқтайды (short-circuit).

Бұл екеуі stream-нің «элемент-элементпен өтеді» деген мінезін дәлелдейді.

---

## Тапсырма 14 — Ағындар (`ThreadLab`)

**Мақсат:** ағынның өмірлік циклін қолмен басқару.

`NEW -> RUNNABLE -> (BLOCKED / WAITING / TIMED_WAITING) -> TERMINATED`

* `counterThread` — **іске қосылмаған** ағын қайтарады (тексеріс өзі `start` жасайды);
* `runAndJoin` — бәрін `start`, сосын бәрін `join`;
* `nameInsideThread` — нәтижені лямбдадан шығару үшін `AtomicReference` керек;
* `interruptSleepingThread` — `interrupt()` + `InterruptedException`;
* `runInsteadOfStart` — **әдейі қате**: `run()` шақырғанда жаңа ағын жасалмайтынын
  көрсетеді (ат `main` болып шығады).

---

## Тапсырма 15 — Синхронизация және race condition

**Мақсат:** `count++` неге атомарлы емес екенін өз көзіңмен көру.

Бір `Counter` интерфейсінің үш іске асуы:

* `UnsafeCounter` — жай `count++`, **synchronized ҚОСПА**;
* `SyncCounter` — `increment()` synchronized **метод**, `get()` synchronized **блок**;
* `AtomicCounter` — `AtomicInteger.incrementAndGet()`.

`SyncLab.race()` 8 ағынды 50 000 реттен жүгіртеді. Күтілетін нәтиже — 400 000.

**Тексеріс `UnsafeCounter`-дің құлауын талап етпейді** (жарыс әрқашан көріне бермейді),
бірақ нақты мәнді экранға шығарады. Іс жүзінде ~90 000 шығады, яғни **300 мыңнан астам
арттыру жоғалады**. Жүгірткен сайын басқа сан көресің — тұрақсыздықтың мәні дәл осында.

**Ереже:** жай санағыш/жалауша → `Atomic*`. Бірнеше өрісті бірге өзгерту → `synchronized`.

---

## Тапсырма 16 — Executors, Callable, Future (`ExecutorLab`)

**Мақсат:** ағындарды қолмен емес, пул арқылы басқару.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
Future<Integer> f = pool.submit(() -> 42);   // Callable нәтиже ҚАЙТАРАДЫ
int result = f.get();                        // БЛОКТАЙДЫ
pool.shutdown();                             // МІНДЕТТІ
```

* `runAll` — `invokeAll`, нәтижелер **реті сақталады** (тексеріс ортадағы тапсырманы
  әдейі баяулатады);
* `sumConcurrently` — `submit` + `Future.get`;
* `awaitWorkers` — `CountDownLatch`;
* `timesOut` — `future.get(100, MILLISECONDS)` → `TimeoutException`.

**Ең жиі қате:** `shutdown()` шақырмау. Пул ағындары daemon емес, сондықтан
бағдарлама аяқталса да JVM тоқтамайды. Әрқашан `try/finally` ішінде жаб.

---

## Тапсырма 17 — Рефлексия (`ReflectionLab`)

**Мақсат:** класты «сырттан», тек `Class` объектісі арқылы зерттеу.
Spring, Jackson, JUnit — бәрі осылай істейді.

`getDeclaredFields`, `getDeclaredMethods`, `getSuperclass`,
`getDeclaredConstructors().newInstance(...)`, `Method.invoke`, `setAccessible(true)`.

`Person` класы — нысана, оны **өзгертпе**.

**Тексеріс көрсететін нәрсе:** рефлексия `private` өрісті сырттан өзгерте алады
(`writeField(person, "age", 30)`) — яғни инкапсуляция тек компилятор деңгейінде.

**Ескерту:** рефлексия баяу, компилятор оны тексермейді (метод атын қате жазсаң —
қате тек орындалу кезінде шығады). Күнделікті кодта қолданба.

---

## Тапсырма 18 — Пайдаланушы аннотациялары

**Мақсат:** JPA-ның `@Entity`/`@Column`-ы қалай жұмыс істейтінін кішірейтіп жасау.

Файлдар: `Table.java`, `Column.java` (өзің толықтырасың), `User.java` / `LogEntry.java`
(нысана, **өзгертпе**), `AnnotationProcessor.java`.

### Алдымен осылай жаса

`Table` мен `Column`-да `@Retention` **әдейі жоқ**. Сол күйінде жүгірт:

```
.\check.ps1 ch3 18
```

Барлығы құлайды. Себебі аннотацияның **әдепкі** өмір сүру мерзімі — `RetentionPolicy.CLASS`:
ол `.class` файлға жазылады, бірақ JVM оны жадыға жүктемейді, сондықтан рефлексия таппайды.

Содан кейін қос:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)    // Column үшін ElementType.FIELD
```

Бұл — аннотациялармен жұмыстағы №1 тұзақ. Бір рет өз көзіңмен көрсең, ұмытпайсың.

`describe(User.class)` → `"users(email NOT NULL, id NOT NULL, nickname)"`.
Бағандар **аты бойынша сұрыпталған**: `getDeclaredFields()` қайтаратын рет
спецификацияда кепілдендірілмеген.

---

## Тапсырма 19 — Файлдармен жұмыс (`FileLab`)

**Мақсат:** ескі `java.io` мен жаңа `java.nio.file`-ді қатар көру.

`Files.write` / `readAllLines` / `writeString` / `lines` / `list` / `copy` / `size`,
плюс бір метод әдейі ескі стильде — `BufferedReader` + `readLine()` циклі.

**Басты ереже:** `Files.lines()` мен `Files.list()` — **Stream**, олар файл дескрипторын
ұстайды. `try-with-resources` ішінде қолдану **міндетті**:

```java
try (Stream<String> lines = Files.lines(path)) {
    return lines.count();
}
```

Тексеріс уақытша каталог жасап, соңында өзі тазалайды — жобаңда қоқыс қалмайды.
Қазақша мәтін де тексеріледі (`java.nio` әдепкіде UTF-8).

---

## Тапсырма 20 — Ерекше жағдайлар (`ExceptionLab`)

**Мақсат:** `try`/`catch`/`finally`, try-with-resources және өз exception-ыңды жасау.

```
Throwable
  +-- Error              JVM-нің өлімші қатесі. ҰСТАМА.
  +-- Exception          checked
        +-- RuntimeException   unchecked
```

* `InsufficientFundsException` — **checked** (`extends Exception`), қосымша өрістермен;
  ақша жетпеуі — қалыпты іскерлік жағдай, шақырушы оны өңдей алады;
* теріс сома → `IllegalArgumentException` — **unchecked**, бұл бағдарламашының қатесі;
* `executionOrder` — `finally`-дің әрқашан орындалатынын дәлелдейді;
* `classify` — multi-catch: `catch (NumberFormatException | NullPointerException e)`;
* `resourceOrder` — try-with-resources, жабылу реті ашылудың **керісінше**;
* `wrapFailure` — **тізбектеу**: `new IllegalStateException(msg, e)`.

**Екі жаман әдет:**

```java
catch (Exception e) { }                                  // қатені жұту
catch (Exception e) { throw new RuntimeException(e.getMessage()); }   // СЕБЕПТІ жоғалту
```

Екіншісі әсіресе қауіпті: стектрейсте нақты не болғаны көрінбей қалады.
Дұрысы — `new RuntimeException(msg, e)`.

---

## Не кірмеді

**JDBC (157–160 б.)** — бұл жиынтыққа кірмеді: JDK-да драйвер де, дайын дерекқор да жоқ,
ал H2/PostgreSQL қосу Maven/Gradle-ді талап етеді (бұл жоба әдейі тәуелсіз).
JDBC-ді 4-главадағы Spring Boot жобасымен бірге, нақты дерекқорда істеген дұрыс.

**Параллельные коллекции (130 б.)** мен `CyclicBarrier`, `ReentrantLock` (142 б.) —
кітапта тізім түрінде ғана аталады. 15 және 16-тапсырмалардағы `synchronized`,
`AtomicInteger`, `CountDownLatch` меңгерілген соң, оларды құжаттамадан өз бетіңмен оқуға болады.
