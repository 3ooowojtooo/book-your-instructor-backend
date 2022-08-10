package bookyourinstructor.usecase.event.cyclic.result;

import com.quary.bookyourinstructor.model.event.EventRealization;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class DeclareCyclicEventResult {

    private final Integer eventId;
    private final Instant createdAt;
    private final List<EventRealization> eventRealizations;
}
