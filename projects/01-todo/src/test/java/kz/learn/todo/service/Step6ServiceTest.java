package kz.learn.todo.service;

import kz.learn.todo.exception.InvalidTaskException;
import kz.learn.todo.exception.TaskNotFoundException;
import kz.learn.todo.model.DeadlineTask;
import kz.learn.todo.model.Priority;
import kz.learn.todo.model.SimpleTask;
import kz.learn.todo.model.Status;
import kz.learn.todo.model.Task;
import kz.learn.todo.repository.InMemoryTaskRepository;
import kz.learn.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 6 — TaskService: композиция және бизнес-логика")
class Step6ServiceTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 1);

    private TaskRepository repo;
    private TaskService service;

    @BeforeEach
    void setUp() {
        repo = new InMemoryTaskRepository();
        service = new TaskService(repo);
    }

    private static List<Long> ids(List<Task> tasks) {
        return tasks.stream().map(Task::getId).toList();
    }

    @Test
    @DisplayName("add*: id 1-ден бастап өседі, тип дұрыс, репозиторийге сақталады")
    void add() {
        Task a = service.addSimple("A", Priority.LOW);
        Task b = service.addWithDeadline("B", Priority.HIGH, TODAY);

        assertEquals(1, a.getId());
        assertEquals(2, b.getId());
        assertInstanceOf(SimpleTask.class, a);
        assertInstanceOf(DeadlineTask.class, b);
        assertEquals(2, repo.findAll().size(), "сервис репозиторийді қолдануы керек");
    }

    @Test
    @DisplayName("өшірілген id қайта берілмейді")
    void idsAreNotReused() {
        service.addSimple("A", Priority.LOW);
        service.addSimple("B", Priority.LOW);
        service.delete(2);

        assertEquals(3, service.addSimple("C", Priority.LOW).getId());
    }

    @Test
    @DisplayName("қате дерек -> InvalidTaskException сервистен де өтеді")
    void invalidData() {
        assertThrows(InvalidTaskException.class, () -> service.addSimple(" ", Priority.LOW));
        assertThrows(InvalidTaskException.class, () -> service.addWithDeadline("X", Priority.LOW, null));
    }

    @Test
    @DisplayName("getById / getAll")
    void get() {
        service.addSimple("A", Priority.LOW);
        service.addSimple("B", Priority.LOW);

        assertEquals("B", service.getById(2).getTitle());
        assertEquals(List.of(1L, 2L), ids(service.getAll()));

        TaskNotFoundException e = assertThrows(TaskNotFoundException.class, () -> service.getById(99));
        assertEquals(99, e.getTaskId());
    }

    @Test
    @DisplayName("start / complete id бойынша")
    void startComplete() {
        service.addSimple("A", Priority.LOW);

        service.start(1);
        assertEquals(Status.IN_PROGRESS, service.getById(1).getStatus());
        service.complete(1);
        assertEquals(Status.DONE, service.getById(1).getStatus());

        assertThrows(InvalidTaskException.class, () -> service.start(1));
        assertThrows(TaskNotFoundException.class, () -> service.start(42));
        assertThrows(TaskNotFoundException.class, () -> service.complete(42));
    }

    @Test
    @DisplayName("delete: бар болса өшіреді, жоқ болса TaskNotFoundException")
    void delete() {
        service.addSimple("A", Priority.LOW);

        service.delete(1);
        assertTrue(service.getAll().isEmpty());
        assertThrows(TaskNotFoundException.class, () -> service.delete(1));
    }

    @Test
    @DisplayName("findByStatus")
    void byStatus() {
        service.addSimple("A", Priority.LOW);
        service.addSimple("B", Priority.LOW);
        service.addSimple("C", Priority.LOW);
        service.start(1);
        service.start(3);
        service.complete(3);

        assertEquals(List.of(2L), ids(service.findByStatus(Status.TODO)));
        assertEquals(List.of(1L), ids(service.findByStatus(Status.IN_PROGRESS)));
        assertEquals(List.of(3L), ids(service.findByStatus(Status.DONE)));
    }

    @Test
    @DisplayName("findOverdue: тек мерзімі өткен әрі аяқталмағандар")
    void overdue() {
        service.addSimple("simple", Priority.LOW);                              // 1 — мерзімі жоқ
        service.addWithDeadline("late", Priority.LOW, TODAY.minusDays(1));      // 2 — кешікті
        service.addWithDeadline("today", Priority.LOW, TODAY);                  // 3 — әлі емес
        service.addWithDeadline("late but done", Priority.LOW, TODAY.minusDays(5)); // 4
        service.start(4);
        service.complete(4);
        service.addWithDeadline("late too", Priority.LOW, TODAY.minusDays(3));  // 5 — кешікті

        assertEquals(List.of(2L, 5L), ids(service.findOverdue(TODAY)));
    }

    @Test
    @DisplayName("sortedByPriority: HIGH -> LOW, тең болса id бойынша")
    void sorted() {
        service.addSimple("a", Priority.LOW);     // 1
        service.addSimple("b", Priority.HIGH);    // 2
        service.addSimple("c", Priority.MEDIUM);  // 3
        service.addSimple("d", Priority.HIGH);    // 4
        service.addSimple("e", Priority.LOW);     // 5

        assertEquals(List.of(2L, 4L, 3L, 1L, 5L), ids(service.sortedByPriority()));
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L), ids(service.getAll()), "getAll реті өзгермеуі керек");
    }
}
