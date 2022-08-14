package com.quary.bookyourinstructor.service.event;

import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.EventTypeFilter;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.repository.EventRepository;
import com.quary.bookyourinstructor.repository.EventSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventStoreImpl implements EventStore {

    private final EventRepository eventRepository;
    private final EventSearchRepository eventSearchRepository;
    private final EventStoreMapper mapper;

    @Override
    public SingleEvent saveSingleEvent(SingleEvent event) {
        EventEntity entity = mapToEntity(event);
        EventEntity savedEntity = eventRepository.save(entity);
        return mapper.mapToSingleEvent(savedEntity);
    }

    private EventEntity mapToEntity(SingleEvent event) {
        final EventEntity absenceEventParentEvent = getParentEventIfNecessary(event.getAbsenceEventParent());
        return mapper.mapToEntity(event, absenceEventParentEvent);
    }

    private EventEntity getParentEventIfNecessary(Integer absenceEventParent) {
        if (absenceEventParent != null) {
            return eventRepository.findById(absenceEventParent)
                    .filter(event -> event.getType() == EventType.CYCLIC)
                    .orElseThrow(() -> new IllegalStateException("Cyclic event with id " + absenceEventParent + " not found"));
        }
        return null;
    }

    @Override
    public CyclicEvent saveCyclicEvent(CyclicEvent event) {
        EventEntity eventEntity = mapper.mapToEntity(event);
        EventEntity savedEntity = eventRepository.save(eventEntity);
        return mapper.mapToCyclicEvent(savedEntity);
    }

    @Override
    public void updateCyclicEventBoundaries(Integer eventId, LocalDateTime startBoundary, LocalDateTime endBoundary) {
        eventRepository.updateCyclicEventBoundaries(eventId, startBoundary, endBoundary);
    }

    @Override
    public Optional<Event> findByIdWithLockForShare(Integer id) {
        return eventRepository.findByIdAndLockForShare(id)
                .map(mapper::mapToEvent);
    }

    @Override
    public Optional<Event> findByIdWithLockForUpdate(Integer id) {
        return eventRepository.findByIdAndLockForUpdate(id)
                .map(mapper::mapToEvent);
    }

    @Override
    public void setStatusByIdAndIncrementVersion(Integer id, EventStatus status) {
        eventRepository.setStatusByIdAndIncrementVersion(id, status);
    }

    @Override
    public Optional<Event> findById(Integer id) {
        return eventRepository.findById(id)
                .map(mapper::mapToEvent);
    }

    @Override
    public void deleteById(Integer id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void incrementVersion(Integer id) {
        eventRepository.incrementVersion(id);
    }

    @Override
    public List<SearchEventsResultItem> searchEvents(DateRangeFilter dateRange, TextSearchFilter text, EventTypeFilter eventType, Instant now) {
        return eventSearchRepository.searchEvents(dateRange, text, eventType, now);
    }
}
