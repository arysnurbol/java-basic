# 03. ООП, JDBC, JPA/Hibernate — 20 сұрақ

> `02-top-30.md` файлында бұл тақырыптар тек жанама қамтылған еді: ООП-тан тек №9 (абстракт класс vs интерфейс),
> JDBC мүлде жоқ, JPA-дан тек №28–30 (ACID, N+1, entity күйлері). Бұл файл сол олқылықты толтырады,
> сол сұрақтарды **қайталамайды**.
> Сұрақтың өзі **орысша** жазылған, себебі сұхбатта ол дәл солай қойылады.
> ООП блогындағы `// →` шығыстарының бәрі **JDK 17-де нақты жүргізіліп тексерілді**.
> JDBC/JPA блогындағы код дерекқорсыз жүргізілмеді — ол JDBC 4 және JPA 3 / Hibernate 6 спецификациясы бойынша жазылды.
>
> Әр сұрақтың құрылымы: **қысқа жауап** (сұхбатта осыны айтасың) → **код** → **⚠ тұзақ** (follow-up сұрағы).

| Блок | Сұрақтар |
|---|---|
| ООП | 1–6 |
| JDBC | 7–12 |
| JPA / Hibernate | 13–20 |

---

# ООП

## 1. Четыре принципа ООП

| Принцип | Мағынасы | Java-да қалай |
|---|---|---|
| **Инкапсуляция** | күйді жасырып, оған тек әдістер арқылы, **инвариантты сақтай отырып** қол жеткізу | `private` өрістер, валидациясы бар әдістер |
| **Наследование** | бар класты кеңейтіп, кодты қайта қолдану | `extends`, `implements` |
| **Полиморфизм** | бір интерфейс → әртүрлі жүзеге асыру; қай әдіс шақырылатыны **runtime-да** объектінің нақты түрі бойынша анықталады | override, интерфейстер |
| **Абстракция** | маңыздыны ғана көрсетіп, егжей-тегжейді жасыру | абстракт класс, интерфейс |

```java
class Account {
    private long balance;                       // сырттан тікелей өзгертуге болмайды
    void withdraw(long amount) {
        if (amount > balance) throw new IllegalStateException("not enough");
        balance -= amount;                      // инвариант: balance >= 0
    }
}
```

⚠ **«Геттер + сеттер = инкапсуляция» ма?** Жоқ. Әр өріске `setX()` қойсаң, инкапсуляция формалды ғана болады.
Нағыз инкапсуляция — объект өз инвариантын өзі қорғайды (`withdraw` бар, `setBalance` жоқ).

---

## 2. Перегрузка (overload) vs переопределение (override)

| | Overload | Override |
|---|---|---|
| Қайда | бір класта (немесе мұрагерде) | мұрагер класта |
| Сигнатура | аты бірдей, **параметрлері әртүрлі** | сигнатура **бірдей** |
| Қашан таңдалады | **компиляция** кезінде, сілтеменің **статикалық** түрі бойынша | **runtime** кезінде, объектінің нақты түрі бойынша |
| Қайтарылатын тип | кез келген | сол тип немесе оның ұрпағы (**ковариантты** return) |
| Exception | кез келген | checked exception-ды **кеңейтуге болмайды** |
| Қол жеткізу | кез келген | **тарылтуға болмайды** (`public` → `protected` болмайды) |

```java
static void m(Object o) { System.out.println("Object"); }
static void m(String s) { System.out.println("String"); }

Object o = "x";
m(o);        // → Object   (объект String болса да, сілтеменің түрі Object — компилятор соны көреді)
m("x");      // → String
m(null);     // → String   (ең нақты түр таңдалады; String ⊂ Object)
```

⚠ `m(null)` кезінде `m(String)` пен `m(Integer)` қатар болса — **компиляция қатесі** (ambiguous),
себебі екеуі де бірдей нақты.
⚠ Әрқашан `@Override` жаз: сигнатурада қате кетсе, компилятор бірден айтады (әйтпесе байқаусызда overload шығады).

---

## 3. Можно ли переопределить static / private метод? Полиморфны ли поля?

