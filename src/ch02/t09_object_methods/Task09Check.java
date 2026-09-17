package ch02.t09_object_methods;

import ch02.check.Check;

public class Task09Check {

    public static void run() {
        Check.task("Тапсырма 09 — Object методтары және көпдеңгейлі мұрагерлік");

        Animal animal = new Animal("Rex");
        Dog dog = new Dog("Rex");
        Puppy puppy = new Puppy("Rex");

        Check.eq("Animal.toString()", "Animal(Rex)", animal.toString());
        Check.eq("Dog.toString() — класс аты өзгереді", "Dog(Rex)", dog.toString());
        Check.eq("Puppy.toString()", "Puppy(Rex)", puppy.toString());

        Check.eq("Animal.speak()", "...", animal.speak());
        Check.eq("Dog.speak()", "Woof", dog.speak());
        Check.eq("Puppy.speak()", "Yip", puppy.speak());
        Check.eq("Puppy.speakWithParent()", "Yip (әкесі: Woof)", puppy.speakWithParent());

        Check.eq("fetch() Dog-тан мұраға келді", "Rex доптың артынан жүгірді", puppy.fetch());
        Check.eq("Puppy-де fetch() қайта жазылмаған", 0,
                countDeclared(Puppy.class, "fetch"));

        Check.isTrue("бірдей аттағы екі Dog тең", dog.equals(new Dog("Rex")));
        Check.isFalse("аты бөлек -> тең емес", dog.equals(new Dog("Barsik")));
        Check.isFalse("Dog пен Puppy тең емес (типі бөлек)", dog.equals(puppy));
        Check.isFalse("null-мен тең емес", dog.equals(null));
        Check.isFalse("бөгде типпен тең емес", dog.equals("Rex"));

        Check.eq("тең объектілердің hashCode-ы да тең",
                dog.hashCode(), new Dog("Rex").hashCode());
        Check.isTrue("hashCode name-нен есептеледі (ұдайы 0 емес)",
                dog.hashCode() == "Rex".hashCode()
                        || dog.hashCode() == java.util.Objects.hash("Rex"));
    }

    private static int countDeclared(Class<?> type, String methodName) {
        int count = 0;
        for (java.lang.reflect.Method m : type.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
