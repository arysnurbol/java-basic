package kz.learn.library.service;

import kz.learn.library.exception.ItemNotAvailableException;
import kz.learn.library.exception.LibraryException;
import kz.learn.library.exception.LoanLimitExceededException;
import kz.learn.library.exception.NotFoundException;
import kz.learn.library.model.Book;
import kz.learn.library.model.Dvd;
import kz.learn.library.model.Identifiable;
import kz.learn.library.model.Item;
import kz.learn.library.model.Loan;
import kz.learn.library.model.Magazine;
import kz.learn.library.model.Member;
import kz.learn.library.model.RegularMember;
import kz.learn.library.model.StudentMember;
import kz.learn.library.repository.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Кітапхананың бизнес-логикасы. Үш репозиторийді конструктор арқылы алады (композиция + DI).
 *
 * id-ды сервис береді: экземплярларға 1, 2, 3, ... ; беру жазбаларына (Loan) — бөлек есептегіш 1, 2, 3, ...
 * Оқырманның id-сы — билет нөмірі, оны тіркеу кезінде сырттан береді.
 *
 * Экземпляр "қолда" (on loan) = оған қайтарылмаған Loan бар.
 */
public class LibraryService {

    // TODO: өрістерді жаз

    public LibraryService(Repository<Item, Long> items,
                          Repository<Member, String> members,
                          Repository<Loan, Long> loans) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 6: негізгі операциялар

    public Book addBook(String title, String author) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Magazine addMagazine(String title, int issue) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Dvd addDvd(String title, int minutes) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * GENERIC МЕТОД. Неге <I extends Item>, жай Item емес: addBook() Book қайтаруы керек.
     * Параметр Item болса, нәтиже де Item болар еді — шақырушы қайтадан (Book) деп cast жасауға мәжбүр.
     *
     * Назар аудар: сәтсіз жасалған экземпляр (бос атау) id-ны "жеп қояды" — бұл қалыпты.
     */
    private <I extends Item> I saveItem(I item) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Билет нөмірі бос емес; бұрын тіркелген болса — LibraryException("Member already exists: S-1"). */
    public Member registerStudent(String cardNumber, String name) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public Member registerRegular(String cardNumber, String name) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private Member register(Member member) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — NotFoundException("Item", id). */
    public Item getItem(long id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — NotFoundException("Member", cardNumber). */
    public Member getMember(String cardNumber) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * GENERIC МЕТОД: кез келген репозиторийден id бойынша алады, жоқ болса — NotFoundException.
     * getItem() де, getMember() де осыны қолданады — код бір рет жазылады.
     * Кеңес: repo.findById(id).orElseThrow(() -> ...)
     */
    private static <T extends Identifiable<ID>, ID> T getOrThrow(Repository<T, ID> repo, ID id, String entity) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Экземплярды оқырманға береді. Тексеріс реті:
     *  1) экземпляр бар ма  — жоқ болса NotFoundException;
     *  2) оқырман бар ма    — жоқ болса NotFoundException;
     *  3) экземпляр бос па  — қолда болса ItemNotAvailableException;
     *  4) оқырманның лимиті — activeLoans >= maxLoans() болса LoanLimitExceededException.
     * Жаңа Loan жасап, сақтап, қайтарады.
     */
    public Loan lend(long itemId, String cardNumber, LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Экземплярды қайтарады, айыппұлды (₸) қайтарады.
     * Экземпляр жоқ — NotFoundException("Item", id);
     * экземпляр қолда емес — NotFoundException("Active loan for item", id).
     */
    public long returnItem(long itemId, LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Осы экземплярдың қайтарылмаған Loan-ы. Кеңес: loans.findWhere(...) */
    private Optional<Loan> activeLoanOf(long itemId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Оқырманның қайтарылмаған Loan-дары, берілген ретімен. Оқырман жоқ — NotFoundException. */
    public List<Loan> activeLoans(String cardNumber) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 7: есептер (Collections + Stream)

    /** Қазір қолда емес экземплярлар, қосылған ретімен. */
    public List<Item> availableItems() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Атауында query бар экземплярлар (үлкен/кіші әріпке қарамай), қосылған ретімен. */
    public List<Item> search(String query) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * today күні кешігіп жатқан (қайтарылмаған) Loan-дар:
     * алдымен ең көп кешіккені; кешігуі тең болса — Loan id бойынша өсу ретімен.
     */
    public List<Loan> overdueLoans(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Әр оқырманның барлық Loan-дары (қайтарылған да, қолдағы да) бойынша жиынтық айыппұлы.
     * Тек айыппұлы > 0 оқырмандар. Кілттері (билет нөмірі) алфавит ретімен — TreeMap.
     * Кеңес: Collectors.groupingBy(..., TreeMap::new, Collectors.summingLong(...))
     */
    public Map<String, Long> finesByMember(LocalDate today) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Ең көп берілген экземплярлар (барлық уақыттағы Loan саны бойынша), ең көбі limit дана.
     * Саны тең болса — Item id бойынша өсу ретімен. Бірде-бір рет берілмегендер кірмейді.
     * Кеңес: groupingBy(Loan::getItem, counting()) — Map<Item, Long>; бұл Item.equals/hashCode-қа сүйенеді!
     */
    public List<Item> mostPopular(int limit) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
