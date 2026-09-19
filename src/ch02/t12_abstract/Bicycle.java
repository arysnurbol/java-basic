package ch02.t12_abstract;

public class Bicycle extends Vehicle {

    public Bicycle(String model) {
        super(model);
    }

    /** Велосипед отын жақпайды. */
    @Override
    public double fuelCostPer100Km() {
        return 0;
    }
}
