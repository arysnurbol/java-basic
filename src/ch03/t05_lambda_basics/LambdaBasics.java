package ch03.t05_lambda_basics;

import java.util.Comparator;
import java.util.List;

/**
 * Тапсырма 05 — Лямбда-выражения.
 * Кітап: "Понимание/Синтаксис/Использование лямбда-выражений" (121–122 б.)
 *
 * ҮШ СИНТАКСИС ТҮРІ:
 *     (a, b) -> a.length() - b.length()      // бір өрнек, return жазылмайды
 *     s -> s.isEmpty()                       // бір параметр — жақша қажет емес
 *     (a, b) -> { ... return x; }            // блок — return МІНДЕТТІ
 *
 * Лямбда — бұл "аты жоқ метод". Ол тек ФУНКЦИОНАЛДЫҚ интерфейстің орнына жазылады,
 * яғни ішінде дерексіз методы ЖАЛҒЫЗ интерфейстің (Comparator, Runnable, ...).
 *
 * ТҰЗАҚ: лямбда сыртқы айнымалыны тек ол "нақты финалды" (effectively final) болса
 * ғана ұстай алады — яғни жарияланғаннан кейін мәні өзгермесе.
 * Сондықтан санақ үшін int-ті емес, StringBuilder/массив/AtomicInteger-ді ұстайды.
 */
public class LambdaBasics {

    /**
     * БҰЛ ЖОЛДЫ ӨЗГЕРТПЕ — лямбдаға дейінгі стиль, салыстыру үшін тұр.
     * Java 8-ге дейін әр Comparator осылай жазылатын: 5 жол бойы бір ғана логика.
     */
    public static final Comparator<String> LEGACY_BY_LENGTH = new Comparator<String>() {
        @Override
        public int compare(String a, String b) {
            return Integer.compare(a.length(), b.length());
        }
    };

    /**
     * Жоғарыдағы анонимді кластың дәл сол логикасы, бірақ ЛЯМБДА түрінде.
     * Бір жолға сыюы керек.
     */
    public static Comparator<String> byLength() {
        return (a, b) -> Integer.compare(a.length(), b.length());
    }

    /**
     * Алдымен ұзындығы бойынша, ұзындығы тең болса — әліпби бойынша.
     * Кеңес: Comparator.comparingInt(...).thenComparing(...) тізбегі.
     */
    public static Comparator<String> byLengthThenAlpha() {
        return Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());
    }

    /**
     * ЖАҢА реттелген тізім қайтарады (кірісті өзгертпейді!).
     * Реті — byLengthThenAlpha() бойынша.
     */
    public static List<String> sortedByLength(List<String> names) {
        return names.stream().sorted(byLengthThenAlpha()).toList();
    }

    /** Кему реті бойынша ЖАҢА тізім. Кеңес: Comparator.reverseOrder(). */
    public static List<String> sortedDescending(List<String> names) {
        return names.stream().sorted(Comparator.reverseOrder()).toList();
    }

    /**
     * Тізімнің ӨЗІН орнында реттейді (жаңа тізім жасамайды).
     * Кеңес: List.sort(Comparator).
     */
    public static void sortInPlaceByLength(List<String> names) {
        names.sort(byLength());
    }

    /**
     * Шақырылғанда sink-ке "Salem, <name>!" жазатын Runnable қайтарады.
     * Бұл — лямбданың сыртқы айнымалыны ҰСТАУЫ (замыкание / closure).
     * Назар аудар: Runnable ҚАЙТАРЫЛАДЫ, бірден орындалмайды.
     */
    public static Runnable greeter(StringBuilder sink, String name) {
        return () -> sink.append("Salem, ").append(name).append("!");
    }
}
