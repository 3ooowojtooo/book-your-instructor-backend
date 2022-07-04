package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.EventLock;

import java.time.Instant;
import java.util.Optional;

public interface EventLockStore {

    EventLock saveEventLock(final EventLock eventLock);

    Optional<EventLock> findById(final Integer id);

    void deleteById(final Integer id);

    void deleteByEventIdAndPastExpirationTime(Integer eventId, Instant now);

    boolean existsByEventIdAndFutureExpirationTime(Integer eventId, Instant now);
}
