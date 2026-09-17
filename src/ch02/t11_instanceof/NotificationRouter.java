package ch02.t11_instanceof;

public class NotificationRouter {

    /**
     * Хабарламаның НАҚТЫ типіне қарай маршрут жолын құрайды:
     *   EmailNotification -> "EMAIL -> a@b.kz: Сәлем"
     *   SmsNotification   -> "SMS -> +77010000000: Сәлем"
     *   басқасы / null    -> "UNKNOWN"
     *
     * Кеңес: Java 16-дан бері instanceof-тың pattern matching түрі бар:
     *   if (n instanceof EmailNotification email) { ... email.getAddress() ... }
     * Бөлек cast жазудың қажеті жоқ.
     */
    public static String route(Notification n) {
        // TODO
        return null;
    }

    /** Массивтегі EmailNotification объектілерінің саны. */
    public static int countEmails(Notification[] all) {
        // TODO
        return 0;
    }
}
