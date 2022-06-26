package bookyourinstructor.usecase.event.cyclic;

import bookyourinstructor.usecase.event.cyclic.data.NewCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.exception.NoRealizationsOfCyclicEventFoundRuntimeException;
import bookyourinstructor.usecase.event.cyclic.helper.CyclicEventRealizationsFinder;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.exception.InvalidCyclicEventBoundariesException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DeclareCyclicEventUseCase {

    private final EventStore eventStore;
    private final EventRealizationStore eventRealizationStore;
    private final TransactionFacade transactionFacade;
    private final CyclicEventRealizationsFinder realizationsFinder;

    public DeclareCyclicEventResult declareNewCyclicEvent(final NewCyclicEventData eventData) throws InvalidCyclicEventBoundariesException {
        try {
            return transactionFacade.executeInTransaction(() -> {
                CyclicEvent event = buildCyclicEvent(eventData);
                CyclicEvent savedEvent = eventStore.saveCyclicEvent(event);
                List<EventRealization> eventRealizations = realizationsFinder.findCyclicEventRealizations(savedEvent);
                List<EventRealization> savedEventRealizations = eventRealizationStore.saveEventRealizations(eventRealizations);
                return buildResult(savedEvent, savedEventRealizations);
            });
        } catch (NoRealizationsOfCyclicEventFoundRuntimeException ex) {
            throw new InvalidCyclicEventBoundariesException();
        }
    }

    private static CyclicEvent buildCyclicEvent(final NewCyclicEventData eventData) {
        return CyclicEvent.newCyclicEvent(eventData.getInstructorId(), eventData.getName(), eventData.getDescription(),
                eventData.getLocation(), eventData.getPrice(), eventData.getStartTime(), eventData.getDuration(), eventData.getDayOfWeek(),
                eventData.getStartBoundary(), eventData.getEndBoundary());
    }

    private static DeclareCyclicEventResult buildResult(CyclicEvent event, List<EventRealization> eventRealizations) {
        return new DeclareCyclicEventResult(event.getId(), eventRealizations);
    }
}
