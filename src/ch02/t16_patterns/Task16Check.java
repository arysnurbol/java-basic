package ch02.t16_patterns;

import ch02.check.Check;

public class Task16Check {

    public static void run() {
        Check.task("Тапсырма 16 — Singleton және Factory шаблондары");

        AppConfig first = AppConfig.getInstance();
        AppConfig second = AppConfig.getInstance();

        Check.isTrue("getInstance() null емес", first != null);
        Check.same("екі шақыру да БІР объект қайтарады", first, second);

        if (first != null) {
            first.setAppName("MyApp");
            Check.eq("бір объект болғандықтан өзгеріс екеуінде де көрінеді",
                    "MyApp", second.getAppName());
            first.setAppName("JavaPractice");
        }

        Check.isTrue("AppConfig конструкторы private", constructorIsPrivate());

        Notifier email = NotifierFactory.create("email");
        Notifier sms = NotifierFactory.create("SMS");

        Check.isTrue("create(\"email\") -> EmailNotifier", email instanceof EmailNotifier);
        Check.isTrue("create(\"SMS\") -> SmsNotifier (регистрге тәуелсіз)", sms instanceof SmsNotifier);

        if (email != null) {
            Check.eq("EmailNotifier.send()", "EMAIL: Сәлем", email.send("Сәлем"));
        } else {
            Check.fail("create(\"email\") null қайтарды");
        }
        if (sms != null) {
            Check.eq("SmsNotifier.send()", "SMS: Сәлем", sms.send("Сәлем"));
        } else {
            Check.fail("create(\"SMS\") null қайтарды");
        }

        Check.throwsEx("белгісіз тип -> IllegalArgumentException",
                IllegalArgumentException.class, () -> NotifierFactory.create("telegram"));
        Check.throwsEx("null -> IllegalArgumentException",
                IllegalArgumentException.class, () -> NotifierFactory.create(null));
    }

    private static boolean constructorIsPrivate() {
        java.lang.reflect.Constructor<?>[] constructors = AppConfig.class.getDeclaredConstructors();
        for (java.lang.reflect.Constructor<?> c : constructors) {
            if (!java.lang.reflect.Modifier.isPrivate(c.getModifiers())) {
                return false;
            }
        }
        return constructors.length > 0;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
