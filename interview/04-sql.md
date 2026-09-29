# 04. SQL және оның Java-мен байланысы — 20 сұрақ

> `02-top-30.md`-де SQL-ден тек №28 (ACID, изоляция деңгейлері) және №29 (N+1) бар,
> `03-oop-jdbc-jpa.md`-де — JDBC API мен JPA. Бұл файл **SQL тілінің өзін** қамтиды
> және әр сұрақта «Java коды осы SQL-ге қалай әсер етеді» деген бөлік бар. Ол сұрақтар мұнда **қайталанбайды**.
> Сұрақтың өзі **орысша** жазылған, себебі сұхбатта ол дәл солай қойылады.
> `-- →` деп белгіленген SQL нәтижелерінің бәрі **SQLite 3.49-да**, №20-дағы Java `// →` шығыстары **JDK 17-де**
> нақты жүргізіліп тексерілді.
> PostgreSQL-ге тән жерлер (`EXPLAIN`, `FOR UPDATE SKIP LOCKED`, индекс түрлері) және Java коды
> дерекқорсыз жүргізілмеді — PostgreSQL 16, JDBC 4, Spring Data JPA 3 құжаттамасы бойынша жазылды.
>
> Әр сұрақтың құрылымы: **қысқа жауап** → **SQL** → **Java-мен байланысы** → **⚠ тұзақ**.

| Блок | Сұрақтар |
|---|---|
| SQL негіздері | 1–7 |
| Күрделі запростар | 8–12 |
| Индекстер мен жобалау | 13–17 |
| SQL ↔ Java практикасы | 18–20 |

Барлық мысалдар осы екі кестеге сүйенеді:

```sql
create table departments (id int primary key, name text);
insert into departments values (1, 'IT'), (2, 'HR'), (3, 'Sales');

create table employees (id int primary key, name text, dept_id int, salary int, manager_id int);
insert into employees values
  (1, 'Ali',   1,    5000, null),
  (2, 'Aru',   1,    4000, 1),
  (3, 'Bek',   1,    4000, 1),
  (4, 'Dana',  2,    6000, 1),
  (5, 'Erlan', null, 3000, 4);     -- бөлімі жоқ қызметкер
```

---

# SQL негіздері

## 1. Логический порядок выполнения SELECT

**Жауап.** Запрос жазылу ретімен **орындалмайды**. Логикалық рет:

```
FROM / JOIN → WHERE → GROUP BY → HAVING → SELECT → DISTINCT → ORDER BY → LIMIT / OFFSET
```

Осыдан шығатын салдарлар:
- `WHERE`-де `SELECT`-тегі **алиасты қолдануға болмайды** (ол әлі есептелмеген);
- `WHERE`-де агрегатты (`count`, `sum`) қолдануға болмайды — оған `HAVING` керек;
- `ORDER BY`-да алиас **жұмыс істейді** (ол `SELECT`-тен кейін).

```sql
select salary * 2 as dbl from employees where dbl > 9000;       -- PostgreSQL: ERROR: column "dbl" does not exist
select salary * 2 as dbl from employees where salary * 2 > 9000; -- дұрыс
select salary * 2 as dbl from employees order by dbl;            -- дұрыс
```

**Java-мен байланысы.** JPQL де сол ережеге бағынады: Hibernate JPQL-ді SQL-ге аударады,
логикалық рет өзгермейді. `Pageable`-дағы `Sort` → `ORDER BY`, беттеу → `LIMIT/OFFSET` ең соңында қолданылады.

⚠ SQLite пен MySQL кейде `WHERE`-де алиасқа рұқсат береді — бұл **стандарттан тыс**. Сұхбатта стандарт бойынша жауап бер.

---

## 2. Виды JOIN. Почему LEFT JOIN «превратился» в INNER?

| JOIN | Нәтиже |
|---|---|
| `INNER JOIN` | тек екі жақта да сәйкестігі бар жолдар |
| `LEFT JOIN` | сол жақтың **бәрі** + оң жақтан сәйкесі (жоқ болса `NULL`) |
| `RIGHT JOIN` | керісінше (іс жүзінде сирек, `LEFT`-пен ауыстырылады) |
| `FULL JOIN` | екі жақтың бәрі |
| `CROSS JOIN` | декарттық көбейтінді (N × M) |
| self join | кесте өзімен (`employees e join employees m on e.manager_id = m.id`) |

```sql
select d.name, e.name from departments d left join employees e on e.dept_id = d.id;
-- → IT Ali | IT Aru | IT Bek | HR Dana | Sales NULL      (Sales-та қызметкер жоқ, бірақ жол бар)

select d.name, e.name from departments d full join employees e on e.dept_id = d.id;
-- → ... + Sales NULL + NULL Erlan                         (екі жақтың «жетімдері» де)
```

**Тұзақ-классика:** оң кестеге шартты `WHERE`-ге жазсаң, `LEFT JOIN` іс жүзінде `INNER JOIN`-ға айналады:

