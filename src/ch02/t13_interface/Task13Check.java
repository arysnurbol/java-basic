package ch02.t13_interface;

import ch02.check.Check;

public class Task13Check {

    public static void run() {
        Check.task("Тапсырма 13 — Интерфейс (Payable)");

        Check.isTrue("Payable — интерфейс", Payable.class.isInterface());
        Check.eq("Payable.currency() статикалық методы", "KZT", Payable.currency());
        Check.eq("DEFAULT_TAX_RATE тұрақтысы", 0.10, Payable.DEFAULT_TAX_RATE, 1e-9);

        FullTimeEmployee aisha = new FullTimeEmployee("Aisha", 500_000);
        Check.eq("оклад жалақысы", 500_000.0, aisha.grossPay(), 1e-9);
        Check.eq("default netPay() 10% салық шегереді", 450_000.0, aisha.netPay(), 1e-9);

        Contractor bolat = new Contractor("Bolat", 160, 3_000);
        Check.eq("мердігердің gross жалақысы (160 x 3000)", 480_000.0, bolat.grossPay(), 1e-9);
        Check.eq("мердігер netPay()-ді қайта жазды (20%)", 384_000.0, bolat.netPay(), 1e-9);

        Payable[] payroll = {aisha, bolat};
        Check.eq("totalNetPay()", 834_000.0, PayrollUtils.totalNetPay(payroll), 1e-9);
        Check.eq("null -> 0", 0.0, PayrollUtils.totalNetPay(null), 1e-9);

        Check.isTrue("FullTimeEmployee netPay()-ді қайта жазбаған (default қолданады)",
                !declares(FullTimeEmployee.class, "netPay"));
        Check.isTrue("Contractor netPay()-ді қайта жазған",
                declares(Contractor.class, "netPay"));
    }

    private static boolean declares(Class<?> type, String methodName) {
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
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
