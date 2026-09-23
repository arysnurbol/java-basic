package ch03.t20_exceptions;

import java.util.List;

/**
 * Тапсырма 20 — try-with-resources үшін ресурс.
 *
 * AutoCloseable іске асырған класты try(...) жақшасында жариялауға болады —
 * блок аяқталғанда (қалыпты да, exception-мен де) close() АВТОМАТТЫ шақырылады.
 *
 * Конструктор log-қа "open:" + name, ал close() "close:" + name жазсын.
 */
public class TrackedResource implements AutoCloseable {

    public String name;
    public List<String> log;

    public TrackedResource(String name, List<String> log) {
        this.name = name;
        this.log = log;
        this.log.add("open:" + name);
    }

    @Override
    public void close() {
        log.add("close:" + name);
    }
}
