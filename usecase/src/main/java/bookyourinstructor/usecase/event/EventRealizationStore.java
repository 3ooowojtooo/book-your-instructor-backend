package bookyourinstructor.usecase.event;

import com.quary.bookyourinstructor.model.event.EventRealization;

import java.util.List;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);

    List<EventRealization> saveEventRealizations(final List<EventRealization> eventRealizations);
}
