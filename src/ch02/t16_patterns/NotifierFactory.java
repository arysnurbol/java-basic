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
        // TODO
        return null;
    }
}
