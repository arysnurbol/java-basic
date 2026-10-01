package kz.learn.todo.repository;

import kz.learn.todo.model.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TaskRepository-дің жадтағы жүзеге асырылуы.
 *
 * Кеңес: Map<Long, Task> қолдан. Қай Map қосылу ретін сақтайды — HashMap па, LinkedHashMap па?
 * findAll() ішкі коллекцияны тікелей ҚАЙТАРМАСЫН — көшірмесін қайтарсын (инкапсуляция).
 */
public class InMemoryTaskRepository implements TaskRepository {

    // TODO: өрістерді жаз

    @Override
    public Task save(Task task) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<Task> findById(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<Task> findAll() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean deleteById(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
