package bookyourinstructor.usecase.event.single.result;

import com.quary.bookyourinstructor.model.event.EventRealization;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeclareSingleEventResult {

    private final Integer eventId;
    private final EventRealization eventRealization;
}
