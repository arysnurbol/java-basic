package kz.learn.library.service;

import kz.learn.library.exception.ItemNotAvailableException;
import kz.learn.library.exception.LibraryException;
import kz.learn.library.exception.LoanLimitExceededException;
import kz.learn.library.exception.NotFoundException;
import kz.learn.library.model.Book;
import kz.learn.library.model.Dvd;
import kz.learn.library.model.Item;
import kz.learn.library.model.Loan;
import kz.learn.library.model.Magazine;
import kz.learn.library.model.Member;
import kz.learn.library.model.RegularMember;
import kz.learn.library.model.StudentMember;
import kz.learn.library.repository.InMemoryRepository;
import kz.learn.library.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Қадам 6 — LibraryService: беру және қайтару ережелері")
class Step6ServiceTest {

    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);

    private Repository<Item, Long> items;
    private Repository<Member, String> members;
    private Repository<Loan, Long> loans;
    private LibraryService service;

    @BeforeEach
    void setUp() {
        items = new InMemoryRepository<>();
        members = new InMemoryRepository<>();
        loans = new InMemoryRepository<>();
        service = new LibraryService(items, members, loans);
    }

    private static List<Long> loanIds(List<Loan> list) {
        return list.stream().map(Loan::getId).toList();
    }

    @Test
    @DisplayName("add*: id 1-ден өседі, нақты тип қайтады (cast-сыз), репозиторийге сақталады")
    void addItems() {
        Book b = service.addBook("Clean Code", "Robert Martin");
        Magazine m = service.addMagazine("Forbes", 3);
        Dvd d = service.addDvd("Interstellar", 169);

        assertEquals(List.of(1L, 2L, 3L), List.of(b.getId(), m.getId(), d.getId()));
        assertEquals("Robert Martin", b.getAuthor());
        assertEquals(3, items.findAll().size(), "сервис репозиторийді қолдануы керек");
        assertThrows(IllegalArgumentException.class, () -> service.addBook(" ", "A"));
    }

    @Test
    @DisplayName("register*: түрі дұрыс, қайталанған билет -> LibraryException")
    void register() {
        assertInstanceOf(StudentMember.class, service.registerStudent("S-1", "Aru"));
        assertInstanceOf(RegularMember.class, service.registerRegular("R-1", "Daniyar"));
        assertEquals(2, members.findAll().size());

        LibraryException e = assertThrows(LibraryException.class, () -> service.registerRegular("S-1", "Other"));
        assertEquals("Member already exists: S-1", e.getMessage());
        assertEquals("Aru", service.getMember("S-1").getName(), "бұрынғы оқырман өзгермеуі керек");
    }

    @Test
    @DisplayName("getItem / getMember: табылмаса NotFoundException")
    void get() {
        service.addBook("B", "A");
        service.registerStudent("S-1", "Aru");

        assertEquals("B", service.getItem(1).getTitle());
        assertEquals("Aru", service.getMember("S-1").getName());

        NotFoundException e1 = assertThrows(NotFoundException.class, () -> service.getItem(99));
        assertEquals("Item not found: 99", e1.getMessage());
        NotFoundException e2 = assertThrows(NotFoundException.class, () -> service.getMember("X"));
        assertEquals("Member not found: X", e2.getMessage());
    }

    @Test
    @DisplayName("lend: Loan жасалады, сақталады, id 1-ден өседі")
    void lend() {
        service.addBook("B", "A");
        service.addDvd("D", 90);
        service.registerStudent("S-1", "Aru");

        Loan l1 = service.lend(1, "S-1", DAY);
        Loan l2 = service.lend(2, "S-1", DAY);

        assertEquals(1L, l1.getId());
        assertEquals(2L, l2.getId());
        assertEquals(DAY.plusDays(14), l1.getDueDate());
        assertEquals(2, loans.findAll().size());
        assertEquals(List.of(1L, 2L), loanIds(service.activeLoans("S-1")));
    }

    @Test
    @DisplayName("lend: жоқ экземпляр / жоқ оқырман -> NotFoundException")
    void lendNotFound() {
        service.addBook("B", "A");
        service.registerStudent("S-1", "Aru");

        assertThrows(NotFoundException.class, () -> service.lend(9, "S-1", DAY));
        assertThrows(NotFoundException.class, () -> service.lend(1, "X", DAY));
        assertTrue(loans.findAll().isEmpty());
    }

    @Test
    @DisplayName("lend: қолдағы экземплярды қайта беруге болмайды")
    void lendTwice() {
        service.addBook("B", "A");
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");
        service.lend(1, "S-1", DAY);

        ItemNotAvailableException e = assertThrows(ItemNotAvailableException.class,
                () -> service.lend(1, "R-1", DAY));
        assertEquals(1, e.getItemId());
    }

    @Test
    @DisplayName("lend: студент 3-тен, кәдімгі оқырман 5-тен көп ала алмайды")
    void limits() {
        for (int i = 1; i <= 6; i++) {
            service.addBook("B" + i, "A");
        }
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");

        service.lend(1, "S-1", DAY);
        service.lend(2, "S-1", DAY);
        service.lend(3, "S-1", DAY);
        LoanLimitExceededException e = assertThrows(LoanLimitExceededException.class,
                () -> service.lend(4, "S-1", DAY));
        assertEquals("Loan limit exceeded: S-1 (max 3)", e.getMessage());

        service.lend(4, "R-1", DAY);
        service.lend(5, "R-1", DAY);
        service.lend(6, "R-1", DAY);
        assertEquals(3, service.activeLoans("R-1").size());
    }

    @Test
    @DisplayName("returnItem: айыппұл қайтады, экземпляр қайтадан берілуі мүмкін, лимит босайды")
    void returnItem() {
        service.addDvd("D", 90);          // 3 күн, 200 ₸
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");

        service.lend(1, "S-1", DAY);
        assertEquals(200, service.returnItem(1, DAY.plusDays(5)));   // 2 күн * 200 / 2
        assertTrue(service.activeLoans("S-1").isEmpty());

        service.lend(1, "R-1", DAY.plusDays(5));
        assertEquals(0, service.returnItem(1, DAY.plusDays(6)));
    }

    @Test
    @DisplayName("returnItem: жоқ экземпляр немесе қолда емес -> NotFoundException")
    void returnErrors() {
        service.addBook("B", "A");

        assertThrows(NotFoundException.class, () -> service.returnItem(9, DAY));
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.returnItem(1, DAY));
        assertEquals("Active loan for item not found: 1", e.getMessage());
    }

    @Test
    @DisplayName("activeLoans: тек осы оқырманның қайтарылмағандары; жоқ оқырман -> NotFoundException")
    void activeLoans() {
        service.addBook("B1", "A");
        service.addBook("B2", "A");
        service.addBook("B3", "A");
        service.registerStudent("S-1", "Aru");
        service.registerRegular("R-1", "Daniyar");
        service.lend(1, "S-1", DAY);    // L1
        service.lend(2, "R-1", DAY);    // L2
        service.lend(3, "S-1", DAY);    // L3
        service.returnItem(1, DAY);

        assertEquals(List.of(3L), loanIds(service.activeLoans("S-1")));
        assertEquals(List.of(2L), loanIds(service.activeLoans("R-1")));
        assertThrows(NotFoundException.class, () -> service.activeLoans("X"));
    }
}
