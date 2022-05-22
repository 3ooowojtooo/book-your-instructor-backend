package bookyourinstructor.usecase.event;

import com.quary.bookyourinstructor.model.event.SingleEvent;

public interface EventStore {

    SingleEvent saveSingleEvent(SingleEvent event);
}
