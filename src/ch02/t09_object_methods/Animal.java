package ch02.t09_object_methods;

import java.util.Objects;

/**
 * Тапсырма 09 — Object класы (toString, equals, hashCode) және көпдеңгейлі мұрагерлік.
 * Кітап: "Класс Object" (92-б.), "Многоуровневое наследование" (90-б.)
 */
public class Animal {

    private final String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String speak() {
        return "...";
    }

    /**
     * "Animal(Rex)" немесе "Dog(Rex)" пішімі — КЛАСС АТЫ нақты типке қарай өзгеруі керек.
     * Кеңес: getClass().getSimpleName() қолдан, класс атын қолмен жазба.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + name + ")";
    }

    /**
     * Екі жануар тең: НАҚТЫ типі бірдей ЖӘНЕ аты бірдей болса.
     * Кеңес: null тексерісі, getClass() салыстыруы, сосын name.equals(...).
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Animal animal = (Animal) other;
        return Objects.equals(name, animal.name);
    }

    /** equals() тең болса, hashCode() да тең болуы керек. */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
