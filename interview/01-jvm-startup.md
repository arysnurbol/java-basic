# 01. Java-қосымша қалай іске қосылады

> Дереккөздер: Boosty (JVM бөлімі: «Что происходит при старте java-приложения»,
> класслоадеры, области памяти, JIT, static/final кепілдіктері, объект құрылымы),
> ITVDN (Jr №5–7, 47, 60; Mid №11–12, 35–36; Sr №6),
> enhorse/java-interview ([jvm.md](https://github.com/enhorse/java-interview/blob/master/jvm.md)).
>
> Барлық шығыс нәтижелері **JDK 17.0.12, HotSpot, Windows** ортасында нақты жүргізіп алынды.
> Сұхбатта терминдер орысша айтылады, сондықтан олар жақша ішінде берілген.

---

## Жалпы сурет: `java Main` → процесс аяқталғанға дейін

```
javac Main.java  →  Main.class (bytecode)
        │
java Main
        │
 1. Launcher (java.exe) ОЖ-да процесс ашады, JVM-ді (jvm.dll / libjvm.so) жүктейді
 2. JVM өзін инициализациялайды: жады аймақтары, GC, JIT, JVM-нің жүйелік ағындары
 3. Bootstrap ClassLoader ядро кластарын жүктейді (Object, String, System, Thread …)
        └─ көбісі CDS архивінен («shared objects file») дайын күйінде оқылады
 4. System.initPhase1/2/3: System.out, қасиеттер (properties), модульдер, ClassLoader-лар
 5. LauncherHelper main класын AppClassLoader арқылы табады және жүктейді
 6. Main класы: Loading → Linking (verify, prepare, resolve) → Initialization (<clinit>)
 7. "main" ағынында public static void main(String[]) шақырылады
        └─ bytecode алдымен интерпретацияланады, «ыстық» код JIT арқылы native кодқа айналады
 8. Қосымша жұмыс істейді: кластар керек болған сәтте ғана (lazy) жүктеледі,
    объектілер heap-те құрылады, GC фонда жады тазалайды
 9. Демон емес соңғы ағын аяқталса немесе System.exit() шақырылса:
    shutdown hooks орындалады → JVM тоқтайды → процесс exit code қайтарады
```

**Сұхбатқа арналған 30 секундтық жауап:**
«Launcher JVM-ді көтереді. JVM жадты, GC мен JIT-ті инициализациялайды. Bootstrap loader
ядро кластарын жүктейді (Java 17-де олардың көбі CDS архивінен келеді). Содан кейін
AppClassLoader main класын жүктейді, ол linking пен initialization кезеңдерінен өтеді
(static блоктар осы кезде орындалады). Кейін main ағынында `main()` шақырылады. Код алдымен
интерпретацияланады, ыстық әдістерді JIT компиляциялайды. Қалған кластар жалқау (lazy)
түрде жүктеледі. Демон емес ағындар біткенде немесе `System.exit()` шақырылғанда shutdown
hooks іске қосылып, JVM тоқтайды.»

---

## 1. `main()`-ге дейін не жұмыс істейді?

**Жауап:** `main()` ең бірінші орындалатын код емес. Одан бұрын:
1. JVM-нің native коды (C++): жадты бөлу, GC, JIT, жүйелік ағындар.
2. **Жүздеген JDK класы** жүктеледі.
3. Main класының `static` блоктары мен static өрістерінің инициализациясы.

**Тексеру:** `java -Xlog:class+load:file=cl.txt Main`

```
[0.018s] java.lang.Object source: shared objects file      ← ең бірінші класс
...
[0.044s] sun.launcher.LauncherHelper source: shared objects file   ← 410-жол
[0.046s] Main source: file:/C:/.../jvmx/                   ← 443-жол
```

Біздің `Main` жүктелгенге дейін **442 класс** жүктеліп үлгерді, бағдарлама
соңына дейін барлығы 594 класс жүктелді. Олардың 561-і `shared objects file`-дан келді.

**`static` блок `main`-нен бұрын орындалады:**

```java
public class Main {
    static { System.out.println("0. Main static block"); }
    public static void main(String[] args) { System.out.println("main басталды"); }
}
```
```
0. Main static block
main басталды
```

Себебі: `main` static әдіс болғандықтан, оны шақыру үшін класс **инициализацияланған**
болуы тиіс.

---

## 2. Bytecode не үшін керек? Кроссплатформалылыққа қалай жетеміз?

**Жауап:** `javac` машиналық код жасамайды. Ол виртуалды машинаға арналған **bytecode**
жасайды (`.class`). Бір `.class` файлы кез келген ОЖ-да жұмыс істейді, себебі әр платформаның
өз JVM-і бар. **«Write once, run anywhere»:** кроссплатформалы болатын бағдарлама емес, JVM
әр платформаға жеке жазылған.

- **JVM** bytecode-ты орындайды (интерпретатор + JIT + GC + жады).
- **JRE** = JVM + стандарт кітапханалар. Java 11-ден бастап JRE бөлек таратылмайды.
- **JDK** = JRE + әзірлеу құралдары (`javac`, `jar`, `jcmd`, `jstack`, `javap` …).

Bytecode-ты өз көзіңмен көру: `javap -c Main.class`

---

## 3. Класс қалай жүктеледі? (Как происходит загрузка классов)

Үш кезең бар:

| Кезең | Не болады |
|---|---|
| **Loading** (загрузка) | ClassLoader `.class` байттарын табады, JVM metaspace-те класс сипаттамасын, ал heap-те `Class<?>` объектісін жасайды |
| **Linking** (связывание) | **Verification**: bytecode-тың дұрыстығы мен қауіпсіздігін тексеру. **Preparation**: static өрістерге жад бөлу және оларға **әдепкі мән** (0, null, false) беру. **Resolution**: символдық сілтемелерді нақты сілтемелерге айналдыру (көбінесе жалқау түрде) |
| **Initialization** (инициализация) | `<clinit>` орындалады, яғни static өрістерге жазылған мәндер мен `static {}` блоктары **файлдағы рет бойынша** |

**Маңызды:** preparation кезінде `static int x = 5;` өрісінің мәні **әлі 0** болады.
5 мәні тек initialization кезінде жазылады.

---

## 4. Қандай ClassLoader-лар бар? Parent delegation дегеніміз не?

| Loader | Нені жүктейді | Java-дан қалай көрінеді |
|---|---|---|
| **Bootstrap** | `java.base` ядросы (`java.lang.*` т.б.), native C++ | `null` |
| **Platform** (Java 8-де Extension) | Қалған JDK модульдері (`java.sql` т.б.) | `PlatformClassLoader` |
| **Application / System** | Сенің classpath/modulepath-ыңдағы кластар | `AppClassLoader` |

Біздің тәжірибедегі нақты нәтиже:
```
Main.class.getClassLoader()            → jdk.internal.loader.ClassLoaders$AppClassLoader
Main.class.getClassLoader().getParent() → jdk.internal.loader.ClassLoaders$PlatformClassLoader
String.class.getClassLoader()           → null   ← Bootstrap
```

**Parent delegation (делегирование родителю):** loader класты алдымен **ата-анасынан**
сұрайды. Ата-анасы таппаған жағдайда ғана өзі іздейді. Не үшін керек:
- **Қауіпсіздік:** өз `java.lang.String` класыңды жазып, ядродағысын ауыстыра алмайсың.
- **Бірегейлік:** бір класс бір рет жүктеледі.

Класс идентификациясы = **толық аты + оны жүктеген loader**. Бір `.class` файлын екі түрлі
loader жүктесе, екі **түрлі** класс пайда болады. Сондықтан кейде `ClassCastException: A cannot be cast to A`
қатесі шығады (Tomcat, плагиндер жағдайында).

---

## 5. Класс қашан инициализацияланады? (Ленивость загрузки)

**Жауап:** бірінші **белсенді қолданылған** кезде ғана:
- `new` арқылы объект құрылғанда;
- static әдіс шақырылғанда;
- константа емес static өріс оқылғанда немесе жазылғанда;
- ішкі класс (subclass) инициализацияланғанда (ата-анасы алдымен);
- рефлексия арқылы (`Class.forName`);
- main класы үшін JVM іске қосылғанда.

**Инициализация туындамайтын жағдайлар (сұхбаттағы тұзақ):**

```java
class Cfg {
    static final int CONST = 5;          // compile-time константа
    static final Integer BOXED = 7;      // константа ЕМЕС
    static { System.out.println("Cfg инициализацияланды"); }
}
// main ішінде:
System.out.println("CONST=" + Cfg.CONST);
System.out.println("массив: " + new Cfg[3].length);
System.out.println("BOXED=" + Cfg.BOXED);
```
```
CONST=5
массив: 3
Cfg инициализацияланды     ← тек BOXED оқылғанда
BOXED=7
```

- `static final` примитив немесе String литералы **compile-time константа** болып саналады.
  `javac` оның мәнін шақыратын жерге тікелей көшіріп қояды (inline), сондықтан класс керек болмайды.
- `new Cfg[3]` жасағанда тек массив құрылады, `Cfg` объектісі құрылмайды.

Бұл демо **Holder Singleton** паттернінің негізі: `static class Holder { static final X INSTANCE = new X(); }`
класы тек алғаш қолданылғанда инициализацияланады. JVM мұны синхронизацияланған түрде бір-ақ рет
жасайды, сондықтан бұл паттерн жалқау әрі потокобезопасный.

---

## 6. `static` пен `final` қандай кепілдік береді?

- **static инициализация потокобезопасна.** JVM `<clinit>`-ті бір ағында және бір-ақ рет
  орындайды. Қалған ағындар оның аяқталуын күтеді. Сондықтан static өрістерге
  синхронизациясыз қауіпсіз түрде мән беруге болады.
- **final өрістердің кепілдігі (JMM, final field semantics).** Конструктор аяқталғаннан кейін
  `final` өрістер басқа ағындарға **толық инициализацияланған күйде** көрінеді, бірақ бір шартпен:
  `this` конструктордан сыртқа «қашпауы» керек. Immutable объектілердің потокобезопасность-ы
  осыған негізделген.

---

## 7. Инициализация реті (мұрагерлікпен) — ITVDN №47

```java
class Parent {
    static { System.out.println("1. Parent static block"); }
    { System.out.println("3. Parent instance block"); }
    Parent() { System.out.println("4. Parent constructor"); }
}
class Child extends Parent {
    static { System.out.println("2. Child static block"); }
    { System.out.println("5. Child instance block"); }
    Child() { System.out.println("6. Child constructor"); }
}
new Child();  new Child();
```
```
1. Parent static block
2. Child static block
3. Parent instance block
4. Parent constructor
5. Child instance block
6. Child constructor
--- екінші new Child()
3. Parent instance block      ← static блоктар ЕНДІ ОРЫНДАЛМАЙДЫ
4. Parent constructor
5. Child instance block
6. Child constructor
```

**Ереже:** static бөліктер (ата-ана → бала) класс үшін **бір рет** орындалады. Қалғаны
әр `new` сайын қайталанады: ата-ана (instance блоктар мен өрістер → конструктор), сосын бала
(дәл сол рет). Бір кезеңнің ішінде өрістер мен блоктар **файлда жазылған рет бойынша** орындалады.

**Тұзақ:** ата-ана конструкторы overridable әдісті шақырса, баладағы переопределённый нұсқа
баланың өрістері **әлі инициализацияланбаған** кезде орындалады (0 немесе null көрінеді).

---

## 8. `new` жазғанда не болады? (Что происходит, когда мы пишем new)

1. Класс жүктелмеген болса: loading → linking → initialization (жоғарыдағыдай).
2. Heap-те жад бөлінеді. Әдетте бұл ағынның жеке **TLAB** (Thread-Local Allocation Buffer)
   аймағында болады, ол жай ғана көрсеткішті жылжытумен шектеледі, сондықтан өте жылдам.
3. Жад **нөлмен толтырылады**. Сондықтан өрістердің әдепкі мәндері 0, null, false болады.
4. **Объект тақырыбы (header)** жазылады: mark word және class pointer.
5. `<init>` шақырылады: `super(...)` тізбегі → өрістер мен instance блоктар → конструктор денесі.
6. Сілтеме стекке қайтарылады (айнымалыға меншіктеледі).

Bytecode-та бұл былай көрінеді (`javap -c`):
`new #7` → `dup` → `invokespecial <init>` → `astore_1`

**Escape analysis:** JIT объектінің әдістен «қашпайтынын» дәлелдесе, оны heap-ке мүлде
орналастырмауы мүмкін (scalar replacement).

---

## 9. JVM-де қандай жады аймақтары бар? (Области памяти / Run-Time Data Areas)

| Аймақ | Кімдікі | Не сақталады | Толса не болады |
|---|---|---|---|
| **Heap** | Барлық ағындарға ортақ | Барлық объектілер мен массивтер, string pool (Java 7+). GC осында жұмыс істейді: Young (Eden, Survivor) және Old | `OutOfMemoryError: Java heap space` |
| **Metaspace** (Java 8-ге дейін PermGen) | Ортақ, native жадта орналасқан | Класс метадеректері, әдістердің bytecode-ы, runtime constant pool | `OutOfMemoryError: Metaspace` |
| **Stack** | **Әр ағынның өзінікі** | Frame-дер: локал айнымалылар (примитивтер мен **сілтемелер**), operand stack | `StackOverflowError` |
| **PC register** | Әр ағынның өзінікі | Қазір орындалып жатқан инструкцияның адресі | — |
| **Native method stack** | Әр ағынның өзінікі | JNI/native әдістер | — |
| **Code cache** | Ортақ | JIT компиляциялаған native код | JIT тоқтайды, жұмыс баяулайды |

**Примитивтер қайда сақталады?** Локал айнымалы болса stack-те. Объектінің өрісі болса
**heap-те**, объектінің ішінде. Демек «примитивтер әрқашан stack-те» деген тұжырым **қате**.

**Heap-тің әдепкі өлшемі** (менің машинамда өлшеніп алынды):
```
InitialHeapSize = 536870912   (512 MB  = RAM-ның 1/64 бөлігі)
MaxHeapSize     = 8547991552  (~8 GB   = RAM-ның 1/4 бөлігі)
UseG1GC         = true        (Java 9+ әдепкі GC)
```
Бұл мәндерді `-Xms` / `-Xmx` арқылы өзгертуге болады.
Тексеру командасы: `java -XX:+PrintFlagsFinal -version | grep HeapSize`

---

## 10. Объект жадта қалай сақталады? (Структура объекта, заголовки)

64-bit JVM-де, compressed pointers қосулы болғанда (әдепкі күй):

```
┌──────────────────────┬────────────────────┬──────────────┬─────────┐
│ mark word (8 байт)   │ class pointer (4)  │ өрістер …    │ padding │
└──────────────────────┴────────────────────┴──────────────┴─────────┘
  identity hashCode,     объектінің класын      өрістердің       объект өлшемі
  GC жасы (age),         (metaspace-тегі)        мәндері          8-ге еселі
  lock күйі              көрсетеді                               болатындай толтыру
```

- Бос `new Object()` **16 байт** алады (12 байт тақырып + 4 байт padding).
- Массивте тақырыпқа тағы **length (4 байт)** қосылады.
- `synchronized` құлыбы туралы ақпарат **mark word**-те сақталады.
  Бұл Boosty-дегі «Где хранится информация о блокировке synchronized» сұрағының жауабы.

Өлшеп көру үшін **JOL** (Java Object Layout) кітапханасын қолдануға болады.

---

## 11. JIT деген не? Код қалай жылдамдайды?

JVM **mixed mode** режимінде жұмыс істейді (`java -version` шығысындағы «mixed mode» жазуы осы):

1. Кез келген әдіс алдымен **интерпретатор** арқылы орындалады. Ол бірден іске қосылады, бірақ баяу жұмыс істейді.
2. JVM әдістердің шақырылу санын және циклдердің қайталану санын есептейді.
3. «Ыстық» әдіс **C1** компиляторына түседі: жылдам компиляция, орташа оптимизация.
4. Одан да ыстық болса, профиль деректерімен бірге **C2**-ге түседі: агрессивті оптимизация.
   Мұны **tiered compilation** деп атайды (`TieredCompilation = true`).
5. Негізгі оптимизациялар: **inlining** (ең маңыздысы), escape analysis, loop unrolling,
   dead code elimination, lock elision/coarsening.
6. **Deoptimization:** JIT-тің болжамы бұзылса (мысалы, жаңа ішкі класс жүктелсе),
   код қайтадан интерпретаторға оралады.

Қорытынды: Java-қосымша **іске қосылғаннан кейін бірден емес, біраз «қызғаннан» (warm-up)
кейін** жылдам жұмыс істейді. Бенчмарк жасағанда warm-up кезеңін міндетті түрде өткізу керек (JMH).

---

## 12. Қосымша іске қосылғанда қандай ағындар жұмыс істейді?

`main` ішінде `Thread.getAllStackTraces()` арқылы алынған нәтиже:
```
thread: main               daemon=false   ← сенің кодың
thread: Reference Handler  daemon=true    ← Soft/Weak/Phantom сілтемелерін кезекке қояды
thread: Finalizer          daemon=true    ← finalize() әдістерін орындайды (deprecated)
thread: Signal Dispatcher  daemon=true    ← ОЖ сигналдарын өңдейді (Ctrl+C т.б.)
thread: Attach Listener    daemon=true    ← jcmd/jstack/VisualVM қосылуы үшін
thread: Common-Cleaner     daemon=true    ← Cleaner API
thread: Notification Thread daemon=true   ← JMX хабарламалары
```
Бұлар тек Java ағындары. Бұлардан басқа Java-дан **көрінбейтін** JVM ағындары да бар:
GC ағындары, JIT компилятор ағындары (C1/C2), VM Thread. Олардың бәрін `jstack <pid>` немесе
`jcmd <pid> Thread.print` арқылы көруге болады.

---

## 13. Қосымша қашан және қалай аяқталады?

- **Демон емес** соңғы ағын аяқталғанда (көбінесе ол `main` болады). Демон ағындар (GC т.б.)
  JVM-ді ұстап тұрмайды.
  → Сондықтан `main` біткенімен, `ExecutorService` жабылмаса, қосымша **тоқтамай қалады**:
  пулдың ағындары демон емес.
- `System.exit(code)` шақырылғанда.
- ОЖ сигналы келгенде: Ctrl+C / SIGTERM болса, hooks орындалады. `kill -9` болса,
  **ешқандай hook орындалмайды**.
- `main`-нен ұсталмаған (uncaught) exception шықса, main ағыны өледі. Демон емес басқа
  ағындар болмаса, JVM exit code 1 қайтарып тоқтайды.

**Shutdown hook:** `Runtime.getRuntime().addShutdownHook(new Thread(...))`.
Тәжірибеде ол `main` біткеннен кейін, ең соңында орындалды (жоғарыдағы шығыстағы `shutdown hook` жолы).
Қолданылуы: ресурстарды жабу, graceful shutdown. Spring те осы механизмді пайдаланады.

---

## 14. Неге Java 17 жылдам іске қосылады? CDS және GraalVM

- **CDS (Class Data Sharing):** ядро кластары бұрыннан талданып (parse), дайын архивте сақталған.
  `java -version` шығысындағы **«sharing»** сөзі осыны білдіреді. Біздің тәжірибеде
  594 кластың 561-і архивтен келді. Өз қосымшаң үшін де **AppCDS** жасауға болады.
- **GraalVM Native Image (AOT):** кодты алдын ала native бинарға компиляциялайды. Іске қосылу
  миллисекундпен өлшенеді, жады аз жұмсалады. Бағасы: рефлексияға шектеу қойылады, JIT-тің
  профильге негізделген оптимизациясы жоқ, build ұзақ жүреді. Бұл Boosty-дегі «Зачем нужен GraalVM» сұрағының жауабы.

---

## Өзіңді тексер (жауап бере алмасаң, жоғарыдан қайта оқы)

1. `main()`-ге дейін қандай код орындалады? Main-ге дейін шамамен қанша класс жүктеледі?
2. Loading, linking және initialization кезеңдерінің айырмашылығы неде? Preparation кезінде static өріс қандай мән алады?
3. `String.class.getClassLoader()` не қайтарады және неге?
4. Parent delegation не үшін керек? Өз `java.lang.String` класыңды жүктей аласың ба?
5. `Cfg.CONST` оқылғанда `Cfg` класы инициализацияланады ма? `Cfg.BOXED` оқылғанда ше?
6. Parent/Child мысалында екінші `new Child()` не басып шығарады?
7. `new` операциясының 6 қадамын ата. TLAB деген не?
8. Объект өрісі болып тұрған `int` қайда сақталады?
9. `new Object()` қанша байт алады? Mark word-те не сақталады?
10. Неге Java бенчмаркінде warm-up керек?
11. `main` бітті, бірақ процесс тоқтамай тұр. Ықтимал себебі қандай?
12. Қай жағдайда shutdown hook орындалмайды?

## Өзің жүргізіп көретін тәжірибелер

```powershell
java -Xlog:class+load Main            # қай кластың қашан жүктелгенін көру
java -verbose:class Main              # осының қысқа нұсқасы
javap -c Main.class                   # bytecode-ты көру (new/dup/invokespecial)
java -XX:+PrintFlagsFinal -version    # JVM-нің барлық параметрлері
jcmd                                  # жұмыс істеп тұрған JVM-дердің тізімі
jcmd <pid> Thread.print               # барлық ағындар, JVM-нің ішкі ағындарымен бірге
java -Xshare:off Main                 # CDS-сіз іске қосу (уақытын салыстырып көр)
```
