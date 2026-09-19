package ch02.t12_abstract;

/**
 * Тапсырма 12 — Абстракция және абстрактілі класс.
 * Кітап: "Абстракция" (99-б.), "Полиморфизм с абстрактными классами" (98-б.)
 *
 * Абстрактілі класстың МӘНІ: ортақ мінез-құлықты бір жерде жазып,
 * ал әр ұрпаққа тән бөлікті abstract метод ретінде қалдыру.
 */
public abstract class Vehicle {

    private final String model;

    protected Vehicle(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    /** 100 км-ге кететін отын шығыны, теңгемен. Әр көлікте өзінше. */
    public abstract double fuelCostPer100Km();

    /** Барлық көлікке ортақ логика — abstract методты пайдаланады. */
    public double tripCost(double km) {
        return (fuelCostPer100Km() / 100.0) * km;
    }

    /** "Toyota Camry: 100 км = 8000 ₸" пішімі (бүтін санға дөңгелектенген). */
    public String describe() {
        return model + ": 100 км = " + Math.round(fuelCostPer100Km()) + " ₸";
    }
}
