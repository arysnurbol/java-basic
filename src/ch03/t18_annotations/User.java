package ch03.t18_annotations;

/**
 * Тапсырма 18 — аннотацияланған класс. Бұл файлды ӨЗГЕРТПЕ.
 * AnnotationProcessor осыны оқып, кестенің сипаттамасын құрастырады.
 */
@Table(name = "users")
public class User {

    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname")
    private String nickname;

    /** @Column ЖОҚ — бұл өріс баған емес, тізімге кірмеуі керек. */
    private String draftNote;

    public long getId() {
        return id;
    }
}
