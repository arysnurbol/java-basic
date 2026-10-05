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

    private final Repository<Item, Long> itemLongRepository;
    private final Repository<Member, String> memberLongRepository;
    private final Repository<Loan, Long> loanLongRepository;
    private long itemId = 1;
    private long loanId = 1;

    public LibraryService(Repository<Item, Long> items,
                          Repository<Member, String> members,
                          Repository<Loan, Long> loans) {
        this.itemLongRepository = items;
        this.memberLongRepository = members;
        this.loanLongRepository = loans;
    }

    // ---------------------------------------------------------------- Қадам 6: негізгі операциялар

    public Book addBook(String title, String author) {
        return saveItem(new Book(itemId++, title, author));
    }

    public Magazine addMagazine(String title, int issue) {
        return saveItem(new Magazine(itemId++, title, issue));
    }

    public Dvd addDvd(String title, int minutes) {
        return saveItem(new Dvd(itemId++, title, minutes));
    }

    /**
     * GENERIC МЕТОД. Неге <I extends Item>, жай Item емес: addBook() Book қайтаруы керек.
     * Параметр Item болса, нәтиже де Item болар еді — шақырушы қайтадан (Book) деп cast жасауға мәжбүр.
     *
     * Назар аудар: сәтсіз жасалған экземпляр (бос атау) id-ны "жеп қояды" — бұл қалыпты.
     */
    private <I extends Item> I saveItem(I item) {
        itemLongRepository.save(item);
        return item;
    }

    /** Билет нөмірі бос емес; бұрын тіркелген болса — LibraryException("Member already exists: S-1"). */
    public Member registerStudent(String cardNumber, String name) {
        return register(new StudentMember(cardNumber, name));
    }

    public Member registerRegular(String cardNumber, String name) {
        return register(new RegularMember(cardNumber, name));
    }

    private Member register(Member member) {
        if (memberLongRepository.existsById(member.getId())) {
            throw new LibraryException("Member already exists: " + member.getId());
        }

        return memberLongRepository.save(member);
    }

    /** Табылмаса — NotFoundException("Item", id). */
    public Item getItem(long id) {
        return getOrThrow(itemLongRepository, id, "Item");
    }

    /** Табылмаса — NotFoundException("Member", cardNumber). */
    public Member getMember(String cardNumber) {
        return getOrThrow(memberLongRepository, cardNumber, "Member");
    }

    /**
     * GENERIC МЕТОД: кез келген репозиторийден id бойынша алады, жоқ болса — NotFoundException.
     * getItem() де, getMember() де осыны қолданады — код бір рет жазылады.
     * Кеңес: repo.findById(id).orElseThrow(() -> ...)
     */
    private static <T extends Identifiable<ID>, ID> T getOrThrow(Repository<T, ID> repo, ID id, String entity) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(entity, id));
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
        Item item = getItem(itemId);
        Member member = getMember(cardNumber);

        if (activeLoanOf(itemId).isPresent()) {
            throw new ItemNotAvailableException(itemId);
        }

        if (activeLoans(cardNumber).size() >= member.maxLoans()) {
            throw new LoanLimitExceededException(member.getId(), member.maxLoans());
        }

        Loan loan = new Loan(loanId++, item, member, today);
        return loanLongRepository.save(loan);
    }

    /**
     * Экземплярды қайтарады, айыппұлды (₸) қайтарады.
     * Экземпляр жоқ — NotFoundException("Item", id);
     * экземпляр қолда емес — NotFoundException("Active loan for item", id).
     */
    public long returnItem(long itemId, LocalDate today) {
        getItem(itemId);

        Loan activeLoan = activeLoanOf(itemId)
                .orElseThrow(() -> new NotFoundException("Active loan for item", itemId));

        activeLoan.markReturned(today);
        return activeLoan.fine(today);
    }

    /** Осы экземплярдың қайтарылмаған Loan-ы. Кеңес: loans.findWhere(...) */
    private Optional<Loan> activeLoanOf(long itemId) {
        return loanLongRepository.findWhere(loan ->
                loan.getItem().getId() == itemId && !loan.isReturned()
        ).stream().findFirst();
    }

    /** Оқырманның қайтарылмаған Loan-дары, берілген ретімен. Оқырман жоқ — NotFoundException. */
    public List<Loan> activeLoans(String cardNumber) {
        Member member = getMember(cardNumber);
        return loanLongRepository.findWhere(loan ->
                loan.getMember().equals(member) && !loan.isReturned()
        );
    }

    // ---------------------------------------------------------------- Қадам 7: есептер (Collections + Stream)

    /** Қазір қолда емес экземплярлар, қосылған ретімен. */
    public List<Item> availableItems() {
        return itemLongRepository.findWhere(item -> activeLoanOf(item.getId()).isEmpty());
    }

    /** Атауында query бар экземплярлар (үлкен/кіші әріпке қарамай), қосылған ретімен. */
    public List<Item> search(String query) {
        String needle = query.trim().toLowerCase();
        return itemLongRepository.findWhere(item -> item.getTitle().toLowerCase().contains(needle));
    }

    /**
     * today күні кешігіп жатқан (қайтарылмаған) Loan-дар:
     * алдымен ең көп кешіккені; кешігуі тең болса — Loan id бойынша өсу ретімен.
     */
    public List<Loan> overdueLoans(LocalDate today) {
        return loanLongRepository.findWhere(loan -> loan.isOverdue(today)).stream()
                .sorted(Comparator.comparingLong((Loan loan) -> loan.overdueDays(today))
                        .reversed()
                        .thenComparing(Loan::getId))
                .toList();
    }

    /**
     * Әр оқырманның барлық Loan-дары (қайтарылған да, қолдағы да) бойынша жиынтық айыппұлы.
     * Тек айыппұлы > 0 оқырмандар. Кілттері (билет нөмірі) алфавит ретімен — TreeMap.
     * Кеңес: Collectors.groupingBy(..., TreeMap::new, Collectors.summingLong(...))
     */
    public Map<String, Long> finesByMember(LocalDate today) {
        return loanLongRepository.findWhere(loan -> loan.fine(today) > 0).stream()
                .collect(Collectors.groupingBy(
                        loan -> loan.getMember().getId(),
                        TreeMap::new,
                        Collectors.summingLong(loan -> loan.fine(today))));
    }

    /**
     * Ең көп берілген экземплярлар (барлық уақыттағы Loan саны бойынша), ең көбі limit дана.
     * Саны тең болса — Item id бойынша өсу ретімен. Бірде-бір рет берілмегендер кірмейді.
     * Кеңес: groupingBy(Loan::getItem, counting()) — Map<Item, Long>; бұл Item.equals/hashCode-қа сүйенеді!
     */
    public List<Item> mostPopular(int limit) {
        Map<Item, Long> counts = loanLongRepository.findAll().stream()
                .collect(Collectors.groupingBy(Loan::getItem, Collectors.counting()));

        return counts.entrySet().stream()
                .sorted(Map.Entry.<Item, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().getId()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }
}
