package kz.learn.bank.repository;

import kz.learn.bank.model.Identifiable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Repository-дің жадтағы жүзеге асырылуы. ДАЙЫН — 02-library-дан өзгеріссіз. */
public class InMemoryRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID> {

    private final Map<ID, T> map = new LinkedHashMap<>();

    @Override
    public T save(T entity) {
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean deleteById(ID id) {
        return map.remove(id) != null;
    }
}
