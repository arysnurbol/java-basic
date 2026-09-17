package ch02.t08_inheritance;

/**
 * Тапсырма 08 — Мұрагерлік және super.
 * Кітап: "Наследование" (87-б.)
 */
public class Employee {

    protected final String name;
    protected final double baseSalary;

    public Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public String getName() {
        return name;
    }

    /** Базалық нұсқада — тек оклад. */
    public double getMonthlySalary() {
        // TODO: baseSalary қайтар
        return 0;
    }

    /** "Employee: Aisha" пішімі. */
    public String getInfo() {
        // TODO
        return null;
    }
}
