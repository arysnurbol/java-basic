package kz.learn.hotel.service;

import kz.learn.hotel.exception.BookingNotFoundException;
import kz.learn.hotel.exception.HotelException;
import kz.learn.hotel.exception.RoomNotAvailableException;
import kz.learn.hotel.exception.RoomNotFoundException;
import kz.learn.hotel.model.Booking;
import kz.learn.hotel.model.DateRange;
import kz.learn.hotel.model.Money;
import kz.learn.hotel.repository.Repository;
import kz.learn.hotel.room.Room;
import kz.learn.hotel.room.RoomFactory;
import kz.learn.hotel.room.RoomType;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Қонақүйдің бизнес-логикасы.
 *
 * Тәуелділіктер конструктор арқылы (DI): екі репозиторий және Clock (BankService-тегідей).
 * Бүгінгі күн — тек LocalDate.now(clock).
 *
 * Назар аудар: HotelPolicy — singleton, оны getInstance() арқылы аламыз. Ал HotelService-тің өзі
 * singleton ЕМЕС: оның күйі бар (репозиторийлер, есептегіш), тестте әр тестке жаңасы керек.
 *
 * Бронь нөмірлері: "B0001", "B0002", ... — String.format("B%04d", n).
 */
public class HotelService {

    private final Repository<Room, String> rooms;
    private final Repository<Booking, String> bookings;
    private final Clock clock;
    private int bookingCounter;

