package ch02.t16_patterns;

public class EmailNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "EMAIL: " + message;
    }
}
