package kz.learn.library.repository;

import kz.learn.library.model.Book;
import kz.learn.library.model.Dvd;
import kz.learn.library.model.Item;
import kz.learn.library.model.Magazine;
import kz.learn.library.model.Member;
import kz.learn.library.model.RegularMember;
import kz.learn.library.model.StudentMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 4 — generic InMemoryRepository<T, ID>")
class Step4RepositoryTest {

    private static List<Long> ids(List<? extends Item> items) {
        return items.stream().map(Item::getId).toList();
    }

    @Test
    @DisplayName("Long id: save / findById / findAll (қосылған ретімен)")
    void longIds() {
        Repository<Item, Long> repo = new InMemoryRepository<>();
        Book b = new Book(5, "B", "A");

        assertSame(b, repo.save(b));
        repo.save(new Dvd(1, "D", 90));
        repo.save(new Magazine(3, "M", 1));

        assertSame(b, repo.findById(5L).orElseThrow());
        assertEquals(Optional.empty(), repo.findById(42L));
        assertEquals(List.of(5L, 1L, 3L), ids(repo.findAll()), "қосылған реті сақталуы керек");
    }

    @Test
    @DisplayName("String id: сол класс басқа сущностьпен де жұмыс істейді")
    void stringIds() {
        Repository<Member, String> repo = new InMemoryRepository<>();
        repo.save(new StudentMember("S-1", "Aru"));
        repo.save(new RegularMember("R-1", "Daniyar"));

        assertEquals("Daniyar", repo.findById("R-1").orElseThrow().getName());
        assertTrue(repo.findById("X").isEmpty());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    @DisplayName("save сол id-мен — ауыстырады, реті өзгермейді")
    void replace() {
        Repository<Item, Long> repo = new InMemoryRepository<>();
        repo.save(new Book(1, "Old", "A"));
        repo.save(new Book(2, "Two", "A"));
        repo.save(new Book(1, "New", "A"));

        assertEquals(2, repo.findAll().size());
        assertEquals("New", repo.findById(1L).orElseThrow().getTitle());
        assertEquals(List.of(1L, 2L), ids(repo.findAll()));
    }

    @Test
    @DisplayName("findAll көшірме қайтарады — сырттан өзгерту ішке әсер етпейді")
    void defensiveCopy() {
        Repository<Item, Long> repo = new InMemoryRepository<>();
        repo.save(new Book(1, "B", "A"));

        List<Item> all = repo.findAll();
        try {
            all.clear();
        } catch (UnsupportedOperationException ignored) {
            // өзгертілмейтін тізім қайтарсаң да — дұрыс
        }
        assertEquals(1, repo.findAll().size());
    }

    @Test
    @DisplayName("deleteById / existsById (default метод)")
    void deleteAndExists() {
        Repository<Item, Long> repo = new InMemoryRepository<>();
        repo.save(new Book(1, "B", "A"));

        assertTrue(repo.existsById(1L));
        assertTrue(repo.deleteById(1L));
        assertFalse(repo.existsById(1L));
        assertFalse(repo.deleteById(1L));
    }

    @Test
    @DisplayName("findWhere (default метод): шарт бойынша, ретімен; Predicate<? super T> қабылдайды")
    void findWhere() {
        Repository<Item, Long> repo = new InMemoryRepository<>();
        repo.save(new Book(1, "Java", "A"));
        repo.save(new Dvd(2, "Movie", 90));
        repo.save(new Book(3, "Kotlin", "B"));

        assertEquals(List.of(1L, 3L), ids(repo.findWhere(i -> i instanceof Book)));

        Predicate<Object> any = o -> true;   // Object — Item-нің ата-класы, сондықтан ? super Item-ге сай
        assertEquals(3, repo.findWhere(any).size());
    }

    @Test
    @DisplayName("default методтар интерфейсте жазылған, InMemoryRepository оларды override етпейді")
    void defaultsLiveInInterface() throws NoSuchMethodException {
        assertTrue(Repository.class.getMethod("existsById", Object.class).isDefault());
        assertTrue(Repository.class.getMethod("findWhere", Predicate.class).isDefault());
        assertEquals(Repository.class,
                InMemoryRepository.class.getMethod("findWhere", Predicate.class).getDeclaringClass());
    }
}