```sql
-- ҚАТЕ: Sales жоғалады, себебі WHERE-де NULL > 4500 → false
select d.name, e.name from departments d left join employees e on e.dept_id = d.id
where e.salary > 4500;
-- → IT Ali | HR Dana

-- ДҰРЫС: шарт ON ішінде — сол жақ толық сақталады
select d.name, e.name from departments d left join employees e on e.dept_id = d.id and e.salary > 4500;
-- → IT Ali | HR Dana | Sales NULL
```

**Java-мен байланысы.** JPQL-де `join` = `INNER`, `left join` = `LEFT`.
`join fetch` N+1-ді шешеді (`02-top-30.md`, №29), бірақ ол **INNER**: бала коллекциясы бос ата-аналар нәтижеден **жоғалады**.

```java
@Query("select d from Department d left join fetch d.employees")   // бос бөлімдер де қалады
List<Department> findAllWithEmployees();
```

⚠ `@OneToMany` коллекциясына `join fetch` + `Pageable` → Hibernate беттеуді **жадта** жасайды
(`HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory`) — бүкіл кестені оқиды.

---

## 3. WHERE vs HAVING. GROUP BY

**Жауап.** `WHERE` **жолдарды** топтауға **дейін** сүзеді, `HAVING` — **топтарды** топтаудан **кейін**.
`GROUP BY` болса, `SELECT`-те тек топтау бағаналары мен агрегаттар бола алады.

```sql
select dept_id, count(*), avg(salary)
from employees
where salary > 1000            -- әр жолға
group by dept_id
having count(*) > 1;           -- әр топқа
-- → 1 | 3 | 4333.33
```

**Java-мен байланысы.** Агрегат нәтижесін entity-ге емес, **projection**-ға (record / DTO) алу керек:

```java
public record DeptStat(Integer deptId, long cnt, double avgSalary) {}

@Query("""
       select new com.example.DeptStat(e.deptId, count(e), avg(e.salary))
       from Employee e group by e.deptId having count(e) > 1
       """)
List<DeptStat> stats();
```

⚠ Топтауды Java-да `stream().collect(groupingBy(...))` арқылы жасау үшін **бүкіл кестені** жадқа тартасың.
Агрегатты дерекқорға қалдыр — ол индекспен және жадсыз жасайды.
⚠ Мүмкін болса, сүзгіні `HAVING`-ке емес, `WHERE`-ге жаз: топталатын жолдар азаяды.

---

## 4. NULL в SQL. Почему NOT IN вернул пустой результат?

**Жауап.** SQL-де **үш мәнді логика**: `true`, `false`, `unknown`. `NULL`-мен кез келген салыстыру (`=`, `<>`, `>`) —
`unknown`, ал `WHERE` тек `true`-ны өткізеді. `NULL`-ды тек `IS NULL` / `IS NOT NULL` арқылы тексереді.

```sql
select count(*) from employees where dept_id = null;      -- → 0  (дұрысы: is null)

select count(*), count(dept_id), count(distinct salary) from employees;
-- → 5 | 4 | 4     count(*) — барлық жол, count(col) — NULL емес мәндер

-- Қызметкері жоқ бөлімдер?
select name from departments where id not in (select dept_id from employees);
-- → (бос!)   ішкі запроста NULL бар: id <> NULL → unknown → ешбір жол өтпейді

select name from departments d where not exists (select 1 from employees e where e.dept_id = d.id);
-- → Sales    NOT EXISTS NULL-ға сезімтал емес
```

**Java-мен байланысы.**
- `rs.getInt()` `NULL`-ды `0` етіп қайтарады (`03`, №7) → entity-де nullable бағаналар үшін `Integer`, `Long` (wrapper) қолдан, `int` емес.
- Spring Data derived query: `findByDeptId(null)` → Spring өзі `where dept_id is null` жасайды.
  Бірақ `@Query("... where e.deptId = :id")`-ге `null` берсең → `= null` → **әрқашан бос**.

```java
@Query("select e from Employee e where (:deptId is null or e.deptId = :deptId)")   // «сүзгі міндетті емес» үлгісі
List<Employee> find(@Param("deptId") Integer deptId);
```

⚠ `sum()`, `avg()` `NULL`-ды өткізіп жібереді; жол мүлде болмаса `sum()` `0` емес, **`NULL`** қайтарады →
Java-да `long` емес `Long` ал немесе SQL-де `coalesce(sum(x), 0)` жаз.

---

## 5. DELETE vs TRUNCATE vs DROP

