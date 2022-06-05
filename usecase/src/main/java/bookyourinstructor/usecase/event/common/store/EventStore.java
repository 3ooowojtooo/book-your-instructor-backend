package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.SingleEvent;

import java.util.Optional;

public interface EventStore {

    SingleEvent saveSingleEvent(SingleEvent event);

    CyclicEvent saveCyclicEvent(CyclicEvent event);

    Optional<Event> findByIdWithLockForShare(Integer id);

    Optional<Event> findByIdWithLockForUpdate(Integer id);

    void setStatusByIdAndIncrementVersion(Integer id, EventStatus status);

    Optional<Event> findById(Integer id);

    void deleteById(Integer id);
}
