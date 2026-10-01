package kz.learn.todo.service;

import kz.learn.todo.exception.TaskNotFoundException;
import kz.learn.todo.model.DeadlineTask;
import kz.learn.todo.model.Priority;
import kz.learn.todo.model.SimpleTask;
import kz.learn.todo.model.Status;
import kz.learn.todo.model.Task;
import kz.learn.todo.repository.TaskRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Бизнес-логика. Тапсырмаларды қалай сақтау керегін білмейді — оны TaskRepository-ге тапсырады.
 *
 * ООП:
 *  - композиция: TaskService HAS-A TaskRepository (мұрагерлік емес!);
 *  - тәуелділікті конструктор арқылы алу (Dependency Injection) — Spring дәл осылай істейді;
 *  - полиморфизм: findOverdue() тапсырманың нақты типін білмейді, тек task.isOverdue() шақырады.
 *
 * id-ды сервис береді: 1, 2, 3, ... (өшірілген id қайта берілмейді).
 * Өзгерту жасайтын методтар табылмаған id үшін TaskNotFoundException лақтырады.
 */
public class TaskService {

    // TODO: өрістерді жаз

    public TaskService(TaskRepository repository) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жаңа SimpleTask жасап, сақтап, қайтарады. */
    public Task addSimple(String title, Priority priority) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Жаңа DeadlineTask жасап, сақтап, қайтарады. */
    public Task addWithDeadline(String title, Priority priority, LocalDate dueDate) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — TaskNotFoundException. Кеңес: Optional.orElseThrow(...) */
    public Task getById(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Қосылған ретімен барлығы. */
    public List<Task> getAll() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public void start(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public void complete(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — TaskNotFoundException. */
    public void delete(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Берілген күйдегілер, қосылған ретімен. */
    public List<Task> findByStatus(Status status) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** today күні кешіккендер, қосылған ретімен. */
    public List<Task> findOverdue(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Маңыздылығы бойынша: алдымен HIGH, соңында LOW (Priority.getWeight() қолдан).
     * Маңыздылығы тең болса — id бойынша өсу ретімен.
     * Кеңес: Comparator.comparing(...).reversed().thenComparing(...)
     */
    public List<Task> sortedByPriority() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