| | `DELETE` | `TRUNCATE` | `DROP` |
|---|---|---|---|
| Не жояды | жолдарды (`WHERE`-мен болады) | **барлық** жолдарды | кестені толығымен (құрылымымен) |
| Тип | DML | DDL | DDL |
| Жылдамдық | баяу (әр жол журналға) | өте жылдам (беттерді босатады) | жылдам |
| Триггерлер | іске қосылады | жоқ | жоқ |
| Rollback | болады | PostgreSQL-де болады, Oracle/MySQL-де **жоқ** (авто-commit) | сол сияқты |
| Identity / sequence | сақталады | қалпына келеді (`RESTART IDENTITY` / MySQL-де автоматты) | жойылады |

**Java-мен байланысы.** Интеграциялық тесттерде кестелерді тазалау үшін `TRUNCATE ... RESTART IDENTITY CASCADE`
`deleteAll()`-дан әлдеқайда жылдам. Spring Data-ның `deleteAll()` — алдымен **бәрін `SELECT`** етіп,
сосын **әрқайсысын жеке `DELETE`** етеді (cascade және `@PreRemove` үшін). Көп жол болса:

```java
@Modifying
@Query("delete from Employee e where e.deptId = :deptId")   // бір SQL, бірақ cascade/колбэктер ЖҰМЫС ІСТЕМЕЙДІ
int deleteByDept(@Param("deptId") int deptId);
```

⚠ Bulk `@Modifying` запрос persistence context-ті айналып өтеді: L1 кэште ескі объектілер қалады →
`@Modifying(clearAutomatically = true)`.

---

## 6. UNION vs UNION ALL

**Жауап.** `UNION` нәтижелерді біріктіріп, **дубликаттарды жояды** (сұрыптау/хэштеу керек → баяу).
`UNION ALL` жай қосады. Бағана саны мен типтері сәйкес болуы керек, бағана аттары **бірінші** запростан алынады.

```sql
select salary from employees where dept_id = 1
union
select salary from employees where dept_id = 1 order by salary;   -- → 4000 | 5000
-- union all орнына → 5000 | 4000 | 4000 | 5000 | 4000 | 4000   (6 жол)
```

⚠ Дубликат болмайтыны белгілі болса — **әрқашан `UNION ALL`**: артық сұрыптау жоқ.
⚠ `ORDER BY` бүкіл `UNION` нәтижесіне бір рет, соңында жазылады.
⚠ JPQL-де `UNION` тек Hibernate 6-дан бастап бар (JPA 3.2 стандартына кірді), ескі нұсқаларда — native query.

---

## 7. Подзапросы: обычный vs коррелированный. EXISTS vs IN

**Жауап.**
- **Қарапайым** ішкі запрос сыртқыдан тәуелсіз — бір рет орындалады.
- **Коррелированный** ішкі запрос сыртқы жолға сілтейді — логикалық тұрғыда **әр сыртқы жол үшін** орындалады.
- `EXISTS` бірінші сәйкестікті тапқанда тоқтайды және `NULL`-ға сезімтал емес; `IN` — мәндер тізімімен салыстырады.

```sql
-- бөлімдегі орташадан көп алатындар (коррелированный)
select e.name from employees e
where e.salary > (select avg(e2.salary) from employees e2 where e2.dept_id = e.dept_id);

-- кем дегенде бір қызметкері бар бөлімдер
select d.name from departments d
where exists (select 1 from employees e where e.dept_id = d.id);
```

**Java-мен байланысы.** Ең жиі кездесетін қате — ішкі запросты **Java циклына** айналдыру:

```java
for (Department d : departmentRepo.findAll()) {                 // 1 запрос
    if (employeeRepo.existsByDeptId(d.getId())) { ... }         // + N запрос → бұл да N+1
}
```

Мұны бір `exists` запросымен ауыстыр. Spring Data-да: `boolean existsByEmail(String email)` →
`select ... limit 1`, `count()` емес — бәрін санау қажет емес.

⚠ Қазіргі оптимизаторлар (PostgreSQL) `IN`/`EXISTS`-ті көбінесе бірдей semi-join-ға айналдырады.
Практикалық айырмашылық — `NOT IN` + `NULL` (№4).

---

# Күрделі запростар

## 8. Оконные функции. ROW_NUMBER vs RANK vs DENSE_RANK

**Жауап.** Терезе функциясы `GROUP BY` сияқты есептейді, бірақ **жолдарды біріктірмейді**:
әр жол сақталып, оған есептелген мән қосылады. Синтаксис: `func() over (partition by ... order by ...)`.

```sql
select name, salary,
       row_number() over (order by salary desc) as rn,
       rank()       over (order by salary desc) as rk,
       dense_rank() over (order by salary desc) as dr
from employees;
```

| name | salary | row_number | rank | dense_rank |
|---|---|---|---|---|
| Dana | 6000 | 1 | 1 | 1 |
| Ali | 5000 | 2 | 2 | 2 |
| Aru | 4000 | 3 | 3 | 3 |
| Bek | 4000 | 4 | **3** | **3** |
| Erlan | 3000 | 5 | **5** | **4** |

