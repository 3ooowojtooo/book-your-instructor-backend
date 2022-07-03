package bookyourinstructor.usecase.event.search.data;

import com.quary.bookyourinstructor.model.event.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EventTypeFilter {

    private final EventType eventType;

    public boolean allEventTypesSelected() {
        return eventType == null;
    }

}
