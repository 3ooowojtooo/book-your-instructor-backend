package bookyourinstructor.usecase.event;

import com.quary.bookyourinstructor.model.event.EventRealization;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);
}
