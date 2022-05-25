package bookyourinstructor.usecase.util.time;

import java.time.*;

public interface TimeUtils {

    Instant nowInstant();

    LocalDateTime nowLocalDateTimeSystemZone();

    LocalDateTime toLocalDateTimeSystemZone(Instant instant);

    Instant toInstantFromSystemZone(LocalDateTime localDateTime);

    Instant toInstantFromUTCZone(LocalDateTime localDateTime);

    LocalDate findDayOfWeekAtOrAfterDate(DayOfWeek dayOfWeek, LocalDate date);
}
