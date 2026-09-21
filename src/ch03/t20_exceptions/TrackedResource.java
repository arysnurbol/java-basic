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

    // TODO: name (String) және log (List<String>) өрістері

    public TrackedResource(String name, List<String> log) {
        // TODO: өрістерді меншікте әрі log-қа "open:" + name жаз
    }

    @Override
    public void close() {
        // TODO: log-қа "close:" + name жаз
    }
}
