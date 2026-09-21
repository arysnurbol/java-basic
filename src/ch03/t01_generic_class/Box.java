package ch03.t01_generic_class;

/**
 * Тапсырма 01 — Обобщенный класс (обобщения / generics).
 * Кітап: "Обобщенные классы" (116–117 б.)
 *
 * <T> — ТИП ПАРАМЕТРІ. Ол нақты класс емес, орын иеленуші (placeholder).
 * Box<String> деп жазғанда компилятор T-ның орнына String қояды да,
 * get() бірден String қайтарады — ешқандай (String) cast керек емес.
 *
 * Генериксіз нұсқаны есіңе сақта (салыстыру үшін):
 *
 *     class RawBox { private Object value; Object get() { return value; } }
 *     String s = (String) rawBox.get();   // cast керек, әрі ClassCastException қаупі бар
 */
public class Box<T> {

    // TODO: жалғыз өріс — T типті value

    /** Бос жәшік. */
    public Box() {
        // TODO
    }

    /** Мәні бар жәшік. */
    public Box(T value) {
        // TODO
    }

    /**
     * Статикалық фабрика. Мұндағы алғашқы <T> — МЕТОДТЫҢ өз тип параметрі:
     * static метод класстың T-сын көре алмайды, сондықтан өзінікін жариялайды.
     */
    public static <T> Box<T> of(T value) {
        return null; // TODO
    }

    /** Ішіндегі мән (бос болса — null). */
    public T get() {
        return null; // TODO
    }

    public void set(T value) {
        // TODO
    }

    /** Ішінде мән жоқ па. */
    public boolean isEmpty() {
        return false; // TODO
    }

    /** Мән бар болса сол, болмаса fallback. */
    public T getOrDefault(T fallback) {
        return null; // TODO
    }

    /** "Box[hello]" немесе бос болса "Box[empty]". */
    public String describe() {
        return null; // TODO
    }
}
