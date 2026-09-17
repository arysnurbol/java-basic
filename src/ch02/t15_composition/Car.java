package ch02.t15_composition;

/**
 * Тапсырма 15 — Car "has-a" Engine (композиция), "is-a" Engine ЕМЕС.
 *
 * ЖАСАМА: class Car extends Engine — бұл қате модель.
 * ЖАСА:   Car ішінде Engine өрісі болады, ал Car оған тапсырма береді (delegation).
 */
public class Car {

    private final String model;
    private final Engine engine;

    public Car(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    public String getModel() {
        return model;
    }

    /** Қозғалтқышқа тапсырма береді (delegation). */
    public String start() {
        // TODO: engine.start() қайтар
        return null;
    }

    public String stop() {
        // TODO
        return null;
    }

    public boolean isRunning() {
        // TODO
        return false;
    }

    /** "Toyota Camry [V6, 300 а.к.]" пішімі. */
    public String getSpec() {
        // TODO
        return null;
    }

    /**
     * Қозғалтқышты ауыстырып, ЖАҢА Car объектісін қайтарады.
     * Композицияның артықшылығы: мұрагерлікте бөлшекті осылай ауыстыра алмас едің.
     */
    public Car withEngine(Engine newEngine) {
        // TODO
        return null;
    }
}
