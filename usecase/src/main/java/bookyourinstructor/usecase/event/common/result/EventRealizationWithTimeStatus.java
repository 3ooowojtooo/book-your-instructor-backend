package bookyourinstructor.usecase.event.common.result;

import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventTimeStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
public class EventRealizationWithTimeStatus {

    private final Integer eventRealizationId;
    private final Instant eventStart;
    private final Instant eventEnd;
    private final EventRealizationStatus status;
    private final EventTimeStatus timeStatus;
}
