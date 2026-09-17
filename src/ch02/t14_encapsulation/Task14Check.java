package ch02.t14_encapsulation;

import ch02.check.Check;

public class Task14Check {

    public static void run() {
        Check.task("Тапсырма 14 — Инкапсуляция (BankAccount, Temperature)");

        Check.isTrue("BankAccount өрістері private", allFieldsPrivate(BankAccount.class));
        Check.isTrue("balance үшін setter жоқ", !hasMethod(BankAccount.class, "setBalance"));

        BankAccount account = new BankAccount("Aisha", 100_000);
        Check.eq("иесі сақталды", "Aisha", account.getOwner());
        Check.eq("бастапқы баланс", 100_000.0, account.getBalance(), 1e-9);

        account.deposit(50_000);
        Check.eq("deposit кейін баланс", 150_000.0, account.getBalance(), 1e-9);

        account.withdraw(30_000);
        Check.eq("withdraw кейін баланс", 120_000.0, account.getBalance(), 1e-9);

        Check.throwsEx("deposit(0) -> IllegalArgumentException",
                IllegalArgumentException.class, () -> account.deposit(0));
        Check.throwsEx("deposit(-100) -> IllegalArgumentException",
                IllegalArgumentException.class, () -> account.deposit(-100));
        Check.throwsEx("withdraw(-100) -> IllegalArgumentException",
                IllegalArgumentException.class, () -> account.withdraw(-100));
        Check.throwsEx("қаражат жетпейді -> IllegalStateException",
                IllegalStateException.class, () -> account.withdraw(999_999));
        Check.eq("сәтсіз операциялардан кейін баланс өзгермеді",
                120_000.0, account.getBalance(), 1e-9);

        Check.throwsEx("теріс бастапқы баланс -> IllegalArgumentException",
                IllegalArgumentException.class, () -> new BankAccount("Bolat", -1));
        Check.throwsEx("бос иесі -> IllegalArgumentException",
                IllegalArgumentException.class, () -> new BankAccount("", 100));

        Temperature warm = new Temperature(25);
        Check.eq("getCelsius()", 25.0, warm.getCelsius(), 1e-9);
        Check.eq("getFahrenheit()", 77.0, warm.getFahrenheit(), 1e-9);

        Temperature warmer = warm.plus(10);
        if (warmer == null) {
            Check.fail("plus() null қайтарды — жаңа Temperature қайтаруы керек");
        } else {
            Check.eq("plus() жаңа мән", 35.0, warmer.getCelsius(), 1e-9);
            Check.eq("түпнұсқа ӨЗГЕРМЕДІ", 25.0, warm.getCelsius(), 1e-9);
            Check.notSame("plus() жаңа объект қайтарады", warm, warmer);
        }
        Check.isTrue("Temperature өрістері final",
                allFieldsFinal(Temperature.class));
        Check.isTrue("Temperature-де setter жоқ", !hasAnySetter(Temperature.class));
    }

    private static boolean allFieldsPrivate(Class<?> type) {
        java.lang.reflect.Field[] fields = type.getDeclaredFields();
        if (fields.length == 0) {
            return false;
        }
        for (java.lang.reflect.Field f : fields) {
            if (!java.lang.reflect.Modifier.isPrivate(f.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    private static boolean allFieldsFinal(Class<?> type) {
        for (java.lang.reflect.Field f : type.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isFinal(f.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasMethod(Class<?> type, String name) {
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAnySetter(Class<?> type) {
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().startsWith("set")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
