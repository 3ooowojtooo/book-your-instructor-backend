package com.quary.bookyourinstructor.service.event;

import bookyourinstructor.usecase.event.store.EventStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.CyclicEvent;
import com.quary.bookyourinstructor.model.event.Event;
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
    public Optional<Event> getByIdWithLockForShare(Integer id) {
        return eventRepository.findByIdAndLockForShare(id)
                .map(mapper::mapToEvent);
    }
}
