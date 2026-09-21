package ch03.t04_generic_interface;

import java.util.List;
import java.util.Optional;

/**
 * Тапсырма 04 — Обобщенный интерфейс.
 * Кітап: "Обобщенные интерфейсы" (119 б.), "Обобщения и стирание типов" (120 б.)
 *
 * Бұл — нақты Spring Data-дағы Repository<T, ID>-дің қарапайым түрі.
 * Интерфейс екі параметр алады: сақталатын типі (T) және кілттің типі (ID).
 *
 * Осы интерфейсті ӨЗГЕРТПЕ — тек InMemoryRepository-ді толтыр.
 */
public interface Repository<T, ID> {

    /** Сақтайды (сондай кілт бар болса — үстінен жазады) және сақталған нәрсені қайтарады. */
    T save(ID id, T item);

    /** Кілт бойынша іздейді. Табылмаса — Optional.empty(). */
    Optional<T> findById(ID id);

    /** Барлығы, қосылу ретімен. */
    List<T> findAll();

    /** Өшіреді. Шынымен өшсе true, мұндай кілт болмаса false. */
    boolean deleteById(ID id);

    int count();
}
