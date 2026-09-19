package ch02.t13_interface;

/**
 * Сағатпен жұмыс істейтін мердігер.
 * Мердігердің салығы 20% — сондықтан netPay() ҚАЙТА ЖАЗЫЛАДЫ.
 */
public class Contractor implements Payable {

    private final String name;
    private final int hours;
    private final double hourlyRate;

    public Contractor(String name, int hours, double hourlyRate) {
        this.name = name;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
    }

    public String getName() {
        return name;
    }

    @Override
    public double grossPay() {
        return  hourlyRate * hours;
    }

    /** 20% салық. */
    @Override
    public double netPay() {
        return grossPay() * (1 - 0.20);
    }
}