**Жауап.** **Жоқ.** Полиморфизм тек **instance, private емес** әдістерге жұмыс істейді.
- `static` әдіс мұрагерде қайта жарияланса — бұл **hiding** (жасыру), override емес: сілтеменің түрі бойынша таңдалады.
- `private` әдіс мұрагерге мүлде көрінбейді: мұрагердегі аттас әдіс — мүлде басқа әдіс.
- **Өрістер** де полиморфты емес: олар да hiding.

```java
class Parent {
    String name = "parent";
    String getName() { return name; }
    static String stat() { return "Parent.stat"; }
    private String priv() { return "Parent.priv"; }
    String callPriv() { return priv(); }
}
class Child extends Parent {
    String name = "child";
    @Override String getName() { return name; }
    static String stat() { return "Child.stat"; }
    private String priv() { return "Child.priv"; }
}

Parent p = new Child();
p.name;        // → parent       (өріс: сілтеменің түрі бойынша)
p.getName();   // → child        (override: объектінің түрі бойынша)
p.stat();      // → Parent.stat  (static: hiding)
p.callPriv();  // → Parent.priv  (private override болмайды)
```

⚠ Бұл **раннее (static) және позднее (dynamic) связывание** туралы сұрақ.
`static`, `private`, `final` әдістер мен өрістер — ерте байланыстыру; қалған instance әдістері — кеш
(JVM-де `invokevirtual` / `invokeinterface` арқылы, vtable бойынша).

---

## 4. Композиция vs наследование. Почему «favor composition over inheritance»?

**Жауап.** Мұрагерлік — **«is-a»**, композиция — **«has-a»**. Мұрагерлік инкапсуляцияны бұзады:
мұрагер ата-ананың **ішкі жүзеге асыруына** тәуелді болады (fragile base class problem).
Композиция кезінде объект басқа объектіні өрісінде ұстап, жұмысты соған **делегирует** етеді.

```java
class CountingSet<E> extends HashSet<E> {
    int addCount = 0;
    @Override public boolean add(E e) { addCount++; return super.add(e); }
    @Override public boolean addAll(Collection<? extends E> c) { addCount += c.size(); return super.addAll(c); }
}
CountingSet<String> s = new CountingSet<>();
s.addAll(List.of("a", "b", "c"));
s.addCount;    // → 6, ал 3 емес!  HashSet.addAll() іштей біздің add()-ты шақырады
```

Композиция арқылы:
```java
class CountingSet<E> {
    private final Set<E> inner = new HashSet<>();    // has-a
    private int addCount;
    boolean add(E e) { addCount++; return inner.add(e); }
    boolean addAll(Collection<? extends E> c) { addCount += c.size(); return inner.addAll(c); }  // → 3
}
```

⚠ Мұрагерлікті қашан қолдануға болады? Нағыз «is-a» болғанда **және** ата-ана кеңейтуге арнайы
жобаланғанда (мысалы, `AbstractList`). Кеңейтуге арналмаған класты `final` етіп қой (Effective Java, Item 18–19).

---

## 5. SOLID

| | Принцип | Бір сөйлеммен | Бұзылу мысалы |
|---|---|---|---|
| **S** | Single Responsibility | класты өзгертуге **бір ғана себеп** болуы керек | `UserService` әрі валидация, әрі SQL, әрі email жіберу жасайды |
| **O** | Open/Closed | кеңейтуге ашық, өзгертуге жабық | жаңа төлем түрі үшін `switch`-ке жаңа `case` қосу керек |
| **L** | Liskov Substitution | мұрагер ата-ананың орнына **келісімді бұзбай** қойыла алуы керек | `Square extends Rectangle`: `setWidth` биіктікті де өзгертеді |
| **I** | Interface Segregation | бір үлкен интерфейстен гөрі бірнеше кішкентайы жақсы | `Worker { work(); eat(); }` — роботқа `eat()` керек емес |
| **D** | Dependency Inversion | жоғары деңгей төменгі деңгейге емес, **абстракцияға** тәуелді болсын | `OrderService` ішінде `new MySqlRepository()` |

