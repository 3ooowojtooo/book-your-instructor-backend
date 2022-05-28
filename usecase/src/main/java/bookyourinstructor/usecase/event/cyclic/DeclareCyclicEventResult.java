package bookyourinstructor.usecase.event.cyclic;

import com.quary.bookyourinstructor.model.event.EventRealization;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class DeclareCyclicEventResult {

    private final Integer eventId;
    private final List<EventRealization> eventRealizations;
}
