package kz.learn.todo.model;

/**
 * Маңыздылық деңгейі. ДАЙЫН — өрісі және конструкторы бар enum-ның үлгісі.
 * Status-ты жазғанда осыған қара.
 */
public enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    private final int weight;

    Priority(int weight) {
        this.weight = weight;
    }

    /** Сұрыптау үшін: салмағы үлкен — маңыздырақ. */
    public int getWeight() {
        return weight;
    }
}
