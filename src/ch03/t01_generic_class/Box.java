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

    private T value;

    /** Бос жәшік. */
    public Box() {
        this.value = null;
    }

    /** Мәні бар жәшік. */
    public Box(T value) {
        this.value = value;
    }

    /**
     * Статикалық фабрика. Мұндағы алғашқы <T> — МЕТОДТЫҢ өз тип параметрі:
     * static метод класстың T-сын көре алмайды, сондықтан өзінікін жариялайды.
     */
    public static <T> Box<T> of(T value) {
        return new Box<>(value);
    }

    /** Ішіндегі мән (бос болса — null). */
    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    /** Ішінде мән жоқ па. */
    public boolean isEmpty() {
        return value == null;
    }

    /** Мән бар болса сол, болмаса fallback. */
    public T getOrDefault(T fallback) {

        if (isEmpty()) return fallback;
        return get();
    }

    /** "Box[hello]" немесе бос болса "Box[empty]". */
    public String describe() {
        if (value == null) {
            return "Box[empty]";
        }

        return "Box[" + value + "]";
    }
}