- `row_number` — әрқашан бірегей (тең мәндер арасында рет **анықталмаған**);
- `rank` — теңдерге бірдей нөмір, сосын **секіріс** (3, 3, 5);
- `dense_rank` — секіріссіз (3, 3, 4).

Басқа пайдалы функциялар: `sum() over (order by ...)` (жинақталған сома), `lag()` / `lead()` (алдыңғы/келесі жол).

```sql
select name, salary, sum(salary) over (order by id) as running from employees;
-- → Ali 5000 5000 | Aru 4000 9000 | Bek 4000 13000 | Dana 6000 19000 | Erlan 3000 22000
```

**Java-мен байланысы.** JPQL-де терезе функциялары тек Hibernate 6-да бар; әдетте native query + interface projection:

```java
interface RankedEmployee { String getName(); Integer getSalary(); Long getRk(); }

@Query(value = "select name, salary, dense_rank() over (order by salary desc) as rk from employees",
       nativeQuery = true)
List<RankedEmployee> ranked();
```

⚠ Терезе функциясының нәтижесін `WHERE`-де қолдануға **болмайды** (№1: ол `SELECT`-те есептеледі) —
ішкі запросқа немесе CTE-ге орап, сыртынан сүз (№9).

---

## 9. Задача: вторая по величине зарплата / топ-N в каждой группе

```sql
-- 2-ші ең үлкен жалақы (классикалық нұсқа)
select max(salary) from employees where salary < (select max(salary) from employees);   -- → 5000

-- N-ші ең үлкен жалақы (жалпы нұсқа, теңдерді дұрыс өңдейді)
select distinct salary from (
    select salary, dense_rank() over (order by salary desc) as r from employees
) t where r = 2;

-- әр бөлімдегі ең көп алатын(дар)
select name, dept_id, salary from (
    select e.*, dense_rank() over (partition by dept_id order by salary desc) as r
    from employees e where dept_id is not null
) t where r = 1;
-- → Ali 1 5000 | Dana 2 6000
```

⚠ `order by salary desc limit 1 offset 1` — **қате** жауап болуы мүмкін: екі адам 6000 алса, ол тағы 6000 қайтарады.
Сондықтан `distinct` немесе `dense_rank`.
⚠ «Әр бөлімнен дәл бір адам» десе → `row_number`, «теңдердің бәрі» десе → `dense_rank`. Сұхбатта осыны нақтыла.

**Java-мен байланысы.** Spring Data-да топ-N жалпы кесте бойынша оңай, бірақ «әр топта» — тек SQL-мен:

```java
List<Employee> findTop3ByOrderBySalaryDesc();          // → ... order by salary desc limit 3
```

---

## 10. CTE (WITH). Рекурсивный запрос для иерархии

**Жауап.** CTE — запрос ішіндегі **атаулы уақытша нәтиже**: оқуға жеңіл, бір запроста бірнеше рет қолдануға болады.
`WITH RECURSIVE` — ағаш тәрізді құрылымдарды (ұйым құрылымы, категориялар, пікірлер) бір запроспен жүріп шығу.

```sql
with recursive tree (id, name, lvl) as (
    select id, name, 0 from employees where manager_id is null      -- 1) якорь: түбір
    union all
    select e.id, e.name, t.lvl + 1                                  -- 2) рекурсия: балалары
    from employees e join tree t on e.manager_id = t.id
)
select * from tree order by lvl, id;
-- → 1 Ali 0 | 2 Aru 1 | 3 Bek 1 | 4 Dana 1 | 5 Erlan 2
```

**Java-мен байланысы.** Рекурсивті CTE-сіз ағашты Java-дан рекурсивті жүктеу = әр түйінге бір запрос:

```java
void load(Employee boss) {                                   // ЖАМАН: ағаштағы түйін саны = запрос саны
    for (Employee e : repo.findByManagerId(boss.getId())) load(e);
}
```

Дұрыс: бір native CTE-запроспен бүкіл тізімді ал, сосын Java-да `Map<Long, List<Node>>` арқылы ағашқа жина — O(n).

⚠ Деректе цикл болса (A → B → A), рекурсия шексіз жүреді. PostgreSQL 14+ — `CYCLE` сөйлемі,
немесе `lvl < 100` сияқты шектеу қос.

---

## 11. Как найти и удалить дубликаты?

```sql
-- табу
select salary, count(*) from employees group by salary having count(*) > 1;   -- → 4000 | 2

-- жою: әр email-дің ең кіші id-сін қалдыру
-- users: (1,'a@x') (2,'b@x') (3,'a@x') (4,'a@x')
delete from users where id not in (select min(id) from users group by email);
-- → (1,'a@x') (2,'b@x')

-- PostgreSQL-де жиі қолданылатын нұсқа
delete from users u using users d where u.email = d.email and u.id > d.id;
```

Дубликаттарды тазалағаннан кейін олардың қайта пайда болуына **constraint** арқылы тосқауыл қою керек:

