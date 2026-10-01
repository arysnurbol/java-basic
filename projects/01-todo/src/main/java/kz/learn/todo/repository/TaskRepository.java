package kz.learn.todo.repository;

import kz.learn.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Тапсырмаларды сақтау орны. ДАЙЫН — тек келісімшарт (contract).
 *
 * Неге интерфейс: TaskService "қалай сақталатынын" білмейді, тек "не істей алатынын" біледі.
 * Бүгін — жадта (InMemoryTaskRepository), ертең — файлда немесе дерекқорда (JDBC/JPA),
 * ал TaskService бір жолы да өзгермейді. Бұл — Spring-тегі Repository-дің дәл өзі.
 */
public interface TaskRepository {

    /** Сақтайды. Сол id-мен тапсырма бұрын болса — ауыстырады. Сақталған тапсырманы қайтарады. */
    Task save(Task task);

    /** Табылмаса — Optional.empty(), ешқашан null емес. */
    Optional<Task> findById(long id);

    /** Барлығы, ҚОСЫЛҒАН РЕТІМЕН. Қайтқан тізімді өзгерту репозиторийге әсер етпеуі керек. */
    List<Task> findAll();

    /** Өшірілсе — true, ондай id жоқ болса — false. */
    boolean deleteById(long id);
}
