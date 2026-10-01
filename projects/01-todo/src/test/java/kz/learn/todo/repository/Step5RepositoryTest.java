package kz.learn.todo.repository;

import kz.learn.todo.model.Priority;
import kz.learn.todo.model.SimpleTask;
import kz.learn.todo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 5 — InMemoryTaskRepository: интерфейсті жүзеге асыру")
class Step5RepositoryTest {

    private TaskRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
    }

    private Task task(long id, String title) {
        return new SimpleTask(id, title, Priority.LOW);
    }

    @Test
    @DisplayName("бос репозиторий")
    void empty() {
        assertTrue(repo.findAll().isEmpty());
        assertTrue(repo.findById(1).isEmpty());
        assertFalse(repo.deleteById(1));
    }

    @Test
    @DisplayName("save -> findById")
    void saveAndFind() {
        Task t = task(1, "A");

        assertSame(t, repo.save(t));
        assertSame(t, repo.findById(1).orElseThrow());
        assertTrue(repo.findById(2).isEmpty());
    }

    @Test
    @DisplayName("findAll қосылған ретімен")
    void insertionOrder() {
        repo.save(task(30, "C"));
        repo.save(task(10, "A"));
        repo.save(task(20, "B"));

        List<Long> ids = repo.findAll().stream().map(Task::getId).toList();
        assertEquals(List.of(30L, 10L, 20L), ids);
    }

    @Test
    @DisplayName("сол id-мен қайта save — ауыстырады, дубликат жоқ")
    void saveReplaces() {
        repo.save(task(1, "Old"));
        repo.save(task(1, "New"));

        assertEquals(1, repo.findAll().size());
        assertEquals("New", repo.findById(1).orElseThrow().getTitle());
    }

    @Test
    @DisplayName("deleteById")
    void delete() {
        repo.save(task(1, "A"));

        assertTrue(repo.deleteById(1));
        assertTrue(repo.findById(1).isEmpty());
        assertFalse(repo.deleteById(1));
    }

    @Test
    @DisplayName("findAll көшірме қайтарады — сырттан өзгерту репозиторийді бұзбайды")
    void defensiveCopy() {
        repo.save(task(1, "A"));

        try {
            repo.findAll().clear();
        } catch (UnsupportedOperationException ignored) {
            // өзгермейтін тізім қайтару да дұрыс шешім
        }
        assertEquals(1, repo.findAll().size());
    }
}