```sql
alter table users add constraint uk_users_email unique (email);
```

**Java-мен байланысы.** «Алдымен `existsByEmail`, сосын `save`» — **race condition**: екі ағын бір уақытта
тексеруден өтіп, екеуі де жазады. Бірегейлікке тек **дерекқордың constraint-і** кепілдік береді; Java оның қатесін ұстайды:

```java
try {
    userRepo.saveAndFlush(user);          // flush — қате дәл осында шықсын
} catch (DataIntegrityViolationException e) {
    throw new EmailAlreadyTakenException(user.getEmail());
}
```

⚠ `@Column(unique = true)` constraint-ті тек `ddl-auto` схема жасағанда ғана құрады; Java жағында ешнәрсе тексермейді.

---

## 12. UPSERT: вставить или обновить

**Жауап.** «Жол бар болса — жаңарт, жоқ болса — қос» — **бір атомарлы** командамен:
- PostgreSQL / SQLite: `insert ... on conflict (key) do update`
- MySQL: `insert ... on duplicate key update`
- SQL стандарты (Oracle, SQL Server, PostgreSQL 15+): `merge`

```sql
-- stock: ('A', 5)
insert into stock (sku, qty) values ('A', 3), ('B', 2)
on conflict (sku) do update set qty = stock.qty + excluded.qty;   -- excluded = қосылмақ болған жол
-- → ('A', 8) ('B', 2)
```

**Java-мен байланысы.** JPA-ның `save()` — upsert **емес**: ол `id` бойынша `SELECT` жасайды (`merge`), сосын
`INSERT` не `UPDATE` жібереді — екі запрос және race condition. Жүктеме жоғары жерде — native upsert + batch:

```java
jdbcTemplate.batchUpdate("""
        insert into stock (sku, qty) values (?, ?)
        on conflict (sku) do update set qty = stock.qty + excluded.qty
        """,
        items, 500, (ps, item) -> { ps.setString(1, item.sku()); ps.setInt(2, item.qty()); });
```

⚠ `on conflict` үшін бағанада `unique` индекс/constraint **міндетті**, әйтпесе қате.

---

# Индекстер мен жобалау

## 13. Что такое индекс? Когда индекс НЕ используется?

**Жауап.** Индекс — кестенің қосымша құрылымы (әдепкі **B-tree**, сұрыпталған ағаш), ол жолды толық сканерлемей
**O(log n)**-да табуға мүмкіндік береді. Бағасы: диск орны және **әр `INSERT/UPDATE/DELETE`-тің баяулауы**.

| Индекс түрі (PostgreSQL) | Не үшін |
|---|---|
| B-tree | `=`, `<`, `>`, `between`, `order by`, `like 'abc%'` |
| Hash | тек `=` |
| GIN | `jsonb`, массивтер, толық мәтінді іздеу |
| GiST / BRIN | геодеректер / өте үлкен, реттелген кестелер (уақыт) |

Индекс **қолданылмайтын** жағдайлар:

```sql
where lower(email) = 'a@x'          -- бағанаға функция → функционалды индекс керек: create index on users (lower(email))
where name like '%ali'              -- басындағы % → B-tree көмектеспейді (pg_trgm + GIN керек)
where salary + 100 > 5000           -- бағанадағы арифметика → where salary > 4900 деп жаз
where phone = 77011234567           -- phone text, ал мән сан → тип түрлендіру
where status = 'ACTIVE'             -- жолдардың 95%-ы ACTIVE → Seq Scan арзанырақ (төмен селективтілік)
```

**Java-мен байланысы.**

```java
@Entity
@Table(name = "users", indexes = @Index(name = "idx_users_email", columnList = "email"))
class User { ... }

List<User> findByEmailIgnoreCase(String email);   // → where upper(email) = upper(?) — қарапайым индекс ІСТЕМЕЙДІ
```

⚠ Foreign key бағанасына PostgreSQL индексті **автоматты жасамайды** (MySQL/InnoDB жасайды).
`@ManyToOne` бағаналарына (`dept_id`) индексті өзің қос — әйтпесе `JOIN` және ата-ананы жою баяу.

---

## 14. Составной индекс. Правило левого префикса. Покрывающий индекс

**Жауап.** `(a, b, c)` индексі алдымен `a`, сосын `b`, сосын `c` бойынша сұрыпталған — телефон кітапшасы сияқты
(тегі → аты). Сондықтан ол **сол жақтан бастап** қолданылады:

| Шарт | `(a, b, c)` индексі қолданыла ма? |
|---|---|
| `a = 1` | иә |
| `a = 1 and b = 2` | иә |
| `b = 2 and a = 1` | иә (шарттардың реті маңызды емес) |
| `b = 2` | **жоқ** (не тиімсіз) |
| `a = 1 and c = 3` | тек `a` бөлігі |
| `a > 1 and b = 2` | `a` диапазоны — `b` бөлігі тиімсіз |

