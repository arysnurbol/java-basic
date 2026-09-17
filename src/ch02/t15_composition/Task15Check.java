package ch02.t15_composition;

import ch02.check.Check;

public class Task15Check {

    public static void run() {
        Check.task("Тапсырма 15 — Композиция (Car has-a Engine)");

        Check.isTrue("Car класы Engine-ден мұра АЛМАЙДЫ",
                Car.class.getSuperclass() == Object.class);
        Check.isTrue("Car ішінде Engine типті өріс бар", hasEngineField());

        Engine v6 = new Engine("V6", 300);
        Car camry = new Car("Toyota Camry", v6);

        Check.isFalse("басында қозғалтқыш өшік", camry.isRunning());
        Check.eq("start() қозғалтқышқа тапсырылды", "V6 іске қосылды", camry.start());
        Check.isTrue("енді қозғалтқыш жұмыс істеп тұр", camry.isRunning());
        Check.isTrue("Engine объектісінің күйі де өзгерді", v6.isRunning());

        Check.eq("stop()", "V6 тоқтады", camry.stop());
        Check.isFalse("қозғалтқыш тоқтады", camry.isRunning());

        Check.eq("getSpec()", "Toyota Camry [V6, 300 а.к.]", camry.getSpec());

        Car hybrid = camry.withEngine(new Engine("Hybrid", 220));
        Check.isTrue("withEngine() null емес", hybrid != null);
        Check.notSame("withEngine() жаңа Car қайтарады", camry, hybrid);
        if (hybrid != null) {
            Check.eq("жаңа қозғалтқышпен spec", "Toyota Camry [Hybrid, 220 а.к.]", hybrid.getSpec());
            Check.eq("ескі Car өзгермеді", "Toyota Camry [V6, 300 а.к.]", camry.getSpec());
        }
    }

    private static boolean hasEngineField() {
        for (java.lang.reflect.Field f : Car.class.getDeclaredFields()) {
            if (f.getType() == Engine.class) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
