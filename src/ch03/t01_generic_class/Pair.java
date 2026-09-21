package ch03.t01_generic_class;

/**
 * Тапсырма 01 — екі тип параметрі бар класс.
 *
 * Pair<K, V> — кілт пен мәннің өзгермейтін (immutable) жұбы.
 * Екі параметр де бірден-бір ерекшелігі жоқ — жай атаулар, бірақ келісім бойынша
 * K = Key, V = Value, T = Type, E = Element, R = Result деп аталады.
 */
public class Pair<K, V> {

    // TODO: екі final өріс — key (K) және value (V)

    public Pair(K key, V value) {
        // TODO
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return null; // TODO
    }

    public K getKey() {
        return null; // TODO
    }

    public V getValue() {
        return null; // TODO
    }

    /**
     * Орнын ауыстырған ЖАҢА жұп: Pair<K,V> -> Pair<V,K>.
     * Назар аудар: қайтарылатын тип те ауысады — генериктің күші дәл осында.
     */
    public Pair<V, K> swap() {
        return null; // TODO
    }

    /** "(Aisha, 25)" пішімі. */
    @Override
    public String toString() {
        return null; // TODO
    }
}
