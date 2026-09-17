package ch02.t01_class_object;

/**
 * Тапсырма 01 — Класс және объект.
 * Кітап: "Понимание классов и объектов" (69-б.)
 *
 * Шарттарды TASKS.md ішінен оқы.
 */
public class Book {

    // TODO: үш өріс жарияла: title (String), author (String), year (int).
    //       Өрістерді private қыл.

    public Book(String title, String author, int year) {
        // TODO: келген мәндерді өрістерге меншікте (this. қолдан).
    }

    /** "Java (Bachina, 2027)" пішімінде жол қайтарады. */
    public String describe() {
        // TODO
        return null;
    }

    /** Кітап көрсетілген жылдан бұрын шыққан ба. */
    public boolean isOlderThan(int otherYear) {
        // TODO
        return false;
    }

    /** Кітап неше жаста (берілген ағымдағы жылға қатысты). */
    public int ageIn(int currentYear) {
        // TODO
        return 0;
    }
}
