package com.quary.bookyourinstructor.service.eventrealization;

import bookyourinstructor.usecase.event.EventRealizationStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.repository.EventRealizationRepository;
import com.quary.bookyourinstructor.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRealizationStoreImpl implements EventRealizationStore {

    private final EventRealizationRepository eventRealizationRepository;
    private final EventRepository eventRepository;
    private final EventRealizationStoreMapper mapper;

    @Override
    public EventRealization saveEventRealization(EventRealization eventRealization) {
        final EventEntity event = eventRepository.findById(eventRealization.getEventId())
                .orElseThrow(() -> new IllegalStateException("Event instance with id " + eventRealization.getEventId() + " not found during single event realization creation"));
        final EventRealizationEntity entity = mapper.mapToNewEntity(eventRealization, event);
        final EventRealizationEntity savedEntity = eventRealizationRepository.save(entity);
        return mapper.mapToEventRealization(savedEntity);
    }
}