```java
// O + D: жаңа төлем түрі = жаңа класс, ескі код өзгермейді
interface PaymentMethod { void pay(long amount); }
class CardPayment implements PaymentMethod { public void pay(long a) { /* ... */ } }
class KaspiPayment implements PaymentMethod { public void pay(long a) { /* ... */ } }

class OrderService {
    private final PaymentMethod payment;              // абстракцияға тәуелді
    OrderService(PaymentMethod payment) { this.payment = payment; }   // сырттан беріледі (DI)
}
```

⚠ **LSP follow-up:** «`Square extends Rectangle` неге қате?» —
`Rectangle` келісімі: `setWidth(5); setHeight(4); area() == 20`. `Square` үшін `area() == 16` шығады,
яғни `Rectangle`-мен жұмыс істейтін код `Square`-мен бұзылады.

---

## 6. Порядок инициализации объекта. Что будет, если вызвать переопределяемый метод в конструкторе?

**Жауап.** Рет:
1. Ата-ананың `static` өрістері мен блоктары → мұрагердің `static` өрістері мен блоктары (**бір рет**, класс жүктелгенде);
2. Ата-ананың instance өрістері мен init-блоктары → ата-ананың конструкторы;
3. Мұрагердің instance өрістері мен init-блоктары → мұрагердің конструкторы.

```java
class A {
    static { print("1 A static"); }
    { print("3 A init-block"); }
    A() { print("4 A()"); show(); }
    void show() {}
}
class B extends A {
    static { print("2 B static"); }
    String value = "set";
    { print("5 B init-block"); }
    B() { print("6 B(), value=" + value); }
    @Override void show() { print("   B.show value=" + value); }
}
new B();
// → 1 A static
// → 2 B static
// → 3 A init-block
// → 4 A()
// →    B.show value=null      ← B-ның өрістері әлі инициализацияланбаған!
// → 5 B init-block
// → 6 B(), value=set
new B();   // екінші рет: 1 және 2 жолдары ШЫҚПАЙДЫ (static бір рет қана)
```

⚠ Конструктордан **переопределяемый** әдісті шақырма: мұрагердің әдісі оның өрістері
инициализацияланбай тұрып орындалады (`null`, `0`). Конструктордан тек `private` / `final` / `static` әдістерді шақыр.

---

# JDBC

## 7. Основные интерфейсы JDBC и порядок работы

**Жауап.** JDBC — дерекқормен жұмыс істеуге арналған **интерфейстер** жиынтығы (`java.sql`).
Жүзеге асыруды нақты дерекқордың **драйвері** береді (PostgreSQL, MySQL, ...).

| Интерфейс | Рөлі |
|---|---|
| `DriverManager` / `DataSource` | байланыс алу (`DataSource` — өндірісте, пулмен) |
| `Connection` | дерекқорға бір байланыс сессиясы, транзакция осында басқарылады |
| `Statement` / `PreparedStatement` / `CallableStatement` | SQL орындау |
| `ResultSet` | `SELECT` нәтижесі, курсор |

```java
String sql = "select id, name from users where age > ?";
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setInt(1, 18);                          // параметрлер 1-ден басталады
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {                    // курсор бірінші жолдың АЛДЫНДА тұрады
            long id = rs.getLong("id");
            String name = rs.getString(2);     // бағаналар да 1-ден
        }
    }
}
```

| Әдіс | Не үшін | Қайтарады |
|---|---|---|
| `executeQuery` | `SELECT` | `ResultSet` |
| `executeUpdate` | `INSERT/UPDATE/DELETE`, DDL | өзгерген жолдар саны `int` |
| `execute` | кез келген SQL | `true`, егер нәтиже `ResultSet` болса |

⚠ `Class.forName("org.postgresql.Driver")` **JDBC 4.0-ден (Java 6) бері керек емес**: драйвер classpath-та болса,
`ServiceLoader` арқылы автоматты тіркеледі.
⚠ `rs.getInt()` бағана `NULL` болса **`0` қайтарады**. Нағыз `NULL` екенін `rs.wasNull()` арқылы тексер
немесе `rs.getObject("age", Integer.class)` қолдан.

---

## 8. Statement vs PreparedStatement vs CallableStatement. Что такое SQL-инъекция?

