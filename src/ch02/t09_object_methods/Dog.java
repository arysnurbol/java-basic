package ch02.t09_object_methods;

/** Тапсырма 09 — бірінші деңгей: Animal -> Dog. */
public class Dog extends Animal {

    public Dog(String name) {
        super(name);
    }

    @Override
    public String speak() {
        return "Woof";
    }

    public String fetch() {
        return getName() + " доптың артынан жүгірді";
    }
}
