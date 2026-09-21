package ch03.t17_reflection;

/**
 * Тапсырма 17 — рефлексияның НЫСАНАСЫ. Бұл файлды ӨЗГЕРТПЕ.
 * ReflectionLab осы класты «сырттан», тек Class объектісі арқылы зерттейді.
 */
public class Person {

    private String name;
    private int age;

    public Person() {
        this("белгісіз", 0);
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String greet() {
        return "Salem, men " + name;
    }

    public boolean isAdult() {
        return age >= 18;
    }
}