| | `Statement` | `PreparedStatement` | `CallableStatement` |
|---|---|---|---|
| SQL | жол ретінде қосылады | `?` параметрлерімен | `{call proc(?, ?)}` |
| SQL-инъекция | **осал** | қорғалған | қорғалған |
| Прекомпиляция | жоқ | иә (жоспар қайта қолданылуы мүмкін) | иә |
| Қолданылуы | статикалық DDL | **күнделікті жұмыс** | stored procedure |

```java
// ОСАЛ: name = "' or '1'='1"  →  барлық қолданушы қайтарылады
stmt.executeQuery("select * from users where name = '" + name + "'");

// ДҰРЫС: мән SQL мәтіні ретінде емес, ДЕРЕК ретінде беріледі
PreparedStatement ps = conn.prepareStatement("select * from users where name = ?");
ps.setString(1, name);

// Stored procedure
CallableStatement cs = conn.prepareCall("{call get_balance(?, ?)}");
cs.setLong(1, userId);
cs.registerOutParameter(2, Types.NUMERIC);
cs.execute();
BigDecimal balance = cs.getBigDecimal(2);
```

⚠ `?` арқылы тек **мәндерді** беруге болады. Кесте, бағана атауын немесе `ORDER BY` бағытын `?` арқылы беруге **болмайды** —
оларды whitelist арқылы тексеріп барып қос.
⚠ `where id in (?)` ішіне тізімді бір `?` арқылы беруге болмайды: `?`-ті керек санда генерациялайды
немесе PostgreSQL-де `= any(?)` + `conn.createArrayOf(...)` қолданады.

---

## 9. Транзакции в JDBC. autoCommit, commit, rollback, Savepoint

**Жауап.** Әдепкі бойынша `autoCommit = true`: **әр SQL өз транзакциясында** бірден commit болады.
Бірнеше операцияны атомарлы ету үшін autoCommit-ті өшіріп, `commit()` / `rollback()` қолмен шақырылады.

```java
try (Connection conn = dataSource.getConnection()) {
    conn.setAutoCommit(false);
    try {
        debit(conn, from, amount);
        credit(conn, to, amount);
        conn.commit();                          // екеуі бірге сақталады
    } catch (SQLException e) {
        conn.rollback();                        // не екеуі де болмайды
        throw e;
    }
}

// Savepoint: транзакцияның бір бөлігін ғана кері қайтару
Savepoint sp = conn.setSavepoint("afterOrder");
try { insertBonus(conn); } catch (SQLException e) { conn.rollback(sp); }   // заказ қалады, бонус жоқ
conn.commit();
```

⚠ Транзакция **`Connection`-ге** байланған. Екі түрлі `Connection` арқылы орындалған операциялар бір транзакция бола алмайды.
Spring-тің `@Transactional` осы себепті байланысты `ThreadLocal`-да ұстайды.
⚠ Изоляция деңгейі: `conn.setTransactionIsolation(...)` (толығырақ — `02-top-30.md`, №28).

---

## 10. Batch-обработка в JDBC

**Жауап.** Көп жолды бір-бірлеп `executeUpdate()` арқылы жіберу = N рет желі арқылы бару (round-trip).
Batch көп командаларды **бір пакетпен** жібереді.

```java
conn.setAutoCommit(false);
try (PreparedStatement ps = conn.prepareStatement("insert into users(name, age) values (?, ?)")) {
    int i = 0;
    for (User u : users) {
        ps.setString(1, u.name());
        ps.setInt(2, u.age());
        ps.addBatch();
        if (++i % 1000 == 0) ps.executeBatch();    // жадты толтырмау үшін бөліп жіберу
    }
    ps.executeBatch();                              // int[] — әр команда үшін өзгерген жолдар саны
}
conn.commit();
```

⚠ Драйвер batch-ты шынымен бір запросқа біріктіруі үшін кейде параметр керек:
MySQL — `rewriteBatchedStatements=true`, PostgreSQL — `reWriteBatchedInserts=true`.
⚠ Batch ортасында қате болса — `BatchUpdateException`, `getUpdateCounts()` арқылы қайсысы өткенін көруге болады.
Сондықтан batch-ты транзакция ішінде орында.

---

## 11. Что такое пул соединений и зачем он нужен?

