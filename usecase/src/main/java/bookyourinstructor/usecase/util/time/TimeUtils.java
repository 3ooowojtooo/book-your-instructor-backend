package bookyourinstructor.usecase.util.time;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimeUtils {

    Instant nowInstant();

    LocalDateTime nowLocalDateTimeSystemZone();

    LocalDateTime toLocalDateTimeSystemZone(Instant instant);

    LocalDateTime toLocalDateTimeUTCZone(Instant instant);

    LocalDate toLocalDateUTCZone(Instant instant);

    Instant toInstantFromSystemZone(LocalDateTime localDateTime);

    Instant toInstantFromUTCZone(LocalDateTime localDateTime);

    LocalDate findDayOfWeekAtOrAfterDate(DayOfWeek dayOfWeek, LocalDate date);
}
