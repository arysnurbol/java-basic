package ch03.t18_annotations;

import java.util.List;

/**
 * Тапсырма 18 — аннотацияларды рефлексиямен өңдеу.
 * Кітап: "Обработка аннотаций с помощью рефлексии",
 *        "Распространенные варианты использования" (147–148 б.)
 *
 * Бұл — Hibernate/JPA-ның @Entity/@Column-ды қалай оқитынының шағын үлгісі.
 *
 * НЕГІЗГІ ӘДІСТЕР:
 *   type.isAnnotationPresent(Table.class)
 *   Table t = type.getAnnotation(Table.class);   t.name()
 *   field.getAnnotation(Column.class)
 *
 * НАЗАР АУДАР: getDeclaredFields() қайтаратын РЕТ кепілдендірілмеген,
 * сондықтан нәтижені әрқашан баған АТЫ бойынша сұрыпта — сонда тұрақты болады.
 */
public class AnnotationProcessor {

    /** Класта @Table бар ма. */
    public static boolean hasTable(Class<?> type) {
        return false; // TODO
    }

    /**
     * Кесте аты: @Table бар болса — оның name() мәні,
     * жоқ болса — класс атының кіші әріппен жазылуы ("LogEntry" -> "logentry").
     */
    public static String tableName(Class<?> type) {
        return null; // TODO
    }

    /** @Column жабыстырылған өрістердің баған аттары, СҰРЫПТАЛҒАН. */
    public static List<String> columnNames(Class<?> type) {
        return null; // TODO
    }

    /** nullable = false деп белгіленген бағандар, СҰРЫПТАЛҒАН. */
    public static List<String> requiredColumns(Class<?> type) {
        return null; // TODO
    }

    /**
     * Толық сипаттама: кестеАты(баған1, баған2, ...).
     * Бағандар аты бойынша сұрыпталған; nullable = false болса " NOT NULL" қосылады.
     *
     *   User     -> "users(email NOT NULL, id NOT NULL, nickname)"
     *   LogEntry -> "logentry(message)"
     *   бағансыз -> "аты()"
     */
    public static String describe(Class<?> type) {
        return null; // TODO
    }
}
