package bookyourinstructor.usecase.event.common.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class GetEventDetailsAsStudentData {

    private final Integer eventId;
    private final Integer studentId;

    public GetEventDetailsAsStudentData(Integer eventId, Integer studentId) {
        validateConstructorArgs(eventId, studentId);
        this.eventId = eventId;
        this.studentId = studentId;
    }

    private static void validateConstructorArgs(Integer eventId, Integer studentId) {
        checkNotNull(eventId, "Event id cannot be null");
        checkNotNull(studentId, "Student id cannot be null");
    }
}
