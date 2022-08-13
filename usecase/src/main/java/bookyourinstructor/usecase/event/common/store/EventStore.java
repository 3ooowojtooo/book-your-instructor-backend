package bookyourinstructor.usecase.event.common.store;

import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.EventTypeFilter;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.SingleEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventStore {

    SingleEvent saveSingleEvent(SingleEvent event);

    CyclicEvent saveCyclicEvent(CyclicEvent event);

    void updateCyclicEventBoundaries(Integer eventId, LocalDateTime startBoundary, LocalDateTime endBoundary);

    Optional<Event> findByIdWithLockForShare(Integer id);

    Optional<Event> findByIdWithLockForUpdate(Integer id);

    void setStatusByIdAndIncrementVersion(Integer id, EventStatus status);

    void incrementVersion(Integer id);

    Optional<Event> findById(Integer id);

    void deleteById(Integer id);

    List<SearchEventsResultItem> searchEvents(DateRangeFilter dateRange, TextSearchFilter text, EventTypeFilter eventType, Instant now);
}
