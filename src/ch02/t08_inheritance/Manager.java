package ch02.t08_inheritance;

/**
 * Тапсырма 08 — Employee-ден мұра алатын класс.
 *
 * ЕСКЕРТУ: name мен baseSalary-ді ҚАЙТА жарияламай, super(...) арқылы бер.
 */
public class Manager extends Employee {

    private final double bonus;

    public Manager(String name, double baseSalary, double bonus) {
        // TODO: super(...) шақыр (ол ең бірінші жол болуы керек), сосын bonus-ты меншікте
        super(name, baseSalary);
        this.bonus = 0;
    }

    /** Оклад + бонус. super.getMonthlySalary() қайта қолданылуы керек. */
    @Override
    public double getMonthlySalary() {
        // TODO
        return 0;
    }

    /** "Manager: Aisha" пішімі. */
    @Override
    public String getInfo() {
        // TODO
        return null;
    }

    public double getBonus() {
        return bonus;
    }
}
