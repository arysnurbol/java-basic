package ch03.t18_annotations;

/**
 * Тапсырма 18 — өріске арналған аннотация.
 *
 * Мұнда да @Retention(RetentionPolicy.RUNTIME) және @Target(ElementType.FIELD) жетпейді.
 *
 * Назар аудар: nullable-дың default мәні бар, сондықтан оны жазбауға болады:
 *     @Column(name = "email")                    -> nullable = true
 *     @Column(name = "id", nullable = false)     -> nullable = false
 */
public @interface Column {

    String name();

    boolean nullable() default true;
}
