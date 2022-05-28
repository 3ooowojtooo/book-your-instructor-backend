package com.quary.bookyourinstructor.service.eventrealization;

import bookyourinstructor.usecase.event.store.EventRealizationStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.repository.EventRealizationRepository;
import com.quary.bookyourinstructor.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class EventRealizationStoreImpl implements EventRealizationStore {

    private final EventRealizationRepository eventRealizationRepository;
    private final EventRepository eventRepository;
    private final EventRealizationStoreMapper mapper;

    @Override
    public EventRealization saveEventRealization(EventRealization eventRealization) {
        final EventRealizationEntity entity = mapToEntity(eventRealization);
        final EventRealizationEntity savedEntity = eventRealizationRepository.save(entity);
        return mapper.mapToEventRealization(savedEntity);
    }

    @Override
    public List<EventRealization> saveEventRealizations(List<EventRealization> eventRealizations) {
        final List<EventRealizationEntity> entities = eventRealizations.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        final Iterable<EventRealizationEntity> savedEntities = eventRealizationRepository.saveAll(entities);
        return StreamSupport.stream(savedEntities.spliterator(), false)
                .map(mapper::mapToEventRealization)
                .collect(Collectors.toList());
    }

    @Override
    public void setStudentIdForEventRealizations(Integer studentId, Integer eventId) {
        eventRealizationRepository.setStudentIdForEventRealizations(studentId, eventId);
    }

    private EventRealizationEntity mapToEntity(EventRealization eventRealization) {
        final EventEntity event = eventRepository.findById(eventRealization.getEventId())
                .orElseThrow(() -> new IllegalStateException("Event instance with id " + eventRealization.getEventId() + " not found during single event realization creation"));
        return mapper.mapToNewEntity(eventRealization, event);
    }
}
