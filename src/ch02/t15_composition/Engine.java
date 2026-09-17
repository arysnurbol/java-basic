package ch02.t15_composition;

/**
 * Тапсырма 15 — Мұрагерліктің орнына композиция.
 * Кітап: "Композиция вместо наследования" (108-б.)
 */
public class Engine {

    private final String type;
    private final int horsePower;
    private boolean running;

    public Engine(String type, int horsePower) {
        this.type = type;
        this.horsePower = horsePower;
    }

    public String getType() {
        return type;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public boolean isRunning() {
        return running;
    }

    /** Іске қосылды деп белгілейді және "V6 іске қосылды" қайтарады. */
    public String start() {
        // TODO
        return null;
    }

    /** Тоқтатылды деп белгілейді және "V6 тоқтады" қайтарады. */
    public String stop() {
        // TODO
        return null;
    }
}
