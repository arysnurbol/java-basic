package kz.learn.library.repository;

import kz.learn.library.model.Identifiable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Кез келген сущностьқа арналған GENERIC репозиторий.
 *
 * ToDo-да TaskRepository тек Task-пен жұмыс істеді. Мұнда үш сущность бар (Item, Member, Loan) —
 * үш бөлек интерфейс жазудың орнына біреуін типтік параметрлермен жазамыз:
 *   T  — сақталатын тип, міндетті түрде Identifiable<ID> (шекті тип, bounded type);
 *   ID — оның id-ының типі (Long немесе String).
 * Spring Data-дағы JpaRepository<T, ID> дәл осылай құрылған.
 *
 * abstract методтар — ДАЙЫН келісімшарт. default методтарды СЕН жазасың.
 */
public interface Repository<T extends Identifiable<ID>, ID> {

    /** Сақтайды (сол id бұрын болса — ауыстырады). id-ны entity.getId()-ден ал. */
    T save(T entity);

    /** Табылмаса — Optional.empty(), ешқашан null емес. */
    Optional<T> findById(ID id);

    /** Барлығы, ҚОСЫЛҒАН РЕТІМЕН. Қайтқан тізімді өзгерту репозиторийге әсер етпеуі керек. */
    List<T> findAll();

    /** Өшірілсе — true, ондай id жоқ болса — false. */
    boolean deleteById(ID id);

    /**
     * default метод: интерфейстің өзінде денесі бар. Жүзеге асыратын класс оны жазбаса да болады.
     * findById(...) арқылы жаз.
     */
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    /**
     * Шартқа сай келетіндер, қосылған ретімен. findAll() + Stream.filter арқылы жаз.
     *
     * Неге Predicate<? super T>, жай Predicate<T> емес: Repository<Book, Long>-ға
     * Predicate<Item> беруге болады — Item туралы шарт Book үшін де дұрыс (PECS: Consumer Super).
     */
    default List<T> findWhere(Predicate<? super T> condition) {
        return findAll().stream().filter(condition).toList();
    }
}
