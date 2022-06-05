package bookyourinstructor.usecase.event.common.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class DeleteDraftEventData {

    private final Integer eventId;
    private final Integer instructorId;

    public DeleteDraftEventData(Integer eventId, Integer instructorId) {
        validateConstructorArgs(eventId, instructorId);
        this.eventId = eventId;
        this.instructorId = instructorId;
    }

    private static void validateConstructorArgs(Integer eventId, Integer instructorId) {
        checkNotNull(eventId, "Id of an draft event to delete cannot be null");
        checkNotNull(instructorId, "Student id cannot be null");
    }
}
