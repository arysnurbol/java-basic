package ch02.t09_object_methods;

/**
 * Тапсырма 09 — екінші деңгей: Animal -> Dog -> Puppy (көпдеңгейлі мұрагерлік).
 * fetch() методын ҚАЙТА ЖАЗБА — ол Dog-тан мұраға келеді.
 */
public class Puppy extends Dog {

    public Puppy(String name) {
        super(name);
    }

    @Override
    public String speak() {
        return "Yip";
    }

    /** "Yip (әкесі: Woof)" — super.speak() арқылы ата-класстың нұсқасын ал. */
    public String speakWithParent() {
        // TODO
        return speak() + " (әкесі: " + super.speak() + ")";
    }
}
