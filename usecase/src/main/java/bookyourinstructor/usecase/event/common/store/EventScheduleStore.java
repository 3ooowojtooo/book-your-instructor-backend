package bookyourinstructor.usecase.event.common.store;

import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResultItem;
import com.quary.bookyourinstructor.model.event.EventSchedule;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import com.quary.bookyourinstructor.model.event.EventScheduleType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface EventScheduleStore {

    void saveSchedules(List<EventSchedule> eventSchedules);

    void update(Integer eventId, Integer eventRealization, Integer studentId, EventScheduleStatus status,
                EventScheduleOwner owner, EventScheduleStatus newStatus, EventScheduleType newType,
                String eventName, String eventDescription, String eventLocation, BigDecimal eventPrice);

    void delete(Integer eventId, Collection<Integer> realizationIds, Integer studentId, EventScheduleStatus status);

    List<GetEventScheduleResultItem> getSchedule(Integer userId, EventScheduleOwner owner, Instant now);
}
