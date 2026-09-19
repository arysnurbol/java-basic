package ch02.t12_abstract;

public class Car extends Vehicle {

    private final double litersPer100Km;
    private final double pricePerLiter;

    public Car(String model, double litersPer100Km, double pricePerLiter) {
        super(model);
        this.litersPer100Km = litersPer100Km;
        this.pricePerLiter = pricePerLiter;
    }

    @Override
    public double fuelCostPer100Km() {
        return litersPer100Km *  pricePerLiter;
    }
}
