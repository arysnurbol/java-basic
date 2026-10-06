package kz.learn.hotel.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Тесттегі «уақыт машинасы»: сағатты қолмен жылжытамыз.
 * BankService уақытты LocalDateTime.now() арқылы ЕМЕС, берілген Clock-тан алатындықтан ғана мүмкін.
 */
final class TestClock extends Clock {

    private Instant now;

    TestClock(LocalDateTime start) {
        set(start);
    }

    void set(LocalDateTime time) {
        now = time.toInstant(ZoneOffset.UTC);
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return now;
    }
}
