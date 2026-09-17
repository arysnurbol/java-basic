package ch02.t11_instanceof;

public class SmsNotification extends Notification {

    private final String phone;

    public SmsNotification(String message, String phone) {
        super(message);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public SmsNotification copy() {
        // TODO
        return null;
    }
}
