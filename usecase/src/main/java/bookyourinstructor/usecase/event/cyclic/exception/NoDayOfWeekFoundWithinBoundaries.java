package bookyourinstructor.usecase.event.cyclic.exception;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class NoDayOfWeekFoundWithinBoundaries extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "No day of week %s found within given dates: %s to %s";

    public NoDayOfWeekFoundWithinBoundaries(DayOfWeek dayOfWeek, LocalDate startBoundary, LocalDate endBoundary) {
        super(String.format(MESSAGE_TEMPLATE, dayOfWeek, startBoundary, endBoundary));
    }
}
