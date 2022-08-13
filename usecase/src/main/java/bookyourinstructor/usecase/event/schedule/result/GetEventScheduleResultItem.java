package bookyourinstructor.usecase.event.schedule.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@RequiredArgsConstructor
@Getter
public class GetEventScheduleResultItem {

    private final Integer eventId;
    private final Integer eventVersion;
    private final String eventName;
    private final String eventDescription;
    private final String eventLocation;
    private final BigDecimal eventPrice;
    private final String instructorName;
    private final String studentName;
    private final EventScheduleStatus status;
    private final Instant eventStart;
    private final Instant eventEnd;
}
