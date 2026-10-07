package kz.learn.shop.repository;

import kz.learn.shop.model.Identifiable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * GENERIC репозиторий. ДАЙЫН — 02-library-да сен жазған код, 03-bank пен 04-hotel арқылы өзгеріссіз келді.
 * Бір рет дұрыс жазылған generic абстракция келесі жобада қайта жазылмайды — қайта қолданылады.
 */
public interface Repository<T extends Identifiable<ID>, ID> {

    /** Сақтайды (сол id бұрын болса — ауыстырады). */
    T save(T entity);

    /** Табылмаса — Optional.empty(), ешқашан null емес. */
    Optional<T> findById(ID id);

    /** Барлығы, қосылған ретімен. Қайтқан тізімді өзгерту репозиторийге әсер етпейді. */
    List<T> findAll();

    /** Өшірілсе — true, ондай id жоқ болса — false. */
    boolean deleteById(ID id);

    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    default List<T> findWhere(Predicate<? super T> condition) {
        return findAll().stream().filter(condition).toList();
    }
}