    /** rooms, bookings, clock — null болмайды. */
    public HotelService(Repository<Room, String> rooms, Repository<Booking, String> bookings, Clock clock) {
        this.rooms = Objects.requireNonNull(rooms, "rooms");
        this.bookings = Objects.requireNonNull(bookings, "bookings");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    // ---------------------------------------------------------------- Қадам 6: операциялар

    /**
     * Жаңа бөлме: RoomFactory арқылы жаса, сақта, қайтар.
     * Сондай нөмір бар болса — HotelException("Room already exists: 101").
     * Кеңес: алдымен жаса, СОСЫН тексер: нөмір strip-тен өтсін, " 101 " мен "101" бір бөлме.
     */
    public Room addRoom(RoomType type, String number) {
        Room room = RoomFactory.create(type, number);
        if (rooms.existsById(room.getId())) {
            throw new HotelException("Room already exists: " + room.getId());
        }
        return rooms.save(room);
    }

    /** Табылмаса — RoomNotFoundException(number). */
    public Room getRoom(String number) {
        return rooms.findById(number).orElseThrow(() -> new RoomNotFoundException(number));
    }

    /** Табылмаса — BookingNotFoundException(id). */
    public Booking getBooking(String id) {
        return bookings.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
    }

    /**
     * Бөлме stay кезеңінде бос па: осы бөлменің ешбір броні оны бөгемейді (Booking.blocks).
     * Бөлме жоқ — RoomNotFoundException. Кеңес: noneMatch.
     */
    public boolean isAvailable(String number, DateRange stay) {
        Room room = getRoom(number);
        return bookings.findAll().stream()
                .filter(b -> b.getRoom().equals(room))
                .noneMatch(b -> b.blocks(stay));
    }

    /**
     * Іздеу: capacity >= guests ЖӘНЕ stay кезеңінде бос бөлмелер.
     * Рет: осы кезеңнің бағасы (priceFor(stay)) бойынша өсу ретімен, тең болса — нөмір бойынша.
     * Кеңес: Comparator.comparing((Room r) -> r.priceFor(stay)).thenComparing(Room::getId).
     * (Лямбданың параметр типін неге жазу керек? Жазбай көр — компилятор не дейді?)
     */
    public List<Room> findAvailable(DateRange stay, int guests) {
        return rooms.findAll().stream()
                .filter(r -> r.capacity() >= guests)
                .filter(r -> isAvailable(r.getId(), stay))
                .sorted(Comparator.comparing((Room r) -> r.priceFor(stay)).thenComparing(Room::getId))
                .toList();
    }

    /**
     * Брондау. Рет:
     *  1) бөлмені тап (жоқ -> RoomNotFoundException);
     *  2) stay.checkIn() бүгіннен БҰРЫН болса -> IllegalArgumentException("checkIn must not be in the past")
     *     (бүгін — рұқсат);
     *  3) бос емес -> RoomNotAvailableException(room.getId(), stay);
     *  4) new Booking(...) — қонақ саны мен түн саны сонда тексеріледі;
     *  5) сақта, қайтар.
     * Сәтсіз брондау нөмірді «жемейді» (BankService.openAccount-тағыдай): есептегішті Booking
     * сәтті жасалғаннан КЕЙІН арттыр.
     */
    public Booking book(String roomNumber, String guest, int guests, DateRange stay) {
        Room room = getRoom(roomNumber);
        if (stay.checkIn().isBefore(today())) {
            throw new IllegalArgumentException("checkIn must not be in the past");
        }
        if (!isAvailable(room.getId(), stay)) {
            throw new RoomNotAvailableException(room.getId(), stay);
        }
        Booking booking = new Booking(String.format("B%04d", bookingCounter + 1), room, guest, guests, stay);
        bookingCounter++;
        return bookings.save(booking);
    }

    /** Болдырмау: getBooking(...).cancel(today()). Қайтарылған ақшаны қайтарады. */
    public Money cancel(String bookingId) {
        return getBooking(bookingId).cancel(today());
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }

    // ---------------------------------------------------------------- Қадам 7: есептер

    /**
     * Қонақтың барлық броні (болдырылмағандары да), аты регистрге қарамай салыстырылады (strip),
     * келу күні бойынша, тең болса — бронь нөмірі бойынша.
     * Кеңес: equalsIgnoreCase.
     */
    public List<Booking> bookingsOf(String guest) {
        String name = guest.strip();
        return bookings.findWhere(b -> b.getGuest().equalsIgnoreCase(name)).stream()
                .sorted(Comparator.comparing((Booking b) -> b.getStay().checkIn()).thenComparing(Booking::getId))
                .toList();
    }

    /**
     * Осы түнгі толу пайызы: белсенді броньмен бос емес бөлмелер * 100 / барлық бөлме, бүтін бөлігі.
     * Бөлме жоқ — 0 (нөлге бөлме!). 4 бөлменің 1-і бос емес -> 25; 3 бөлменің 1-і -> 33.
     * Кеңес: stay.contains(night) — кету күні кірмейді, сол түні бөлме бос.
     */
    public int occupancyPercent(LocalDate night) {
        int total = rooms.findAll().size();
        if (total == 0) {
            return 0;
        }
        long occupied = bookings.findAll().stream()
                .filter(b -> b.isActive() && b.getStay().contains(night))
                .map(Booking::getRoom)
                .distinct()
                .count();
        return (int) (occupied * 100 / total);
    }

    /**
     * Бөлме түрі бойынша табыс: барлық броньның revenue() қосындысы (болдырылмағандарынан қалған ақша да).
     * Тек броні бар түрлер; EnumMap — кілттер enum ретімен.
     * Кеңес: 03-bank-тағы totalsByType-ты еске ал: groupingBy + EnumMap + reducing.
     */
    public Map<RoomType, Money> revenueByType() {
        return bookings.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getRoom().getType(),
                        () -> new EnumMap<>(RoomType.class),
                        Collectors.reducing(Money.ZERO, Booking::revenue, Money::plus)));
    }

    /** Осы күні келетін қонақтар: белсенді, checkIn == day, бөлме нөмірі бойынша. */
    public List<Booking> checkInsOn(LocalDate day) {
        return bookings.findAll().stream()
                .filter(b -> b.isActive() && b.getStay().checkIn().equals(day))
                .sorted(Comparator.comparing((Booking b) -> b.getRoom().getId()))
                .toList();
    }
}
