package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRealizationStore {

    EventRealization saveEventRealization(final EventRealization eventRealization);

    List<EventRealization> saveEventRealizations(final List<EventRealization> eventRealizations);

    Optional<EventRealization> findById(Integer eventRealizationId);

    Optional<EventRealization> findByIdWithLockForUpdate(Integer eventRealizationId);

    void setStudentIdForEventRealizations(final Integer studentId, final Integer eventId);

    void setStudentIdForEventRealization(Integer studentId, Integer id);

    void setStatusForEventRealization(EventRealizationStatus status, Integer id);

    void setStatusForEventRealizations(final EventRealizationStatus status, final Integer eventId);

    List<EventRealization> findAllRealizations(final Integer eventId);

    List<EventRealization> findAllFutureRealizationsWithStatus(final Integer eventId, final Instant now, final EventRealizationStatus status);

    List<EventRealization> findAllByEventIdStartingAfterSortedAscWithLockForUpdate(final Integer eventId, final Instant now);

    void deleteRealizationsByEventId(final Integer eventId);
}
