package ch02.t08_inheritance;

import ch02.check.Check;

public class Task08Check {

    public static void run() {
        Check.task("Тапсырма 08 — Мұрагерлік және super (Employee / Manager)");

        Employee worker = new Employee("Aisha", 400_000);
        Check.eq("Employee жалақысы == оклад", 400_000.0, worker.getMonthlySalary(), 1e-9);
        Check.eq("Employee.getInfo()", "Employee: Aisha", worker.getInfo());

        Manager boss = new Manager("Bolat", 600_000, 150_000);
        Check.eq("Manager жалақысы == оклад + бонус", 750_000.0, boss.getMonthlySalary(), 1e-9);
        Check.eq("Manager.getInfo()", "Manager: Bolat", boss.getInfo());
        Check.eq("bonus сақталды", 150_000.0, boss.getBonus(), 1e-9);
        Check.eq("getName() ата-класстан мұраға алынды", "Bolat", boss.getName());

        // Полиморфизм: сілтеменің типі Employee, бірақ шақырылатын метод — Manager-дікі
        Employee asEmployee = boss;
        Check.eq("Employee сілтемесі арқылы да Manager методы шақырылады",
                750_000.0, asEmployee.getMonthlySalary(), 1e-9);

        Check.isTrue("Manager — Employee-дің ұрпағы", Employee.class.isAssignableFrom(Manager.class));
        Check.eq("Manager-де тек bonus өрісі жаңа (name/baseSalary қайта жарияланбаған)",
                1, Manager.class.getDeclaredFields().length);
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
