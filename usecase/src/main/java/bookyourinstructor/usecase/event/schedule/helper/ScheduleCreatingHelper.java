package bookyourinstructor.usecase.event.schedule.helper;

import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventRealization;

import java.util.List;

public interface ScheduleCreatingHelper {

    void handleEventBooked(Event event, Integer studentId);

    void handleStudentAbsence(Event event, EventRealization eventRealization);

    void handleInstructorAbsence(Event event, EventRealization eventRealization);

    void handleCyclicEventResigned(CyclicEvent event, List<Integer> resignedRealizationsIds, Integer studentId);
}