**Жауап.** Жаңа `Connection` ашу **қымбат**: TCP байланыс, аутентификация, сессия құру (миллисекундтар).
Пул байланыстарды алдын ала ашып ұстайды және оларды қайта қолданады.
Java-дағы стандарт — **HikariCP** (Spring Boot-та әдепкі).

```java
HikariConfig cfg = new HikariConfig();
cfg.setJdbcUrl("jdbc:postgresql://localhost:5432/app");
cfg.setUsername("app"); cfg.setPassword("secret");
cfg.setMaximumPoolSize(10);
DataSource ds = new HikariDataSource(cfg);

try (Connection conn = ds.getConnection()) {   // пулдан алады
    // ...
}                                              // close() байланысты ЖАППАЙДЫ, пулға қайтарады
```

⚠ **Пул неше байланыс болуы керек?** «Көп болса жақсы» емес: дерекқор CPU мен дискке тіреледі.
HikariCP ұсынатын бастапқы формула: `connections = ядро_саны * 2 + дискілер_саны` — сосын жүктеме тестімен түзетіледі.
⚠ `close()` шақырмасаң — **connection leak**: пул таусылып, қалған ағындар `getConnection()`-да күтіп тұрады,
сосын `SQLTransientConnectionException` шығады. Hikari-де `leakDetectionThreshold` бар.

---

## 12. Какие проблемы у «голого» JDBC? Зачем нужен ORM?

**Жауап.** Таза JDBC-дің кемшіліктері:
- **boilerplate**: әр запрос үшін `Connection` → `PreparedStatement` → `ResultSet` → қолмен маппинг → ресурстарды жабу;
- `SQLException` — **checked**, әрі нақты қатені vendor-кодтан ажырату керек;
- `ResultSet`-ті объектілерге қолмен маппинг жасау, байланыстарды (`User` → `List<Order>`) қолмен құрастыру;
- SQL мәтіні нақты дерекқорға байланады (dialect).

**Object-relational impedance mismatch** — объектілер әлемі (мұрагерлік, сілтемелер, коллекциялар)
мен кестелер әлемі (жолдар, foreign key) арасындағы сәйкессіздік. ORM осыны шешеді.

| Деңгей | Мысал | Не береді |
|---|---|---|
| JDBC | `java.sql` | толық бақылау, максималды жылдамдық |
| JDBC-обёртка | Spring `JdbcTemplate`, jOOQ | boilerplate жоқ, SQL өзің жазасың |
| ORM | JPA / Hibernate | объектілермен жұмыс, dirty checking, кэш, lazy loading |

⚠ ORM «SQL-ді білмеуге» мүмкіндік бермейді: N+1, артық `JOIN`-дар, batch-тың жоқтығы — бәрі SQL деңгейінде ғана көрінеді.
Күрделі есептер (отчёттар) үшін native SQL немесе `JdbcTemplate` жиі тиімдірек.

---

# JPA / Hibernate

## 13. JPA vs Hibernate vs Spring Data JPA

**Жауап.**
- **JPA** (Jakarta Persistence) — **спецификация**: аннотациялар (`@Entity`, `@Id`, ...) және интерфейстер (`EntityManager`, JPQL).
  Өзі ештеңе орындамайды.
- **Hibernate** — JPA-ның ең танымал **жүзеге асыруы** (провайдер). Өзінің қосымша мүмкіндіктері бар
  (`@BatchSize`, `@Formula`, `Session` API). Басқа провайдерлер: EclipseLink.
- **Spring Data JPA** — JPA-ның үстіндегі абстракция: `JpaRepository`, әдіс атауынан запрос генерациялау.

| JPA | Hibernate баламасы |
|---|---|
| `EntityManagerFactory` | `SessionFactory` |
| `EntityManager` | `Session` |
| JPQL | HQL (JPQL-дің кеңейтілген нұсқасы) |

⚠ Java EE → Jakarta EE көшуінен кейін пакет **`javax.persistence` → `jakarta.persistence`** болды
(Hibernate 6, Spring Boot 3). Ескі және жаңа кітапханаларды араластыру — жиі кездесетін қате.

---

## 14. Как объявить сущность? Стратегии генерации ID