Бағаналардың реті: алдымен **теңдікпен** сүзілетіндер, сосын диапазон / сұрыптау.

**Покрывающий индекс** — запросқа керекті барлық бағаналар индексте бар → кестеге мүлде бармайды (**Index Only Scan**):

```sql
create index idx_emp_dept_salary on employees (dept_id, salary) include (name);   -- PostgreSQL 11+
select name, salary from employees where dept_id = 1 order by salary;             -- тек индекстен
```

**Java-мен байланысы.** Индексті **запростарға** қарап жобала: `findByDeptIdOrderBySalaryDesc(...)`
деген репозиторий әдісі → `(dept_id, salary)` индексі. `@Index(columnList = "dept_id, salary")`.

⚠ `(a)` және `(a, b)` екі индекс — артық: `(a, b)` бірінші индекстің жұмысын да атқарады.

---

## 15. Как понять, почему запрос медленный? EXPLAIN / EXPLAIN ANALYZE

**Жауап.** `EXPLAIN` — оптимизатордың **жоспары** (бағалау), `EXPLAIN ANALYZE` — запросты **шынымен орындап**,
нақты уақыт пен жол санын көрсетеді.

```sql
explain analyze select * from employees where dept_id = 1;
-- Seq Scan on employees  (cost=0.00..25.88 rows=6 width=44) (actual time=0.010..0.012 rows=3 loops=1)
--   Filter: (dept_id = 1)
```

| Жоспардағы түйін | Мағынасы |
|---|---|
| `Seq Scan` | бүкіл кестені оқу (кіші кестеде — қалыпты) |
| `Index Scan` | индекс + кестеден жол алу |
| `Index Only Scan` | тек индекстен (№14) |
| `Bitmap Heap Scan` | индекстен көп жол, беттер бойынша топтап оқу |
| `Nested Loop` / `Hash Join` / `Merge Join` | JOIN алгоритмдері |

Неге қарау керек: `rows` бағасы мен `actual rows` арасындағы үлкен айырма (статистика ескі → `ANALYZE`),
үлкен кестедегі `Seq Scan`, `Sort` дискке шығуы.

**Java-мен байланысы.** Алдымен Java не жіберетінін **көру** керек:

```properties
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE                  # параметр мәндері (Hibernate 6)
spring.jpa.properties.hibernate.generate_statistics=true         # бір сессиядағы запрос саны
```

Сосын сол SQL-ді параметрлерімен `EXPLAIN ANALYZE`-ке салу. Өндірісте — PostgreSQL `pg_stat_statements`.

⚠ `EXPLAIN ANALYZE` запросты **шынымен орындайды**: `delete`/`update` үшін оны `begin; ... rollback;` ішінде жүргіз.

---

## 16. Нормализация (1НФ, 2НФ, 3НФ) и денормализация

| Форма | Талап | Бұзылу мысалы |
|---|---|---|
| **1НФ** | әр ұяшықта **бір атомарлы** мән, қайталанатын топтар жоқ | `phones = '8701..., 8777...'` |
| **2НФ** | 1НФ + кілт емес бағана **бүкіл** құрама кілтке тәуелді | `order_items(order_id, product_id, product_name)` — `product_name` тек `product_id`-ге тәуелді |
| **3НФ** | 2НФ + кілт емес бағаналар арасында **транзитивті тәуелділік** жоқ | `employees(dept_id, dept_name)` — `dept_name` `dept_id` арқылы тәуелді |

Мақсаты — **дубликат пен аномалиялардан** құтылу: бөлім атауы өзгерсе, оны бір жерде ғана жаңартасың.

**Денормализация** — оқуды жылдамдату үшін әдейі қайталау (есептік кестелер, `orders.total_amount`, кэш-бағаналар).
Бағасы — деректердің синхрондылығын өзің қамтамасыз етесің.

**Java-мен байланысы.** Entity моделі кестелерді қайталайды:
3НФ → бөлек `@Entity Department` + `@ManyToOne`; 1НФ бұзылуы → `List<String> phones`-ті бір бағанаға сақтама,
`@ElementCollection` (бөлек кесте) немесе PostgreSQL `jsonb` қолдан. Бір кестедегі топталған өрістер
(мекенжай) — `@Embeddable`: объектіде бөлек класс, кестеде сол бағаналар.

⚠ «Әрқашан 3НФ» — догма емес. OLTP (транзакциялар) → нормализация, OLAP/есептер → денормализация.

---

## 17. Ключи и ограничения: PK, FK, UNIQUE, CHECK. Natural vs surrogate key

| Constraint | Кепілдік |
|---|---|
| `PRIMARY KEY` | бірегей + `NOT NULL`, кестеде біреу |
| `FOREIGN KEY` | сілтеме бар жолға көрсетеді (референттік тұтастық) |
| `UNIQUE` | бірегей (`NULL`-дар бірнеше болуы мүмкін) |
| `NOT NULL` / `CHECK` | міндеттілік / ереже (`check (salary >= 0)`) |

