package ch03.t18_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Тапсырма 18 — өріске арналған аннотация.
 *
 * Мұнда да @Retention(RetentionPolicy.RUNTIME) және @Target(ElementType.FIELD) жетпейді.
 *
 * Назар аудар: nullable-дың default мәні бар, сондықтан оны жазбауға болады:
 *     @Column(name = "email")                    -> nullable = true
 *     @Column(name = "id", nullable = false)     -> nullable = false
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name();

    boolean nullable() default true;
}
