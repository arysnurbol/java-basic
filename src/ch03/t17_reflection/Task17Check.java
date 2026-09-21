package ch03.t17_reflection;

import ch03.check.Check;

import java.util.List;

public class Task17Check {

    public static void run() throws Exception {
        Check.task("Тапсырма 17 — Рефлексия (ReflectionLab)");

        Check.eq("fieldNames(Person) — private өрістер де көрінеді",
                List.of("age", "name"), ReflectionLab.fieldNames(Person.class));
        Check.eq("methodNames(Person)",
                List.of("getAge", "getName", "greet", "isAdult", "setName"),
                ReflectionLab.methodNames(Person.class));
        Check.eq("superclassName(Person)", "Object", ReflectionLab.superclassName(Person.class));
        Check.eq("superclassName(Object) -> \"—\"", "—", ReflectionLab.superclassName(Object.class));

        Object created = ReflectionLab.createInstance(Person.class, "Aisha", 25);
        Check.isTrue("createInstance() Person қайтарады", created instanceof Person);
        Check.eq("екі аргументті конструктор шақырылды", "Aisha", ReflectionLab.readField(created, "name"));
        Check.eq("age өрісі", 25, ReflectionLab.readField(created, "age"));

        Object empty = ReflectionLab.createInstance(Person.class);
        Check.eq("аргументсіз конструктор", "белгісіз", ReflectionLab.readField(empty, "name"));
        Check.throwsEx("сәйкес конструктор жоқ -> NoSuchMethodException",
                NoSuchMethodException.class, () -> ReflectionLab.createInstance(Person.class, 1, 2, 3));

        Check.eq("invoke(greet)", "Salem, men Aisha", ReflectionLab.invoke(created, "greet"));
        Check.eq("invoke(isAdult)", true, ReflectionLab.invoke(created, "isAdult"));
        Check.eq("void метод -> null", null, ReflectionLab.invoke(created, "setName", "Bolat"));
        Check.eq("setName шынымен әсер етті", "Salem, men Bolat", ReflectionLab.invoke(created, "greet"));
        Check.throwsEx("жоқ метод -> NoSuchMethodException",
                NoSuchMethodException.class, () -> ReflectionLab.invoke(created, "fly"));

        // Рефлексия инкапсуляцияны айналып өтеді: setter жоқ өрісті де өзгертеміз
        Person person = new Person("Dana", 10);
        Check.isFalse("бастапқыда ересек емес", person.isAdult());
        ReflectionLab.writeField(person, "age", 30);
        Check.isTrue("private age өрісі рефлексиямен өзгертілді", person.isAdult());
        Check.eq("readField(age)", 30, ReflectionLab.readField(person, "age"));

        Check.throwsEx("жоқ өріс -> NoSuchFieldException",
                NoSuchFieldException.class, () -> ReflectionLab.readField(person, "salary"));
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
