package kz.learn.library.service;

import kz.learn.library.model.Item;
import kz.learn.library.model.Loan;
import kz.learn.library.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 7 — есептер: Collections + Stream API")
class Step7ReportsTest {

    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);

    private LibraryService service;

    @BeforeEach
    void setUp() {
        service = new LibraryService(new InMemoryRepository<>(), new InMemoryRepository<>(), new InMemoryRepository<>());
        service.addBook("Clean Code", "Robert Martin");   // 1: 14 күн, 50 ₸
        service.addBook("Effective Java", "Joshua Bloch"); // 2
        service.addMagazine("Java Magazine", 7);           // 3: 7 күн, 20 ₸
        service.addDvd("Interstellar", 169);               // 4: 3 күн, 200 ₸
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");
    }

    private static List<Long> itemIds(List<Item> items) {
        return items.stream().map(Item::getId).toList();
    }

    private static List<Long> loanIds(List<Loan> loans) {
        return loans.stream().map(Loan::getId).toList();
    }

    @Test
    @DisplayName("availableItems: қолдағылар кірмейді, қайтарылса — қайта кіреді")
    void available() {
        assertEquals(List.of(1L, 2L, 3L, 4L), itemIds(service.availableItems()));

        service.lend(2, "S-1", DAY);
        service.lend(4, "R-1", DAY);
        assertEquals(List.of(1L, 3L), itemIds(service.availableItems()));

        service.returnItem(2, DAY);
        assertEquals(List.of(1L, 2L, 3L), itemIds(service.availableItems()));
    }

    @Test
    @DisplayName("search: атауы бойынша, үлкен/кіші әріпке қарамайды")
    void search() {
        assertEquals(List.of(2L, 3L), itemIds(service.search("JAVA")));
        assertEquals(List.of(4L), itemIds(service.search("  stellar ")));
        assertTrue(service.search("python").isEmpty());
    }

    @Test
    @DisplayName("overdueLoans: тек қолдағы әрі кешіккендер, көп кешіккені алдымен, тең болса id бойынша")
    void overdue() {
        service.lend(1, "S-1", DAY);                 // L1: due 10-15
        service.lend(4, "S-1", DAY);                 // L2: due 10-04
        service.lend(3, "R-1", DAY);                 // L3: due 10-08
        service.lend(2, "R-1", DAY.plusDays(3));     // L4: due 10-18
        LocalDate today = DAY.plusDays(10);          // 10-11

        // L2: 7 күн, L3: 3 күн, L1 және L4 — әлі ерте
        assertEquals(List.of(2L, 3L), loanIds(service.overdueLoans(today)));

        service.returnItem(4, today);
        assertEquals(List.of(3L), loanIds(service.overdueLoans(today)));
    }

    @Test
    @DisplayName("overdueLoans: кешігуі тең болса — Loan id бойынша өсу ретімен")
    void overdueTie() {
        service.lend(2, "R-1", DAY);   // L1
        service.lend(1, "R-1", DAY);   // L2 — бірдей мерзім

        assertEquals(List.of(1L, 2L), loanIds(service.overdueLoans(DAY.plusDays(20))));
    }

    @Test
    @DisplayName("finesByMember: қайтарылған + қолдағы, тек > 0, кілттері сұрыпталған (TreeMap)")
    void fines() {
        service.registerRegular("A-1", "Abai");
        service.lend(4, "S-1", DAY);                 // DVD, due 10-04
        assertTrue(service.finesByMember(DAY).isEmpty(), "айыппұлсыз оқырмандар кірмейді");
        service.returnItem(4, DAY.plusDays(5));      // 2 күн * 200 / 2 = 200
        service.lend(3, "S-1", DAY);                 // журнал, due 10-08
        service.lend(1, "R-1", DAY);                 // кітап, due 10-15
        service.lend(2, "A-1", DAY);                 // кітап, due 10-15
        LocalDate today = DAY.plusDays(17);          // 10-18

        // S-1: 200 + 10 күн * 20 / 2 = 300;  R-1: 3 * 50 = 150;  A-1: 3 * 50 = 150
        Map<String, Long> fines = service.finesByMember(today);
        assertEquals(Map.of("A-1", 150L, "R-1", 150L, "S-1", 300L), fines);
        assertInstanceOf(TreeMap.class, fines, "кілттер реті үшін TreeMap керек");
        assertEquals(List.of("A-1", "R-1", "S-1"), List.copyOf(fines.keySet()));

    }

    @Test
    @DisplayName("mostPopular: берілу саны бойынша, тең болса id бойынша, limit-пен")
    void popular() {
        // 3 — үш рет, 1 — екі рет, 4 — екі рет, 2 — ешқашан
        for (int i = 0; i < 3; i++) {
            service.lend(3, "R-1", DAY);
            service.returnItem(3, DAY);
        }
        for (long id : new long[]{4, 1, 4, 1}) {
            service.lend(id, "R-1", DAY);
            service.returnItem(id, DAY);
        }

        assertEquals(List.of(3L, 1L, 4L), itemIds(service.mostPopular(10)), "берілмегендер кірмейді");
        assertEquals(List.of(3L, 1L), itemIds(service.mostPopular(2)));
        assertTrue(service.mostPopular(0).isEmpty());
    }
}
