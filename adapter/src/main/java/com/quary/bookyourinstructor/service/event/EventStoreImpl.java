package com.quary.bookyourinstructor.service.event;

import bookyourinstructor.usecase.event.common.result.GetEventListResultItem;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.EventTypeFilter;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.*;
import com.quary.bookyourinstructor.model.user.UserType;
import com.quary.bookyourinstructor.repository.EventRepository;
import com.quary.bookyourinstructor.repository.EventSearchRepository;
import com.quary.bookyourinstructor.repository.GetEventListRepository;
import com.quary.bookyourinstructor.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final EventSearchRepository eventSearchRepository;
    private final GetEventListRepository eventListRepository;
    private final EventStoreMapper mapper;

    @Override
    public SingleEvent saveSingleEvent(SingleEvent event) {
        EventEntity entity = mapToEntity(event);
        EventEntity savedEntity = eventRepository.save(entity);
        return mapper.mapToSingleEvent(savedEntity);
    }

    private EventEntity mapToEntity(SingleEvent event) {
        final UserEntity student = getUser(event.getStudentId());
        final UserEntity instructor = getUser(event.getInstructorId());
        return mapper.mapToEntity(event, student, instructor);
    }

    private UserEntity getUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " not found"));
    }

    @Override
    public CyclicEvent saveCyclicEvent(CyclicEvent event) {
        final UserEntity student = getUser(event.getStudentId());
        final UserEntity instructor = getUser(event.getInstructorId());
        EventEntity eventEntity = mapper.mapToEntity(event, student, instructor);
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
    public void setStatusAndStudentByIdAndIncrementVersion(Integer id, Integer studentId, EventStatus status) {
        eventRepository.setStatusAndStudentByIdAndIncrementVersion(id, studentId, status);
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

    @Override
    public List<GetEventListResultItem> getEventList(Integer userId, UserType userType, Instant now) {
        return eventListRepository.getEventList(userId, userType, now);
    }
}
