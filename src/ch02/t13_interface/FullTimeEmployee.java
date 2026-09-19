package ch02.t13_interface;

/** Айлық оклад алатын қызметкер. netPay() әдепкі нұсқасын қолданады. */
public class FullTimeEmployee implements Payable {

    private final String name;
    private final double monthlySalary;

    public FullTimeEmployee(String name, double monthlySalary) {
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public String getName() {
        return name;
    }

    @Override
    public double grossPay() {
        return monthlySalary;
    }
}
