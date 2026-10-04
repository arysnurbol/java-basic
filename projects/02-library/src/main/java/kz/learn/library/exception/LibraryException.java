package kz.learn.library.exception;

/**
 * Кітапхананың барлық бизнес-қателерінің ата-класы.
 *
 * Неге ортақ ата-класс: ConsoleApp (кейін Spring-тегі @ControllerAdvice) бір ғана
 * catch (LibraryException e) арқылы барлық домендік қатені ұстай алады,
 * ал NullPointerException сияқты бағдарламашы қателерін ұстамайды.
 */
public class LibraryException extends RuntimeException {

    public LibraryException(String message) {
        super(message);
    }
}
