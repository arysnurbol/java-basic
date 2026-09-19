package ch02.t16_patterns;

public class SmsNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "SMS: " + message;
    }
}
