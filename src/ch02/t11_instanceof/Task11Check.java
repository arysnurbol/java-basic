package ch02.t11_instanceof;

import ch02.check.Check;

public class Task11Check {

    public static void run() {
        Check.task("Тапсырма 11 — instanceof және ковариантты типтер");

        EmailNotification email = new EmailNotification("Сәлем", "a@b.kz");
        SmsNotification sms = new SmsNotification("Сәлем", "+77010000000");
        Notification plain = new Notification("Сәлем");

        Check.eq("email маршруты", "EMAIL -> a@b.kz: Сәлем", NotificationRouter.route(email));
        Check.eq("sms маршруты", "SMS -> +77010000000: Сәлем", NotificationRouter.route(sms));
        Check.eq("белгісіз тип", "UNKNOWN", NotificationRouter.route(plain));
        Check.eq("null -> UNKNOWN", "UNKNOWN", NotificationRouter.route(null));

        Notification[] all = {email, sms, new EmailNotification("Тест", "c@d.kz"), plain};
        Check.eq("countEmails() == 2", 2, NotificationRouter.countEmails(all));

        // Ковариантты қайтару типі: cast жазбай-ақ EmailNotification айнымалысына түседі
        EmailNotification emailCopy = email.copy();
        Check.isTrue("email.copy() null емес", emailCopy != null);
        Check.notSame("copy() жаңа объект қайтарады", email, emailCopy);
        if (emailCopy != null) {
            Check.eq("көшірменің мәтіні бірдей", "Сәлем", emailCopy.getMessage());
            Check.eq("көшірменің адресі бірдей", "a@b.kz", emailCopy.getAddress());
        }

        SmsNotification smsCopy = sms.copy();
        if (smsCopy != null) {
            Check.eq("sms көшірмесінің нөмірі", "+77010000000", smsCopy.getPhone());
        } else {
            Check.fail("sms.copy() null қайтарды");
        }

        Notification plainCopy = plain.copy();
        Check.isTrue("базалық copy() да жұмыс істейді",
                plainCopy != null && "Сәлем".equals(plainCopy.getMessage()));

        Check.isTrue("EmailNotification.copy() қайтару типі — EmailNotification",
                returnTypeOfCopy(EmailNotification.class) == EmailNotification.class);
    }

    private static Class<?> returnTypeOfCopy(Class<?> type) {
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().equals("copy") && !m.isBridge() && m.getParameterCount() == 0) {
                return m.getReturnType();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
