package com.quary.bookyourinstructor.service.event;

import bookyourinstructor.usecase.event.common.store.EventStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.SingleEvent;
import com.quary.bookyourinstructor.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventStoreImpl implements EventStore {

    private final EventRepository eventRepository;
    private final EventStoreMapper mapper;

    @Override
    public SingleEvent saveSingleEvent(SingleEvent event) {
        EventEntity entity = mapper.mapToEntity(event);
        EventEntity savedEntity = eventRepository.save(entity);
        return mapper.mapToSingleEvent(savedEntity);
    }

    @Override
    public CyclicEvent saveCyclicEvent(CyclicEvent event) {
        EventEntity eventEntity = mapper.mapToEntity(event);
        EventEntity savedEntity = eventRepository.save(eventEntity);
        return mapper.mapToCyclicEvent(savedEntity);
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
}