- **Natural key** — бизнес мағынасы бар (ИИН, email). Мәселесі: **өзгеруі мүмкін** және ұзын.
- **Surrogate key** — мағынасыз (`bigint` identity, `UUID`). Практикада PK = surrogate, ал natural key-ге `UNIQUE`.

```sql
create table employees (
    id        bigint generated always as identity primary key,
    iin       char(12) not null unique,
    dept_id   bigint references departments(id) on delete set null,
    salary    numeric(12, 2) check (salary >= 0)
);
```

**Java-мен байланысы.** JPA `cascade` мен дерекқордың `ON DELETE CASCADE` — **әртүрлі нәрсе**:

| | JPA `CascadeType.REMOVE` | SQL `ON DELETE CASCADE` |
|---|---|---|
| Кім орындайды | Hibernate, Java-да | дерекқор |
| Қалай | балаларды жүктеп, **әрқайсысын** `DELETE` | бір `DELETE`, дерекқор ішінде |
| `@PreRemove`, L1 кэш | ескеріледі | Hibernate **білмейді** |

⚠ Constraint-терді тек Java валидациясына (`@NotNull`, `@Size`) сеніп тастама: деректі басқа сервис, скрипт немесе
қолмен жазылған SQL өзгертуі мүмкін. **Соңғы қорғаныс — дерекқор.**
⚠ Схеманы өндірісте `ddl-auto=update` арқылы өзгертпе — Flyway/Liquibase миграцияларын қолдан (`validate` ғана қалдыр).

---

# SQL ↔ Java практикасы

## 18. Пагинация: OFFSET vs keyset (seek). Как работает Pageable?

**Жауап.** `limit 20 offset 100000` — дерекқор **100 020 жолды оқып**, 100 000-ын лақтырады: беттер алыстаған сайын баяу.
Сонымен қатар беттер арасында жол қосылса, **дубликат/өткізу** пайда болады.

**Keyset (seek) пагинация** — соңғы көрген кілттен бастап оқу, индекс бойынша бірден секіреді:

```sql
-- OFFSET
select * from orders order by created_at desc, id desc limit 20 offset 100000;

-- KEYSET: клиент алдыңғы беттің соңғы (created_at, id) мәнін қайтарады
select * from orders
where (created_at, id) < (:lastCreatedAt, :lastId)
order by created_at desc, id desc
limit 20;                                  -- индекс: (created_at, id)
```

**Java-мен байланысы.**

```java
Page<Order>  findByStatus(String status, Pageable p);   // LIMIT/OFFSET + ҚОСЫМША count(*) запросы
Slice<Order> findByStatus(String status, Pageable p);   // count(*) жоқ, limit+1 оқиды → «келесі бет бар ма»

Page<Order> page = repo.findByStatus("NEW", PageRequest.of(0, 20, Sort.by("createdAt").descending()));
```

Spring Data 3.1+ — `ScrollPosition.keyset()` + `Window<T>` арқылы keyset пагинация.

⚠ `Page` әр шақыруда `select count(*)` жасайды — миллиондаған жолда бұл негізгі запростан да баяу болуы мүмкін.
Шексіз лента үшін — `Slice` немесе keyset.
⚠ `ORDER BY` **бірегей** болуы керек (`created_at, id`), әйтпесе тең мәндер беттер арасында «секіреді».

---

## 19. Lost update: почему «прочитать → изменить в Java → сохранить» опасно?

**Жауап.** Екі ағын бір жолды оқып, Java-да өзгертіп, жазса — біреуінің өзгерісі **жоғалады**
(`READ COMMITTED`-те бұған ешнәрсе кедергі жасамайды):

```
T1: select balance → 100         T2: select balance → 100
T1: update balance = 100 - 30    T2: update balance = 100 - 50
Нәтиже: 50  (дұрысы — 20)
```

Шешімдері:

```sql
-- 1) Атомарлы UPDATE: есептеуді дерекқорға бер + шартпен тексер
update accounts set balance = balance - :amount where id = :id and balance >= :amount;
-- өзгерген жол саны 0 → қаражат жетпейді

-- 2) Құлыппен оқу (pessimistic)
select balance from accounts where id = :id for update;

-- 3) Нұсқамен (optimistic)
update accounts set balance = :newBalance, version = version + 1 where id = :id and version = :oldVersion;
```

**Java-мен байланысы.**

```java
// ҚАУІПТІ: read-modify-write
Account a = repo.findById(id).orElseThrow();
a.setBalance(a.getBalance().subtract(amount));          // dirty checking → update ... set balance = ?  (есептелген мән)

// ҚАУІПСІЗ: атомарлы update
@Modifying
@Query("update Account a set a.balance = a.balance - :amount where a.id = :id and a.balance >= :amount")
int withdraw(@Param("id") long id, @Param("amount") BigDecimal amount);

if (repo.withdraw(id, amount) == 0) throw new InsufficientFundsException();
```

