package ch03.t18_annotations;

import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        return type.isAnnotationPresent(Table.class);
    }

    /**
     * Кесте аты: @Table бар болса — оның name() мәні,
     * жоқ болса — класс атының кіші әріппен жазылуы ("LogEntry" -> "logentry").
     */
    public static String tableName(Class<?> type) {
        if (type.isAnnotationPresent(Table.class)) {
            return type.getAnnotation(Table.class).name();
        }
        // Класс атын кіші әріпке ауыстыру ("LogEntry" -> "logentry")
        return type.getSimpleName().toLowerCase();
    }

    /** @Column жабыстырылған өрістердің баған аттары, СҰРЫПТАЛҒАН. */
    public static List<String> columnNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                // Тек @Column аннотациясы бар өрістерді сүзгіден өткіземіз (draftNote сияқтылар кірмейді)
                .filter(field -> field.isAnnotationPresent(Column.class))
                // Өрістің өз атын емес, аннотациядағы баған атын (name) аламыз
                .map(field -> field.getAnnotation(Column.class).name())
                .sorted()
                .collect(Collectors.toList());
    }

    /** nullable = false деп белгіленген бағандар, СҰРЫПТАЛҒАН. */
    public static List<String> requiredColumns(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .filter(field -> !field.getAnnotation(Column.class).nullable()) // nullable == false болса
                .map(field -> field.getAnnotation(Column.class).name())
                .sorted()
                .collect(Collectors.toList());
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
        String tName = tableName(type);

        String columnsDescription = Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> {
                    Column col = field.getAnnotation(Column.class);
                    // Егер nullable = false болса, жанына " NOT NULL" сөзін қосамыз
                    return col.name() + (col.nullable() ? "" : " NOT NULL");
                })
                // Бағандарды аты бойынша дұрыс сұрыптау үшін Stream ішінде sorted() қолданамыз
                .sorted()
                .collect(Collectors.joining(", "));

        return tName + "(" + columnsDescription + ")";
    }
}
