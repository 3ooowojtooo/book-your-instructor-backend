package bookyourinstructor.usecase.event.store;

import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.SingleEvent;

import java.util.Optional;

public interface EventStore {

    SingleEvent saveSingleEvent(SingleEvent event);

    CyclicEvent saveCyclicEvent(CyclicEvent event);

    Optional<Event> getById(Integer id);
}
