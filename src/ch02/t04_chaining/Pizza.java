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
        this("M", true, false);
    }

    /** Өлшемі берілген: ірімшікпен, пепперонисіз. */
    public Pizza(String size) {
        this(size, true, false);
    }

    /** Өлшемі мен ірімшігі берілген: пепперонисіз. */
    public Pizza(String size, boolean cheese) {
        this(size, cheese, false);
    }

    /** Негізгі конструктор — өрістерді МЕНШІКТЕУ тек осында болады. */
    public Pizza(String size, boolean cheese, boolean pepperoni) {
        this.size = size;
        this.cheese = cheese;
        this.pepperoni = pepperoni;
        createdCount++;
    }

    public String getSize() {
        return size;
    }

    /** "Pizza[size=M, cheese=true, pepperoni=false]" пішімі. */
    @Override
    public String toString() {
        return "Pizza[size=" + size + ", cheese=" +cheese + ", pepperoni=" +pepperoni + "]";
    }

    public static int getCreatedCount() {
        return createdCount;
    }

    public static void resetCreatedCount() {
        createdCount = 0;
    }
}
