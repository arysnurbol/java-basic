package kz.learn.hotel.room;

import java.util.Locale;
import java.util.Objects;

/**
 * FACTORY: «қандай бөлме керек» деген сұраққа жауап беретін ЖАЛҒЫЗ орын.
 *
 * Клиент (HotelService, ConsoleApp) StandardRoom, DeluxeRoom, Suite кластарын БІЛМЕЙДІ —
 * ол тек Room және RoomType-ты көреді. Жаңа түр қосу (мысалы, FamilyRoom) = жаңа класс + осы файлда
 * бір case. Сервис пен UI-ға бір жол да тимейді.
 * Java-ның өзінде де бар: List.of(...), LocalDate.of(...), Executors.newFixedThreadPool(...) — қай
 * класс қайтатынын білмейсің, тек интерфейсті алып жұмыс істейсің.
 *
 * Объект жасалмайды — тек статикалық методтар (Collections, Objects сияқты): private конструктор.
 */
public final class RoomFactory {

    private RoomFactory() {
    }

    /**
     * type -> сәйкес ұрпақ. type null болса — NullPointerException.
     * Кеңес: switch expression: case STANDARD -> new StandardRoom(number); ...
     * Енумның барлық мәнін қамтыған switch expression-да default керек емес — компилятор өзі тексереді:
     * RoomType-қа жаңа мән қоссаң, бұл жер компиляцияланбай қалады. Бұл — жақсы.
     */
    public static Room create(RoomType type, String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Мәтіннен: "deluxe", " Suite " -> RoomType (регистрге қарамай, strip), содан create(type, number).
     * Белгісіз түр -> IllegalArgumentException("Unknown room type: penthouse").
     * Кеңес: RoomType.valueOf(typeName.strip().toUpperCase(Locale.ROOT)) белгісіз атауға IllegalArgumentException лақтырады.
     *
     * ТҰЗАҚ: create(...)-ты try ішіне салма. Бос нөмір де IllegalArgumentException береді
     * ("number must not be blank"), ал сенің catch-ің оны «Unknown room type» деп бүркемелеп жібереді.
     * try-ды тек түрді талдайтын жолға ғана қой.
     */
    public static Room create(String typeName, String number) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
