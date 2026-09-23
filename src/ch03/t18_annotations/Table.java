package ch03.t18_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Тапсырма 18 — класқа арналған аннотация.
 * Кітап: "Понятие аннотаций", "Пользовательские аннотации" (146–147 б.)
 *
 * !!! БҰЛ ЖЕРДЕ ЕҢ БАСТЫ ҚАДАМ ЖОҚ !!!
 *
 * Аннотацияның ӘДЕПКІ өмір сүру мерзімі — RetentionPolicy.CLASS.
 * Ол .class файлға жазылады, бірақ JVM оны жадыға ЖҮКТЕМЕЙДІ.
 * Сондықтан рефлексия оны ТАППАЙДЫ — бүкіл тексеріс құлайды.
 *
 * Қосатының:
 *     @Retention(RetentionPolicy.RUNTIME)   // орындалу кезінде көрінсін
 *     @Target(ElementType.TYPE)             // тек класқа жабыстыруға болады
 *
 * Алдымен ҚОСПАЙ жүгіртіп көр — тексеріс не дейтінін өз көзіңмен көр.
 * Сосын қосып, айырмашылығын байқа. Бұл — аннотациялардағы №1 тұзақ.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    String name();
}
