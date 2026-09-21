package ch03.t20_exceptions;

import ch03.check.Check;

import java.util.ArrayList;
import java.util.List;

public class Task20Check {

    public static void run() throws Exception {
        Check.task("Тапсырма 20 — Ерекше жағдайлар (ExceptionLab)");

        Check.eq("parseOrDefault(\"42\", 0)", 42, ExceptionLab.parseOrDefault("42", 0));
        Check.eq("parseOrDefault(\"abc\", -1)", -1, ExceptionLab.parseOrDefault("abc", -1));
        Check.eq("parseOrDefault(null, 7)", 7, ExceptionLab.parseOrDefault(null, 7));

        Check.eq("executionOrder(false)", List.of("try", "finally"), ExceptionLab.executionOrder(false));
        Check.eq("executionOrder(true) — finally exception кезінде де орындалады",
                List.of("try", "catch", "finally"), ExceptionLab.executionOrder(true));

        Check.eq("classify(\"42\")", "42", ExceptionLab.classify("42"));
        Check.eq("classify(\"abc\")", "san emes", ExceptionLab.classify("abc"));
        Check.eq("classify(null)", "null", ExceptionLab.classify(null));

        Check.eq("withdraw(1000, 300)", 700.0, ExceptionLab.withdraw(1000, 300), 1e-9);
        Check.eq("withdraw(300, 300) — дәл сонша болса өтеді", 0.0,
                ExceptionLab.withdraw(300, 300), 1e-9);
        Check.throwsEx("withdraw(100, -1) -> IllegalArgumentException (unchecked)",
                IllegalArgumentException.class, () -> ExceptionLab.withdraw(100, -1));
        Check.throwsEx("withdraw(300, 500) -> InsufficientFundsException (checked)",
                InsufficientFundsException.class, () -> ExceptionLab.withdraw(300, 500));

        try {
            ExceptionLab.withdraw(300, 500);
            Check.fail("withdraw(300, 500) exception лақтыруы керек еді");
        } catch (InsufficientFundsException e) {
            Check.eq("getRequested()", 500.0, e.getRequested(), 1e-9);
            Check.eq("getAvailable()", 300.0, e.getAvailable(), 1e-9);
            Check.eq("getShortfall()", 200.0, e.getShortfall(), 1e-9);
            Check.eq("хабарлама", "Qarajat jetkiliksiz: surangan 500.0, bar 300.0", e.getMessage());
            Check.isFalse("InsufficientFundsException — CHECKED (RuntimeException ЕМЕС)",
                    RuntimeException.class.isAssignableFrom(e.getClass()));
        }

        Check.eq("resourceOrder() — жабылу реті КЕРІСІНШЕ",
                List.of("open:A", "open:B", "body", "close:B", "close:A"),
                ExceptionLab.resourceOrder());

        List<String> log = new ArrayList<>();
        try (TrackedResource r = new TrackedResource("X", log)) {
            Check.eq("конструктор log-қа жазды", List.of("open:X"), log);
        }
        Check.eq("блоктан шыққанда close() автоматты шақырылды", List.of("open:X", "close:X"), log);

        IllegalStateException wrapped = ExceptionLab.wrapFailure("abc");
        Check.notNull("wrapFailure(\"abc\") null емес", wrapped);
        if (wrapped != null) {
            Check.eq("хабарлама", "Baptau qate: abc", wrapped.getMessage());
            Check.isTrue("СЕБЕП сақталды (getCause)", wrapped.getCause() instanceof NumberFormatException);
        }
        Check.eq("wrapFailure(\"42\") -> null (қате жоқ)", null, ExceptionLab.wrapFailure("42"));
    }

    public static void main(String[] args) throws Exception {
        run();
        System.exit(Check.summary());
    }
}
