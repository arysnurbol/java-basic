package ch03.t18_annotations;

import ch03.check.Check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class Task18Check {

    public static void run() {
        Check.task("Тапсырма 18 — Пайдаланушы аннотациялары (AnnotationProcessor)");

        // Ең алдымен: аннотациялар орындалу кезінде КӨРІНЕ МЕ?
        boolean tableRuntime = isRuntime(Table.class);
        boolean columnRuntime = isRuntime(Column.class);
        Check.isTrue("@Table-да @Retention(RUNTIME) бар", tableRuntime);
        Check.isTrue("@Column-да @Retention(RUNTIME) бар", columnRuntime);
        if (!tableRuntime || !columnRuntime) {
            Check.info("@Retention(RetentionPolicy.RUNTIME) қосылмаса, рефлексия аннотацияны");
            Check.info("МҮЛДЕ көрмейді — төмендегі тексерістердің бәрі сол себептен құлайды.");
        }

        Check.isTrue("hasTable(User)", AnnotationProcessor.hasTable(User.class));
        Check.isFalse("hasTable(LogEntry)", AnnotationProcessor.hasTable(LogEntry.class));

        Check.eq("tableName(User) — @Table-дан", "users", AnnotationProcessor.tableName(User.class));
        Check.eq("tableName(LogEntry) — әдепкі", "logentry", AnnotationProcessor.tableName(LogEntry.class));

        Check.eq("columnNames(User) — @Column жоқ өріс кірмейді",
                List.of("email", "id", "nickname"), AnnotationProcessor.columnNames(User.class));
        Check.eq("columnNames(LogEntry)", List.of("message"),
                AnnotationProcessor.columnNames(LogEntry.class));
        Check.eq("columnNames(NoColumns) — бағаны жоқ класс", List.of(),
                AnnotationProcessor.columnNames(NoColumns.class));

        Check.eq("requiredColumns(User) — nullable = false болғандары",
                List.of("email", "id"), AnnotationProcessor.requiredColumns(User.class));
        Check.eq("requiredColumns(LogEntry) — nullable әдепкісі true", List.of(),
                AnnotationProcessor.requiredColumns(LogEntry.class));

        Check.eq("describe(User)", "users(email NOT NULL, id NOT NULL, nickname)",
                AnnotationProcessor.describe(User.class));
        Check.eq("describe(LogEntry)", "logentry(message)", AnnotationProcessor.describe(LogEntry.class));
        Check.eq("describe(бағансыз класс)", "nocolumns()", AnnotationProcessor.describe(NoColumns.class));
    }

    /** Аннотацияның өзінде @Retention(RUNTIME) тұр ма — соны тексереді. */
    private static boolean isRuntime(Class<?> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        return retention != null && retention.value() == RetentionPolicy.RUNTIME;
    }

    /** Ешқандай бағаны жоқ класс. */
    private static class NoColumns {
        private int something;
    }

    public static void main(String[] args) {
        run();
        System.exit(Check.summary());
    }
}
