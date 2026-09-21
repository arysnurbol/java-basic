package ch03.t01_generic_class;

/**
 * Тапсырма 01 — екі тип параметрі бар класс.
 *
 * Pair<K, V> — кілт пен мәннің өзгермейтін (immutable) жұбы.
 * Екі параметр де бірден-бір ерекшелігі жоқ — жай атаулар, бірақ келісім бойынша
 * K = Key, V = Value, T = Type, E = Element, R = Result деп аталады.
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    /**
     * Орнын ауыстырған ЖАҢА жұп: Pair<K,V> -> Pair<V,K>.
     * Назар аудар: қайтарылатын тип те ауысады — генериктің күші дәл осында.
     */
    public Pair<V, K> swap() {
        return new Pair<>(value, key);
    }

    /** "(Aisha, 25)" пішімі. */
    @Override
    public String toString() {
        return  "(" + key + ", " + value + ")";
    }
}