**Жауап.** Entity-ге қойылатын талаптар: `@Entity`, `@Id`, **public/protected no-args конструктор**,
класс `final` болмауы керек (Hibernate lazy-proxy жасау үшін мұрагер класс құрады).

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)          // ORDINAL емес!
    private Status status;

    @Transient
    private String tempToken;             // дерекқорға сақталмайды

    protected User() {}                   // JPA үшін
}
```

| Стратегия | Қалай | Ескерту |
|---|---|---|
| `IDENTITY` | дерекқордың auto-increment | id тек INSERT-тен кейін белгілі → Hibernate **batch insert-ті өшіреді** |
| `SEQUENCE` | дерекқордың sequence | `allocationSize` арқылы id-лар топтап алынады, batch жұмыс істейді — **ең жақсы таңдау** |
| `TABLE` | бөлек кесте-санағыш | баяу, құлыптар |
| `AUTO` | провайдер өзі таңдайды | нәтиже болжаусыз |
| `UUID` (JPA 3.1+) | `java.util.UUID` | индекс үлкен, кездейсоқ ретпен түседі |

⚠ `@Enumerated(EnumType.ORDINAL)` (әдепкі!) enum-ға ортасынан жаңа мән қосқанда **барлық ескі деректерді бұзады**.
Әрқашан `EnumType.STRING` қолдан.

---

## 15. persist vs merge. find vs getReference

**persist vs merge:**

| | `persist(e)` | `merge(e)` |
|---|---|---|
| Кімге | жаңа (transient) объектіге | detached (немесе жаңа) объектіге |
| Нәтиже | **сол объект** managed болады | **жаңа managed көшірме** қайтарады, аргумент detached күйінде қалады |
| Detached-ке шақырса | exception (`EntityExistsException` / `PersistentObjectException`) | күйін managed көшірмеге көшіреді |

```java
User detached = ...;
User managed = em.merge(detached);
managed.setName("A");    // сақталады
detached.setName("B");   // САҚТАЛМАЙДЫ — бұл әлі detached
```

**find vs getReference:**

| | `find(User.class, id)` | `getReference(User.class, id)` |
|---|---|---|
| Запрос | бірден `SELECT` | жасамайды, **прокси** қайтарады |
| Жол жоқ болса | `null` | өріске жүгінгенде `EntityNotFoundException` |
| Қашан керек | объект шынымен керек | тек **FK орнату** үшін |

```java
Order o = new Order();
o.setUser(em.getReference(User.class, userId));   // User-ді SELECT етпей-ақ байланыс орнатамыз
em.persist(o);
```

⚠ Hibernate-тің `save()`, `update()`, `saveOrUpdate()` әдістері **Hibernate 6-да deprecated** —
JPA-ның `persist` / `merge` әдістерін қолдан.

---

## 16. FetchType LAZY vs EAGER. Как бороться с LazyInitializationException?

**Жауап.** `LAZY` — байланыс оған алғаш жүгінгенде жүктеледі (прокси немесе `PersistentBag` арқылы).
`EAGER` — ата-ана объектісімен бірге бірден жүктеледі.

| Аннотация | Әдепкі FetchType |
|---|---|
| `@ManyToOne`, `@OneToOne` | **EAGER** ⚠ |
| `@OneToMany`, `@ManyToMany` | LAZY |

**Ереже:** барлық байланысты `LAZY` етіп қой, керек жерде запрос деңгейінде жүкте.
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;
```

**`LazyInitializationException`** — сессия жабылғаннан кейін (detached) LAZY байланысқа жүгінгенде шығады.

| Шешім | Бағасы |
|---|---|
| `join fetch` / `@EntityGraph` запроста | ✅ дұрыс |
| DTO проекциясы | ✅ дұрыс, тек керек бағаналар |
| Транзакция ішінде `Hibernate.initialize(...)` | ✅ жарамды |
| `FetchType.EAGER` | ❌ N+1 және артық деректер |
| Open Session in View (`spring.jpa.open-in-view=true`) | ❌ антипаттерн: запростар View қабатында, транзакциясыз орындалады |
| `hibernate.enable_lazy_load_no_trans=true` | ❌ әр жүгінуге жаңа сессия, жасырын N+1 |

