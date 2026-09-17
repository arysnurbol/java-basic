package ch02.t12_abstract;

import ch02.check.Check;

public class Task12Check {

    public static void run() {
        Check.task("Тапсырма 12 — Абстрактілі класс (Vehicle)");

        Check.isTrue("Vehicle — abstract класс",
                java.lang.reflect.Modifier.isAbstract(Vehicle.class.getModifiers()));
        Check.isTrue("fuelCostPer100Km() — abstract метод", isAbstractMethod("fuelCostPer100Km"));
        Check.isFalse("tripCost() abstract ЕМЕС (ортақ логика)", isAbstractMethod("tripCost"));

        Car camry = new Car("Toyota Camry", 8, 1000);   // 8 л x 1000 ₸ = 8000 ₸ / 100 км
        Bicycle bike = new Bicycle("Trek");

        Check.eq("Car.fuelCostPer100Km()", 8000.0, camry.fuelCostPer100Km(), 1e-9);
        Check.eq("Bicycle.fuelCostPer100Km()", 0.0, bike.fuelCostPer100Km(), 1e-9);

        Check.eq("tripCost(250) == 20000", 20000.0, camry.tripCost(250), 1e-9);
        Check.eq("велосипедтің сапары тегін", 0.0, bike.tripCost(250), 1e-9);

        Check.eq("Car.describe()", "Toyota Camry: 100 км = 8000 ₸", camry.describe());
        Check.eq("Bicycle.describe()", "Trek: 100 км = 0 ₸", bike.describe());

        // Полиморфизм: екеуі де Vehicle ретінде өңделеді
        Vehicle[] fleet = {camry, bike};
        double total = 0;
        for (Vehicle v : fleet) {
            total += v.tripCost(100);
        }
        Check.eq("парк бойынша жалпы шығын", 8000.0, total, 1e-9);
    }

    private static boolean isAbstractMethod(String name) {
        for (java.lang.reflect.Method m : Vehicle.class.getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return java.lang.reflect.Modifier.isAbstract(m.getModifiers());
            }
        }
        return false;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
