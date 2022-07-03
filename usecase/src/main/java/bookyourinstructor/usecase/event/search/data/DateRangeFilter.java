package bookyourinstructor.usecase.event.search.data;

import lombok.Getter;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class DateRangeFilter {

    private final Instant from;
    private final Instant to;

    public DateRangeFilter(Instant from, Instant to) {
        validateConstructorArgs(from, to);
        this.from = from;
        this.to = to;
    }

    private static void validateConstructorArgs(Instant from, Instant to) {
        checkNotNull(from, "Date range filter - from cannot be null");
        checkNotNull(to, "Date range filter - to cannot be null");
        checkArgument(from.isBefore(to), "Date range filter - from must be before to");
    }
}
