package ch02.t04_chaining;

/**
 * Тапсырма 04 — Конструкторларды тізбектеу (this(...)) және объектінің өмірлік циклі.
 * Кітап: "Конструкторы и жизненный цикл объекта" (77-80-б.)
 *
 * МАҢЫЗДЫ: createdCount-ты ТЕК бір ғана (ең толық) конструкторда өсір.
 * Қалған конструкторлар this(...) арқылы соған жүгінуі керек.
 */
public class Pizza {

    private static int createdCount = 0;

    private String size;
    private boolean cheese;
    private boolean pepperoni;

    /** Әдепкі: "M", ірімшікпен, пепперонисіз. */
    public Pizza() {
        // TODO: this("M", true, false) арқылы негізгі конструкторға жүгін
    }

    /** Өлшемі берілген: ірімшікпен, пепперонисіз. */
    public Pizza(String size) {
        // TODO: this(...) арқылы негізгі конструкторға жүгін
    }

    /** Өлшемі мен ірімшігі берілген: пепперонисіз. */
    public Pizza(String size, boolean cheese) {
        // TODO: this(...) арқылы негізгі конструкторға жүгін
    }

    /** Негізгі конструктор — өрістерді МЕНШІКТЕУ тек осында болады. */
    public Pizza(String size, boolean cheese, boolean pepperoni) {
        // TODO: үш өрісті меншікте және createdCount-ты 1-ге өсір
    }

    public String getSize() {
        // TODO
        return null;
    }

    /** "Pizza[size=M, cheese=true, pepperoni=false]" пішімі. */
    @Override
    public String toString() {
        // TODO
        return null;
    }

    public static int getCreatedCount() {
        return createdCount;
    }

    public static void resetCreatedCount() {
        createdCount = 0;
    }
}
