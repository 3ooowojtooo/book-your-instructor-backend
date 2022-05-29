package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;

import java.util.List;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);

    List<EventRealization> saveEventRealizations(final List<EventRealization> eventRealizations);

    void setStudentIdForEventRealizations(final Integer studentId, final Integer eventId);

    void setStatusForEventRealizations(final EventRealizationStatus status, final Integer eventId);
}
