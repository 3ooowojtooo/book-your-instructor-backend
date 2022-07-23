package bookyourinstructor.usecase.util.time.impl;

import bookyourinstructor.usecase.util.time.TimeUtils;

import java.time.*;

public enum TimeUtilsImpl implements TimeUtils {
    INSTANCE;

    private static final Clock CLOCK = Clock.systemDefaultZone();
    private static final ZoneId SYSTEM_ZONE_ID = ZoneId.systemDefault();

    @Override
    public Instant nowInstant() {
        return Instant.now(CLOCK);
    }

    @Override
    public LocalDateTime nowLocalDateTimeSystemZone() {
        return LocalDateTime.now(CLOCK);
    }

    @Override
    public LocalDateTime toLocalDateTimeSystemZone(Instant instant) {
        return LocalDateTime.ofInstant(instant, SYSTEM_ZONE_ID);
    }

    @Override
    public LocalDateTime toLocalDateTimeUTCZone(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public LocalDate toLocalDateUTCZone(Instant instant) {
        return LocalDate.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public Instant toInstantFromSystemZone(LocalDateTime localDateTime) {
        return localDateTime.toInstant(createZoneOffset());
    }

    @Override
    public Instant toInstantFromUTCZone(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    @Override
    public LocalDate findDayOfWeekAtOrAfterDate(DayOfWeek dayOfWeek, LocalDate date) {
        LocalDate currentDate = date;
        while (currentDate.getDayOfWeek() != dayOfWeek) {
            currentDate = currentDate.plusDays(1);
        }
        return currentDate;
    }

    private ZoneOffset createZoneOffset() {
        Instant now = nowInstant();
        return SYSTEM_ZONE_ID.getRules().getOffset(now);
    }
}
