package bookyourinstructor.usecase.util.time;

import java.time.Instant;
import java.time.LocalDateTime;

public interface TimeUtils {

    Instant nowInstant();

    LocalDateTime nowLocalDateTime();

    LocalDateTime toLocalDateTime(Instant instant);

    Instant toInstant(LocalDateTime localDateTime);
}