⚠ Spring Boot-та `open-in-view` **әдепкі бойынша қосулы** (іске қосқанда WARN шығарады). Оны `false` етіп қою ұсынылады.

---

## 17. Связи @OneToMany / @ManyToOne: owning side, mappedBy, cascade, orphanRemoval

**Жауап.** Екі жақты байланыста **иелік етуші жақ** (owning side) — foreign key-ді ұстайтын жақ,
яғни `@ManyToOne`. Кері жақ `mappedBy` арқылы белгіленеді. Hibernate FK-ны **тек owning side бойынша** жазады.

```java
@Entity class Author {
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book b) { books.add(b); b.setAuthor(this); }      // екі жақты да синхрондау
    public void removeBook(Book b) { books.remove(b); b.setAuthor(null); }
}
@Entity class Book {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")          // owning side: FK осында
    private Author author;
}
```

| Опция | Не істейді |
|---|---|
| `cascade = PERSIST / MERGE / REMOVE / ALL ...` | ата-анаға жасалған операцияны балаларына **тарату** |
| `orphanRemoval = true` | коллекциядан алынған баланы **дерекқордан өшіру** |

⚠ `author.getBooks().add(book)` деп қана жазсаң (кері жақ), `book.author == null` болып қалады —
FK **сақталмайды**. Сондықтан `addBook` сияқты helper-әдістер жазылады.
⚠ `@ManyToMany`-ге `CascadeType.REMOVE` / `ALL` қойма: бір студентті өшіргенде ортақ курстар да өшіп кетеді.
⚠ `mappedBy`-сыз `@OneToMany` Hibernate-те **қосымша join-кесте** құрады — бұл көбіне күтпеген нәтиже.

---

## 18. Стратегии наследования в JPA

| Стратегия | Кестелер | Артықшылығы | Кемшілігі |
|---|---|---|---|
| `SINGLE_TABLE` (әдепкі) | бір кесте + `dtype` бағанасы | ең жылдам, JOIN жоқ, полиморфты запрос оңай | мұрагер өрістері `NOT NULL` бола алмайды, бос бағаналар көп |
| `JOINED` | ата-ана кестесі + әр мұрагерге бөлек кесте (PK = FK) | нормализацияланған, `NOT NULL` болады | әр запроста JOIN |
| `TABLE_PER_CLASS` | әр нақты класқа толық кесте | бір түрді оқу жылдам | полиморфты запрос = `UNION`, `IDENTITY` қолдануға болмайды |
| `@MappedSuperclass` | ата-ана кестесі жоқ | ортақ өрістерді (`id`, `createdAt`) қайта қолдану | ата-ана **entity емес**, оған запрос та, байланыс та жасалмайды |

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
abstract class Payment { @Id @GeneratedValue Long id; BigDecimal amount; }

@Entity @DiscriminatorValue("CARD")  class CardPayment extends Payment { String cardNumber; }
@Entity @DiscriminatorValue("KASPI") class KaspiPayment extends Payment { String phone; }

em.createQuery("select p from Payment p", Payment.class);   // полиморфты: барлық түрі қайтарылады
```

⚠ **Қайсысын таңдау керек?** Әдетте: мұрагер өрістері аз болса — `SINGLE_TABLE`, көп және әртүрлі болса — `JOINED`.
Тек ортақ өрістер керек болса (`BaseEntity`) — `@MappedSuperclass`.

---

## 19. Кэш первого и второго уровня в Hibernate

| | L1 кэш | L2 кэш |
|---|---|---|
| Қай деңгейде | бір `EntityManager` / `Session` | `EntityManagerFactory` / `SessionFactory`, барлық сессияларға ортақ |
| Қосулы ма | **әрқашан**, өшіруге болмайды | **әдепкі бойынша өшірулі** |
| Өмір сүруі | сессия / транзакция бойы | қолданба бойы (TTL, eviction бойынша) |
| Жүзеге асыру | Hibernate-тің өзінде | сыртқы провайдер: Ehcache, Caffeine (JCache арқылы), Infinispan |

```java
User a = em.find(User.class, 1L);   // SELECT
User b = em.find(User.class, 1L);   // SELECT жоқ — L1 кэштен
a == b;                             // true — бір сессияда бір id = бір объект (identity map)

