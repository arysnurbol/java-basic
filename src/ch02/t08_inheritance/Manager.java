package ch02.t08_inheritance;

/**
 * Тапсырма 08 — Employee-ден мұра алатын класс.
 *
 * ЕСКЕРТУ: name мен baseSalary-ді ҚАЙТА жарияламай, super(...) арқылы бер.
 */
public class Manager extends Employee {

    private final double bonus;

    public Manager(String name, double baseSalary, double bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
    }

    /** Оклад + бонус. super.getMonthlySalary() қайта қолданылуы керек. */
    @Override
    public double getMonthlySalary() {
        return super.getMonthlySalary() + bonus;
    }

    /** "Manager: Aisha" пішімі. */
    @Override
    public String getInfo() {
        return getClassName() + ": " + name;
    }

    public double getBonus() {
        return bonus;
    }
}
