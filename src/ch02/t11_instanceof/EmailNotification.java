package ch02.t11_instanceof;

public class EmailNotification extends Notification {

    private final String address;

    public EmailNotification(String message, String address) {
        super(message);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    /**
     * КОВАРИАНТТЫ ҚАЙТАРУ ТИПІ: қайтару типі Notification емес, EmailNotification.
     * Осының арқасында шақырған жақта cast қажет болмайды.
     */
    @Override
    public EmailNotification copy() {
        return new EmailNotification(getMessage(), getAddress());
    }
}
