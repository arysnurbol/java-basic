package ch03.t04_generic_interface;

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

    // TODO: LinkedHashMap<ID, T> өрісі (final)

    @Override
    public T save(ID id, T item) {
        return null; // TODO
    }

    @Override
    public Optional<T> findById(ID id) {
        return null; // TODO
    }

    @Override
    public List<T> findAll() {
        return null; // TODO
    }

    @Override
    public boolean deleteById(ID id) {
        return false; // TODO
    }

    @Override
    public int count() {
        return 0; // TODO
    }
}
