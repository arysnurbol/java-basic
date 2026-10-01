package kz.learn.todo.exception;

/**
 * Тапсырманың дерегі қате болса немесе күйді ауыстыру ережеге қайшы болса лақтырылады.
 * RuntimeException-нан мұраланады — яғни unchecked: throws жазу міндетті емес.
 *
 * Хабарлама getMessage() арқылы қолжетімді болуы керек.
 */
public class InvalidTaskException extends RuntimeException {

    public InvalidTaskException(String message) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }
}
