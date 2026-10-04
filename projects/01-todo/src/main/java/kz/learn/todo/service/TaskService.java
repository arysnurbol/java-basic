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

    private final TaskRepository taskRepository;
    private long currentId = 1;

    public TaskService(TaskRepository repository) {
        this.taskRepository = repository;
    }

    /** Жаңа SimpleTask жасап, сақтап, қайтарады. */
    public Task addSimple(String title, Priority priority) {
        SimpleTask simpleTask = new SimpleTask(currentId++, title, priority);
        return taskRepository.save(simpleTask);
    }

    /** Жаңа DeadlineTask жасап, сақтап, қайтарады. */
    public Task addWithDeadline(String title, Priority priority, LocalDate dueDate) {
        DeadlineTask deadlineTask = new DeadlineTask(currentId++, title, priority, dueDate);
        return taskRepository.save(deadlineTask);
    }

    /** Табылмаса — TaskNotFoundException. Кеңес: Optional.orElseThrow(...) */
    public Task getById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /** Қосылған ретімен барлығы. */
    public List<Task> getAll() {
        return taskRepository.findAll()
                .stream()
                .toList();
    }

    public void start(long id) {
        Task localTask = getById(id);
        localTask.start();
        taskRepository.save(localTask);
    }

    public void complete(long id) {
        Task localTask = getById(id);
        localTask.complete();
        taskRepository.save(localTask);
    }

    /** Табылмаса — TaskNotFoundException. */
    public void delete(long id) {
        getById(id);
        taskRepository.deleteById(id);
    }

    /** Берілген күйдегілер, қосылған ретімен. */
    public List<Task> findByStatus(Status status) {
        return getAll().stream().filter(task -> task.getStatus() == status).toList();
    }

    /** today күні кешіккендер, қосылған ретімен. */
    public List<Task> findOverdue(LocalDate today) {
        return getAll().stream().filter(task -> task.isOverdue(today)).toList();
    }

    /**
     * Маңыздылығы бойынша: алдымен HIGH, соңында LOW (Priority.getWeight() қолдан).
     * Маңыздылығы тең болса — id бойынша өсу ретімен.
     * Кеңес: Comparator.comparing(...).reversed().thenComparing(...)
     */
    public List<Task> sortedByPriority() {
        return getAll().stream()
                .sorted(
                        Comparator.comparing(
                                (Task task) -> task.getPriority().getWeight()).reversed()
                                .thenComparing(Task::getId)).toList();
    }
}
