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

    private final Map<Long, Task> map = new LinkedHashMap<>();

    @Override
    public Task save(Task task) {
        map.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean deleteById(long id) {
        return map.remove(id) != null;
    }
}
