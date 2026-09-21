package ch03.t18_annotations;

/**
 * Тапсырма 18 — @Table аннотациясы ЖОҚ класс (әдепкі мінезді тексеру үшін).
 * Бұл файлды ӨЗГЕРТПЕ.
 */
public class LogEntry {

    @Column(name = "message")
    private String message;

    private int ignored;
}
