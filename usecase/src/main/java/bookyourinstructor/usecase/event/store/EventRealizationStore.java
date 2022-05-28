package bookyourinstructor.usecase.event.store;

import com.quary.bookyourinstructor.model.event.EventRealization;

import java.util.List;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);

    List<EventRealization> saveEventRealizations(final List<EventRealization> eventRealizations);

    void setStudentIdForEventRealizations(final Integer studentId, final Integer eventId);
}
