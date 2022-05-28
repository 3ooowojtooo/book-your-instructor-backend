package bookyourinstructor.usecase.event.store;

import com.quary.bookyourinstructor.model.event.EventLock;

public interface EventLockStore {

    EventLock saveEventLock(final EventLock eventLock);
}
