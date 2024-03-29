package com.quary.bookyourinstructor.service.eventlock;

import bookyourinstructor.usecase.event.common.store.EventLockStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventLockEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.repository.EventLockRepository;
import com.quary.bookyourinstructor.repository.EventRepository;
import com.quary.bookyourinstructor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventLockStoreImpl implements EventLockStore {

    private final EventLockRepository eventLockRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventLockStoreMapper mapper;

    @Override
    public EventLock saveEventLock(EventLock eventLock) {
        final UserEntity user = userRepository.findById(eventLock.getUserId())
                .orElseThrow(() -> new IllegalStateException("User with id " + eventLock.getUserId() + " not found"));
        final EventEntity event = eventRepository.findByIdAndVersion(eventLock.getEventId(), eventLock.getEventVersion())
                .orElseThrow(() -> new IllegalStateException("Event with id " + eventLock.getEventId() + " and version " + eventLock.getEventVersion() + " not found"));
        final EventLockEntity entity = mapper.mapToEntity(eventLock, user, event);
        final EventLockEntity savedEntity = eventLockRepository.save(entity);
        return mapper.mapToEventLock(savedEntity);
    }

    @Override
    public Optional<EventLock> findById(Integer id) {
        return eventLockRepository.findById(id)
                .map(mapper::mapToEventLock);
    }

    @Override
    public void deleteById(Integer id) {
        eventLockRepository.deleteById(id);
    }

    @Override
    public void deleteByEventIdAndPastExpirationTime(Integer eventId, Instant now) {
        eventLockRepository.deleteByEventIdAndExpirationTimeLessThanEqual(eventId, now);
    }

    @Override
    public boolean existsByEventIdAndFutureExpirationTime(Integer eventId, Instant now) {
        return eventLockRepository.existsByEventIdAndExpirationTimeGreaterThan(eventId, now);
    }
}