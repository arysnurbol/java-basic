package kz.learn.library.repository;

import kz.learn.library.model.Identifiable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository-дің жадтағы GENERIC жүзеге асырылуы. Бір класс — үш сущность:
 *   new InMemoryRepository<Item, Long>()
 *   new InMemoryRepository<Member, String>()
 *   new InMemoryRepository<Loan, Long>()
 *
 * Кеңес: Map<ID, T> (ToDo-дағыдай — қай Map қосылу ретін сақтайды?).
 * Класс ішінде T мен ID — нақты тип емес, сондықтан new T() деп жаза алмайсың (type erasure).
 */
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
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        }
        return false;
    }
}
