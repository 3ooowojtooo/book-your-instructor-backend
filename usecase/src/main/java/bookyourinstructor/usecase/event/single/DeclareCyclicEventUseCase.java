package bookyourinstructor.usecase.event.single;

import bookyourinstructor.usecase.event.EventRealizationStore;
import bookyourinstructor.usecase.event.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewCyclicEventData;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class DeclareCyclicEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TimeUtils timeUtils;
    private final TransactionFacade transactionFacade;

    public List<EventRealization> declareNewCyclicEvent(final NewCyclicEventData eventData) {
        return transactionFacade.executeInTransaction(() -> {
            CyclicEvent event = buildCyclicEvent(eventData);
            CyclicEvent savedEvent = eventStore.saveCyclicEvent(event);
            return buildEventRealizations(savedEvent);
        });
    }

    private static CyclicEvent buildCyclicEvent(final NewCyclicEventData eventData) {
        return CyclicEvent.newCyclicEvent(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getStartTime(), eventData.getEndTime(), eventData.getDayOfWeek(),
                eventData.getStartBoundary(), eventData.getEndBoundary());
    }

    private List<EventRealization> buildEventRealizations(final CyclicEvent cyclicEvent) {
        return List.of();
    }
}
