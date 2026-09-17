package ch02.t10_polymorphism;

/**
 * Тапсырма 10 — Орындалу кезіндегі полиморфизм.
 * Кітап: "Полиморфизм во время выполнения" (95-б.)
 */
public class Shape {

    private final String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /** Базалық фигураның ауданы белгісіз — 0. Ұрпақтары қайта анықтайды. */
    public double area() {
        return 0;
    }

    /** "Circle: 78.54" — ауданы 2 ондық белгіге дейін дөңгелектенген. */
    public String describe() {
        // TODO: String.format(java.util.Locale.ROOT, "%s: %.2f", getName(), area())
        //       Locale.ROOT қажет: онсыз Windows-тың орысша/қазақша локалінде
        //       нүктенің орнына үтір шығады ("16,00")
        return null;
    }
}
