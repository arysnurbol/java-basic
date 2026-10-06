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

    // TODO: өрістерді жаз

    /** rooms, bookings, clock — null болмайды. */
    public HotelService(Repository<Room, String> rooms, Repository<Booking, String> bookings, Clock clock) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 6: операциялар

    /**
     * Жаңа бөлме: RoomFactory арқылы жаса, сақта, қайтар.
     * Сондай нөмір бар болса — HotelException("Room already exists: 101").
     * Кеңес: алдымен жаса, СОСЫН тексер: нөмір strip-тен өтсін, " 101 " мен "101" бір бөлме.
     */
    public Room addRoom(RoomType type, String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — RoomNotFoundException(number). */
    public Room getRoom(String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Табылмаса — BookingNotFoundException(id). */
    public Booking getBooking(String id) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Бөлме stay кезеңінде бос па: осы бөлменің ешбір броні оны бөгемейді (Booking.blocks).
     * Бөлме жоқ — RoomNotFoundException. Кеңес: noneMatch.
     */
    public boolean isAvailable(String number, DateRange stay) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Іздеу: capacity >= guests ЖӘНЕ stay кезеңінде бос бөлмелер.
     * Рет: осы кезеңнің бағасы (priceFor(stay)) бойынша өсу ретімен, тең болса — нөмір бойынша.
     * Кеңес: Comparator.comparing((Room r) -> r.priceFor(stay)).thenComparing(Room::getId).
     * (Лямбданың параметр типін неге жазу керек? Жазбай көр — компилятор не дейді?)
     */
    public List<Room> findAvailable(DateRange stay, int guests) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Болдырмау: getBooking(...).cancel(today()). Қайтарылған ақшаны қайтарады. */
    public Money cancel(String bookingId) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private LocalDate today() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    // ---------------------------------------------------------------- Қадам 7: есептер

    /**
     * Қонақтың барлық броні (болдырылмағандары да), аты регистрге қарамай салыстырылады (strip),
     * келу күні бойынша, тең болса — бронь нөмірі бойынша.
     * Кеңес: equalsIgnoreCase.
     */
    public List<Booking> bookingsOf(String guest) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Осы түнгі толу пайызы: белсенді броньмен бос емес бөлмелер * 100 / барлық бөлме, бүтін бөлігі.
     * Бөлме жоқ — 0 (нөлге бөлме!). 4 бөлменің 1-і бос емес -> 25; 3 бөлменің 1-і -> 33.
     * Кеңес: stay.contains(night) — кету күні кірмейді, сол түні бөлме бос.
     */
    public int occupancyPercent(LocalDate night) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Бөлме түрі бойынша табыс: барлық броньның revenue() қосындысы (болдырылмағандарынан қалған ақша да).
     * Тек броні бар түрлер; EnumMap — кілттер enum ретімен.
     * Кеңес: 03-bank-тағы totalsByType-ты еске ал: groupingBy + EnumMap + reducing.
     */
    public Map<RoomType, Money> revenueByType() {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Осы күні келетін қонақтар: белсенді, checkIn == day, бөлме нөмірі бойынша. */
    public List<Booking> checkInsOn(LocalDate day) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
