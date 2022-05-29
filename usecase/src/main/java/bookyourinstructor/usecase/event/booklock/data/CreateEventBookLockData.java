package bookyourinstructor.usecase.event.booklock.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class CreateEventBookLockData {

    private final Integer eventId;
    private final Integer eventVersion;
    private final Integer studentId;

    public CreateEventBookLockData(Integer eventId, Integer eventVersion, Integer studentId) {
        validateConstructorArgs(eventId, eventVersion, studentId);
        this.eventId = eventId;
        this.eventVersion = eventVersion;
        this.studentId = studentId;
    }

    private static void validateConstructorArgs(Integer eventId, Integer eventVersion, Integer studentId) {
        checkNotNull(eventId, "Book lock event id cannot be null");
        checkNotNull(eventVersion, "Book lock event version cannot be null");
        checkNotNull(studentId, "Book lock student id cannot be null");
    }
}
