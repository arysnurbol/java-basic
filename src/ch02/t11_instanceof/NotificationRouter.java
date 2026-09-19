package ch02.t11_instanceof;

import java.util.Arrays;

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
        // Java 16 Pattern Matching: instanceof бір уақытта тексеріп, типті түрлендіреді (cast)
        if (n instanceof EmailNotification email) {
            return "EMAIL -> " + email.getAddress() + ": " + email.getMessage();
        } else if (n instanceof SmsNotification sms) {
            // SmsNotification класында getPhoneNumber() сияқты әдіс бар деп есептесек:
            return "SMS -> " + sms.getPhone() + ": " + sms.getMessage();
        } else {
            return "UNKNOWN";
        }
    }

    /** Массивтегі EmailNotification объектілерінің саны. */
    public static int countEmails(Notification[] all) {
        int count = 0;
        for (Notification n : all) {
            if (n instanceof EmailNotification) {
                count++;
            }
        }

        return count;
    }
}
