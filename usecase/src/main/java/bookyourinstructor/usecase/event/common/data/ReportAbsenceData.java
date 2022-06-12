package bookyourinstructor.usecase.event.common.data;

import bookyourinstructor.usecase.event.common.port.UserData;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class ReportAbsenceData {

    private final Integer eventRealizationId;
    private final Integer eventVersion;
    private final UserData user;

    public ReportAbsenceData(Integer eventRealizationId, Integer eventVersion, UserData user) {
        validateConstructorArgs(eventRealizationId, eventVersion, user);
        this.eventRealizationId = eventRealizationId;
        this.eventVersion = eventVersion;
        this.user = user;
    }

    private static void validateConstructorArgs(Integer eventRealizationId, Integer eventVersion, UserData user) {
        checkNotNull(eventRealizationId, "Event realization id cannot be null");
        checkNotNull(eventVersion, "Event version cannot be null");
        checkNotNull(user, "User cannot be null");
    }
}