`@Version` (optimistic) және `@Lock(PESSIMISTIC_WRITE)` (`FOR UPDATE`) — `03-oop-jdbc-jpa.md`, №20.

⚠ Кезек (job queue) үшін: `select ... for update skip locked limit 10` — бірнеше воркер бір-бірін күтпей,
әртүрлі жолдарды алады (PostgreSQL 9.5+, MySQL 8+).
⚠ Java-дағы `synchronized` бұл мәселені **шешпейді**: қолданба бірнеше инстансқа жайылса, құлып тек бір JVM ішінде.

---

## 20. Соответствие типов SQL ↔ Java. Какие ошибки бывают?

| SQL | Java | Ескерту |
|---|---|---|
| `integer` / `bigint` | `Integer` / `Long` | nullable болса — **wrapper** (№4) |
| `numeric(p, s)` / `decimal` | **`BigDecimal`** | ақша үшін **ешқашан** `double` емес |
| `varchar` / `text` | `String` | |
| `boolean` | `Boolean` | |
| `date` | `LocalDate` | |
| `timestamp` (without time zone) | `LocalDateTime` | уақыт белдеуі жоқ |
| `timestamptz` | `OffsetDateTime` / `Instant` | UTC-да сақталады |
| `uuid` | `UUID` | |
| `jsonb` | `String` / Hibernate 6 `@JdbcTypeCode(SqlTypes.JSON)` | |

```java
System.out.println(0.1 + 0.2);                                             // → 0.30000000000000004
System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));      // → 0.3

// JDBC 4.2+: java.time тікелей
ps.setObject(1, LocalDate.of(2026, 9, 29));
LocalDateTime t = rs.getObject("created_at", LocalDateTime.class);

// Entity
@Column(precision = 12, scale = 2) private BigDecimal salary;
@Enumerated(EnumType.STRING)        private Status status;     // ORDINAL — enum ретін ауыстырсаң, дерек бұзылады
```

⚠ `new BigDecimal(0.1)` (double-дан) → `0.1000000000000000055511151231257827...`; тек `new BigDecimal("0.1")` немесе `BigDecimal.valueOf(0.1)`.
⚠ `BigDecimal`-ды `equals` арқылы салыстырма: `2.0` пен `2.00` — `equals` → `false` (scale әртүрлі); `compareTo(...) == 0` қолдан.
⚠ `timestamp` + `LocalDateTime`: сервер мен дерекқордың уақыт белдеуі әртүрлі болса, уақыт «жылжиды».
Нақты сәт керек болса — `timestamptz` + `Instant`, және `hibernate.jdbc.time_zone=UTC`.
⚠ `java.util.Date` / `java.sql.Timestamp` — ескі API, жаңа кодта `java.time` қолдан.

---

## Өзіңді тексер: сұрақтарды жабық күйде қарап шық

| # | Сұрақ | Кілт сөздер |
|---|---|---|
| 1 | порядок выполнения SELECT | FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY |
| 2 | виды JOIN | WHERE LEFT-ті INNER етеді, join fetch = INNER |
| 3 | WHERE / HAVING | жолдар vs топтар, projection record |
| 4 | NULL | үш мәнді логика, NOT IN + NULL = бос, count(*) vs count(col) |
| 5 | DELETE / TRUNCATE / DROP | DML vs DDL, deleteAll() = N запрос |
| 6 | UNION / UNION ALL | дубликат жою = сұрыптау |
| 7 | подзапросы | коррелированный, EXISTS, Java-циклдағы N+1 |
| 8 | оконные функции | 1-2-3-4 / 1-2-3-3-5 / 1-2-3-3-4 |
| 9 | вторая зарплата | dense_rank, offset 1 тұзағы |
| 10 | рекурсивный CTE | якорь + union all, CYCLE |
| 11 | дубликаты | min(id), unique constraint, DataIntegrityViolationException |
| 12 | UPSERT | on conflict, excluded, save() ≠ upsert |
| 13 | индекс | B-tree, lower(), '%x', FK-ге индекс жоқ |
| 14 | составной индекс | сол жақ префикс, covering, Index Only Scan |
| 15 | EXPLAIN | Seq/Index Scan, actual rows, hibernate.SQL логы |
| 16 | нормализация | 1-2-3НФ, @Embeddable, OLTP vs OLAP |
| 17 | ключи и constraints | surrogate, JPA cascade ≠ ON DELETE CASCADE, Flyway |
| 18 | пагинация | offset баяу, keyset, Page vs Slice |
| 19 | lost update | balance = balance - ?, for update skip locked |
| 20 | типы SQL ↔ Java | BigDecimal, timestamptz, EnumType.STRING |
