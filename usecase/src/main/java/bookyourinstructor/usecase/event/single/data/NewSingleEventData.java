package bookyourinstructor.usecase.event.single.data;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class NewSingleEventData {

    private final String name;
    private final String description;
    private final String location;
    private final BigDecimal price;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final Integer instructorId;

    public NewSingleEventData(String name, String description, String location, BigDecimal price,
                              LocalDateTime startDateTime, LocalDateTime endDateTime,
                              Integer instructorId) {
        validateConstructorArgs(name, location, price, startDateTime, endDateTime, instructorId);
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.instructorId = instructorId;
    }

    private static void validateConstructorArgs(String name, String location, BigDecimal price, LocalDateTime startDateTime,
                                                LocalDateTime endDateTime, Integer instructorId) {
        checkArgument(isNotBlank(name), "Single event name cannot be blank");
        checkArgument(isNotBlank(location), "Single event location cannot be blank");
        checkNotNull(price, "Single event event price cannot be null");
        checkArgument(price.compareTo(BigDecimal.ZERO) > 0, "Single event price must be greater than 0");
        checkNotNull(startDateTime, "Single event start date time cannot be null");
        checkNotNull(endDateTime, "Single event end date time cannot be null");
        checkArgument(endDateTime.isAfter(startDateTime), "Single event end date time must be after start date time");
        checkNotNull(instructorId, "Single event instructor id cannot be null");
    }
}
