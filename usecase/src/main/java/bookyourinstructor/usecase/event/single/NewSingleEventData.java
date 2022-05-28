package bookyourinstructor.usecase.event.single;

import lombok.Getter;

import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class NewSingleEventData {

    private final String name;
    private final String description;
    private final String location;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Integer instructorId;

    public NewSingleEventData(String name, String description, String location, LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Integer instructorId) {
        validateConstructorArgs(name, location, startDateTime, endDateTime, instructorId);
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.instructorId = instructorId;
    }

    private static void validateConstructorArgs(String name, String location, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                Integer instructorId) {
        checkArgument(isNotBlank(name), "Single event name cannot be blank");
        checkArgument(isNotBlank(location), "Single event location cannot be blank");
        checkNotNull(startDateTime, "Single event start date time cannot be null");
        checkNotNull(endDateTime, "Single event end date time cannot be null");
        checkArgument(endDateTime.isAfter(startDateTime), "Single event end date time must be after start date time");
        checkNotNull(instructorId, "Single event instructor id cannot be null");
    }
}
