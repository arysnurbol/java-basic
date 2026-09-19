package ch02.t16_patterns;

/**
 * Тапсырма 16 — Factory шаблоны.
 * Шақырушы жақ нақты класты БІЛМЕЙДІ, тек "email"/"sms" деп сұрайды.
 */
public class NotifierFactory {

    /**
     * "email" -> EmailNotifier, "sms" -> SmsNotifier.
     * Регистр маңызды емес ("EMAIL" де жүруі керек).
     * Белгісіз тип немесе null -> IllegalArgumentException.
     */
    public static Notifier create(String type) {
        // 1. Егер null келсе, бірден IllegalArgumentException лақтырамыз
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        // 2. Регистрге тәуелсіз тексеру үшін equalsIgnoreCase қолданамыз
        if (type.equalsIgnoreCase("email")) {
            return new EmailNotifier();
        } else if (type.equalsIgnoreCase("sms")) {
            return new SmsNotifier();
        }

        // 3. Егер белгісіз тип келсе де IllegalArgumentException лақтырамыз
        throw new IllegalArgumentException("Unknown notifier type: " + type);

    }
}
