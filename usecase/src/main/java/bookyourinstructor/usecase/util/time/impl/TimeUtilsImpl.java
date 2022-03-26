package bookyourinstructor.usecase.util.time.impl;

import bookyourinstructor.usecase.util.time.TimeUtils;

import java.sql.Time;
import java.time.*;
import java.util.Date;

public class TimeUtilsImpl implements TimeUtils {

    private static final Clock CLOCK = Clock.systemDefaultZone();
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Override
    public Instant nowInstant() {
        return Instant.now(CLOCK);
    }

    @Override
    public LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(CLOCK);
    }

    @Override
    public LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZONE_ID);
    }

    @Override
    public Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(createZoneOffset());
    }

    private ZoneOffset createZoneOffset() {
        Instant now = nowInstant();
        return ZONE_ID.getRules().getOffset(now);
    }
}
