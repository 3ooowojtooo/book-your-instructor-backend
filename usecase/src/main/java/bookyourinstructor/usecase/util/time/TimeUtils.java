package bookyourinstructor.usecase.util.time;

import java.time.*;

public interface TimeUtils {

    Instant nowInstant();

    LocalDateTime nowLocalDateTime();

    LocalDateTime toLocalDateTime(Instant instant);

    Instant toInstant(LocalDateTime localDateTime);

    OffsetDateTime toOffsetDataTime(LocalDateTime localDateTime);

    LocalDate findDayOfWeekAtOrAfterDate(DayOfWeek dayOfWeek, LocalDate date);
}
