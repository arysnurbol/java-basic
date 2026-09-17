package ch02.t01_class_object;

/**
 * Тапсырма 01 — Класс және объект.
 * Кітап: "Понимание классов и объектов" (69-б.)
 *
 * Шарттарды TASKS.md ішінен оқы.
 */
public class Book {

    private String title;
    private String author;
    private int year;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    /** "Java (Bachina, 2027)" пішімінде жол қайтарады. */
    public String describe() {
        return this.title + " (" + this.author + ", " + this.year + ")";
    }

    /** Кітап көрсетілген жылдан бұрын шыққан ба. */
    public boolean isOlderThan(int otherYear) {
        return this.year < otherYear;
    }

    /** Кітап неше жаста (берілген ағымдағы жылға қатысты). */
    public int ageIn(int currentYear) {
        if  (this.year < currentYear) {
            return currentYear - this.year;
        }
        return 0;
    }
}
