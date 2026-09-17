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
        // TODO: km / 100 * fuelCostPer100Km()
        return 0;
    }

    /** "Toyota Camry: 100 км = 8000 ₸" пішімі (бүтін санға дөңгелектенген). */
    public String describe() {
        // TODO: Math.round(fuelCostPer100Km()) қолдан
        return null;
    }
}
