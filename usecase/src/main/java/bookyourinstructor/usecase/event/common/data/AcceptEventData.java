package bookyourinstructor.usecase.event.common.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class AcceptEventData {

    private final Integer eventId;
    private final Integer userId;

    public AcceptEventData(Integer eventId, Integer userId) {
        validateConstructorArgs(eventId, userId);
        this.eventId = eventId;
        this.userId = userId;
    }

    private static void validateConstructorArgs(Integer eventId, Integer userId) {
        checkNotNull(eventId, "Id of event to accept cannot be null");
        checkNotNull(userId, "Id of user accepting event cannot be null");
    }
}
