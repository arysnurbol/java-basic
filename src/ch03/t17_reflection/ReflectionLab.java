package ch03.t17_reflection;

import java.util.List;

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
        return null; // TODO
    }

    /** Класта жарияланған методтардың аттары, СҰРЫПТАЛҒАН әрі қайталанбайтын. */
    public static List<String> methodNames(Class<?> type) {
        return null; // TODO
    }

    /** Ата-класының қысқа аты. Ата-класы болмаса (Object-тің өзі) — "—". */
    public static String superclassName(Class<?> type) {
        return null; // TODO
    }

    /**
     * Аргументтер САНЫ сәйкес келетін конструктор тауып, объект жасайды.
     * Кеңес: getDeclaredConstructors() бойынша цикл, getParameterCount() салыстыр,
     * сосын newInstance(args).
     * Сәйкес конструктор болмаса — NoSuchMethodException лақтыр.
     */
    public static Object createInstance(Class<?> type, Object... args) throws Exception {
        return null; // TODO
    }

    /**
     * Аты мен аргумент саны бойынша метод тауып, оны ШАҚЫРАДЫ.
     * Метод табылмаса — NoSuchMethodException.
     * void метод шақырылса — null қайтады (Method.invoke солай істейді).
     */
    public static Object invoke(Object target, String methodName, Object... args) throws Exception {
        return null; // TODO
    }

    /** private өрістің мәнін оқиды. Кеңес: getDeclaredField + setAccessible(true). */
    public static Object readField(Object target, String fieldName) throws Exception {
        return null; // TODO
    }

    /** private өрістің мәнін ЖАЗАДЫ — инкапсуляцияны айналып өтеді. */
    public static void writeField(Object target, String fieldName, Object value) throws Exception {
        // TODO
    }
}
