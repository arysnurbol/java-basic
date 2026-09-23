package ch03.t17_reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Тапсырма 17 — Рефлексия.
 * Кітап: "Работа с рефлексией", "Получение информации о классе",
 *        "Динамический вызов методов" (143–146 б.)
 *
 * Рефлексия — бағдарламаның ӨЗ құрылымын орындалу кезінде оқуы және өзгертуі.
 * Осы арқылы Spring bean жасайды, Jackson JSON-ды объектіге айналдырады,
 * JUnit @Test әдістерін табады.
 *
 * НЕГІЗГІ КІРУ НҮКТЕЛЕРІ:
 *   Class<?> c = obj.getClass();  |  Person.class  |  Class.forName("ch03...Person")
 *   c.getDeclaredFields()      осы класта жарияланған БАРЛЫҚ өріс (private-пен қоса)
 *   c.getFields()              тек public, мұрагерлікпен келгенін ҚОСА
 *   c.getDeclaredMethods()     осы класта жарияланған методтар
 *   c.getDeclaredConstructor(...).newInstance(...)
 *   member.setAccessible(true) private-қа кіру рұқсаты
 *
 * ЕСКЕРТУ: рефлексия баяу, компилятор оны тексермейді (атын қате жазсаң —
 * қате тек орындалу кезінде шығады) әрі инкапсуляцияны бұзады.
 * Сондықтан оны кітапхана жазғанда ғана қолдан, күнделікті кодта емес.
 */
public class ReflectionLab {

    /** Класта жарияланған өрістердің аттары, СҰРЫПТАЛҒАН. private-тар да кіреді. */
    public static List<String> fieldNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .map(Field::getName) // Field-тен атын (String) аламыз
                .sorted()            // Сұрыптаймыз
                .collect(Collectors.toList()); // Тізімге жинаймыз
    }

    /** Класта жарияланған методтардың аттары, СҰРЫПТАЛҒАН әрі қайталанбайтын. */
    public static List<String> methodNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .map(Method::getName) // Method-тан атын аламыз
                .distinct()           // Қайталанбайтындарды ғана қалдырамыз
                .sorted()             // Сұрыптаймыз
                .collect(Collectors.toList());
    }

    /** Ата-класының қысқа аты. Ата-класы болмаса (Object-тің өзі) — "—". */
    public static String superclassName(Class<?> type) {
        Class<?> superclass = type.getSuperclass();
        if (superclass == null) {
            return "—";
        }
        return superclass.getSimpleName(); // Қысқа атын алу
    }

    /**
     * Аргументтер САНЫ сәйкес келетін конструктор тауып, объект жасайды.
     * Кеңес: getDeclaredConstructors() бойынша цикл, getParameterCount() салыстыр,
     * сосын newInstance(args).
     * Сәйкес конструктор болмаса — NoSuchMethodException лақтыр.
     */
    public static Object createInstance(Class<?> type, Object... args) throws Exception {
        int targetCount = (args == null) ? 0 : args.length;

        // Барлық конструкторларды аралап, аргумент саны сәйкес келетінін іздейміз
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == targetCount) {
                constructor.setAccessible(true); // private болса да рұқсат беру
                return constructor.newInstance(args); // Объект құрастыру
            }
        }
        // Егер ешқандай конструктор табылмаса
        throw new NoSuchMethodException("Сәйкес аргумент саны бар конструктор табылмады: " + targetCount);
    }

    /**
     * Аты мен аргумент саны бойынша метод тауып, оны ШАҚЫРАДЫ.
     * Метод табылмаса — NoSuchMethodException.
     * void метод шақырылса — null қайтады (Method.invoke солай істейді).
     */
    public static Object invoke(Object target, String methodName, Object... args) throws Exception {
        int targetCount = (args == null) ? 0 : args.length;
        Class<?> type = target.getClass();

        // Кластың барлық әдістерінің ішінен аты мен аргумент саны сәйкесін іздейміз
        for (Method method : type.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == targetCount) {
                method.setAccessible(true);
                return method.invoke(target, args); // void болса автоматты түрде null қайтады
            }
        }
        throw new NoSuchMethodException("Метод табылмады: " + methodName + " аругмент саны: " + targetCount);
    }

    /** private өрістің мәнін оқиды. Кеңес: getDeclaredField + setAccessible(true). */
    public static Object readField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Бірінші рұқсат аламыз
        return field.get(target);   // Сосын мәнін оқимыз
    }

    /** private өрістің мәнін ЖАЗАДЫ — инкапсуляцияны айналып өтеді. */
    public static void writeField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Міндетті түрде рұқсат ашу
        field.set(target, value);   // setValue емес, set(нысан, мән) қолданылады
    }
}
