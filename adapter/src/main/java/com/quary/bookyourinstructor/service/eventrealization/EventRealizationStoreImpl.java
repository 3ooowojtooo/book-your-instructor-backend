package com.quary.bookyourinstructor.service.eventrealization;

import bookyourinstructor.usecase.event.common.store.EventRealizationStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.repository.EventRealizationRepository;
import com.quary.bookyourinstructor.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
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

    @Override
    public void setStatusForEventRealizations(EventRealizationStatus status, Integer eventId) {
        eventRealizationRepository.setStatusForEventRealizations(status, eventId);
    }

    @Override
    public Optional<EventRealization> findById(Integer eventRealizationId) {
        return eventRealizationRepository.findById(eventRealizationId)
                .map(mapper::mapToEventRealization);
    }

    @Override
    public List<EventRealization> findAllRealizations(Integer eventId) {
        return eventRealizationRepository.findAllByEventId(eventId)
                .stream()
                .map(mapper::mapToEventRealization)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventRealization> findAllByEventIdStartingAfterSortedAscWithLockForUpdate(Integer eventId, Instant now) {
        return eventRealizationRepository.findAllByEventIdStartingAfterOrderByStartAscWithLockForUpdate(eventId, now)
                .stream()
                .map(mapper::mapToEventRealization)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRealizationsByEventId(Integer eventId) {
        eventRealizationRepository.deleteAllByEventId(eventId);
    }

    @Override
    public Optional<EventRealization> findByIdWithLockForUpdate(Integer eventRealizationId) {
        return eventRealizationRepository.findByIdWithLockForUpdate(eventRealizationId)
                .map(mapper::mapToEventRealization);
    }

    @Override
    public void setStudentIdForEventRealization(Integer studentId, Integer id) {
        eventRealizationRepository.setStudentIdForEventRealization(studentId, id);
    }

    @Override
    public void setStatusForEventRealization(EventRealizationStatus status, Integer id) {
        eventRealizationRepository.setStatusForEventRealization(status, id);
    }

    private EventRealizationEntity mapToEntity(EventRealization eventRealization) {
        final EventEntity event = eventRepository.findById(eventRealization.getEventId())
                .orElseThrow(() -> new IllegalStateException("Event instance with id " + eventRealization.getEventId() + " not found during single event realization creation"));
        return mapper.mapToNewEntity(eventRealization, event);
    }
}
