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

    // TODO: өрістерді жаз

    @Override
    public T save(T entity) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<T> findById(ID id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<T> findAll() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean deleteById(ID id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
