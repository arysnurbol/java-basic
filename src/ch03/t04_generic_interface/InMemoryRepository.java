package ch03.t04_generic_interface;


import  java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Тапсырма 04 — обобщенный интерфейстің жадыдағы іске асуы.
 *
 * Ішінде LinkedHashMap<ID, T> ұста: ол қосылу ретін сақтайды,
 * сондықтан findAll() болжамды нәтиже береді.
 *
 * ТИПТЕРДІ ӨШІРУ (стирание типов): компиляциядан кейін T де, ID де жоғалып,
 * орнына Object қалады. Сондықтан:
 *   - new T() деп жазу мүмкін емес;
 *   - item instanceof T деп тексеру мүмкін емес;
 *   - InMemoryRepository<String,Integer> мен InMemoryRepository<Integer,String>
 *     орындалу кезінде БІР ҒАНА класс. Тексеріс дәл соны көрсетеді.
 */
public class InMemoryRepository<T, ID> implements Repository<T, ID> {

    private final LinkedHashMap<ID, T> linkedHashMap = new LinkedHashMap<>();

    @Override
    public T save(ID id, T item) {
        linkedHashMap.put(id, item);
        return item;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(linkedHashMap.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(linkedHashMap.values());
    }

    @Override
    public boolean deleteById(ID id) {
        if  (linkedHashMap.containsKey(id)) {
            linkedHashMap.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public int count() {
        return linkedHashMap.size();
    }
}