// L2 қосу
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Country { ... }
```

⚠ L1 кэш **JPQL запростарына әсер етпейді**: `createQuery(...)` әрқашан SQL жібереді
(бірақ нәтижедегі объектілер сессияда бар болса, сол объектілер қайтарылады).
Запрос нәтижесін кэштеу үшін бөлек **query cache** керек.
⚠ L2 кэш — жиі оқылатын, сирек өзгеретін деректер үшін (анықтамалықтар). Дерекқорды басқа сервис
тікелей өзгертсе, кэш **ескірген** деректі береді.
⚠ Үлкен batch-өңдеуде L1 кэш жадты толтырады → кезең-кезеңімен `em.flush(); em.clear();` шақыр.

---

## 20. Оптимистичная vs пессимистичная блокировка

**Жауап.** Екеуі де **lost update** мәселесін шешеді: екі транзакция бір жолды оқып, екеуі де өзгертсе,
біреуінің өзгерісі жоғалады.

**Optimistic** — құлып жоқ, конфликт **commit кезінде** анықталады:
```java
@Entity class Account {
    @Id Long id;
    BigDecimal balance;
    @Version Long version;                 // Hibernate өзі басқарады
}
// Hibernate генерациялайтын SQL:
// update account set balance=?, version=6 where id=? and version=5
// 0 жол өзгерсе → OptimisticLockException  (бізден бұрын басқа біреу өзгертті)
```

**Pessimistic** — жол оқылған сәтте **құлыпталады** (`SELECT ... FOR UPDATE`):
```java
Account acc = em.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE);
// басқа транзакциялар бұл жолды біз commit/rollback жасағанша өзгерте алмайды
```

| | Optimistic | Pessimistic |
|---|---|---|
| Механизм | `version` бағанасы | дерекқор құлпы |
| Конфликт аз болғанда | ✅ жылдам | артық құлыптар |
| Конфликт жиі болғанда | көп retry | ✅ |
| Қауіп | қолданба exception-ды өңдеп, **retry** жасауы керек | deadlock, күту уақыты |

⚠ `@Version` өрісін **қолмен өзгертпе** және setter арқылы клиенттен қабылдама — әйтпесе тексерудің мәні жоғалады.
⚠ Pessimistic құлып тек **транзакция ішінде** мағыналы; құлыпты күту уақытын `jakarta.persistence.lock.timeout` арқылы шектеуге болады.

---

## Өзіңді тексер: сұрақтарды жабық күйде қарап шық

| # | Сұрақ | Кілт сөздер |
|---|---|---|
| 1 | 4 принцип ООП | инвариант, «геттер ≠ инкапсуляция» |
| 2 | overload / override | компиляция vs runtime, m(null) |
| 3 | static / private / поля | hiding, ранее/позднее связывание |
| 4 | композиция / наследование | fragile base class, addCount=6 |
| 5 | SOLID | Square/Rectangle, DI |
| 6 | порядок инициализации | static бір рет, value=null |
| 7 | интерфейсы JDBC | executeQuery/Update, wasNull |
| 8 | Statement / Prepared | инъекция, ? тек мәндерге |
| 9 | транзакции JDBC | autoCommit, Savepoint |
| 10 | batch | addBatch, rewriteBatched |
| 11 | пул соединений | HikariCP, close() → пулға, leak |
| 12 | JDBC vs ORM | boilerplate, impedance mismatch |
| 13 | JPA / Hibernate | спецификация vs провайдер, jakarta |
| 14 | @Entity, ID | IDENTITY batch-ты өшіреді, EnumType.STRING |
| 15 | persist / merge | көшірме, getReference = прокси |
| 16 | LAZY / EAGER | ToOne = EAGER, open-in-view |
| 17 | связи | owning side, mappedBy, orphanRemoval |
| 18 | наследование | SINGLE_TABLE, JOINED, MappedSuperclass |
| 19 | L1 / L2 кэш | identity map, flush+clear |
| 20 | блокировки | @Version, FOR UPDATE, retry |
