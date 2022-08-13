package com.quary.bookyourinstructor.service.eventschedule;

import bookyourinstructor.usecase.event.common.store.EventScheduleStore;
import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventSchedule;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import com.quary.bookyourinstructor.model.event.EventScheduleType;
import com.quary.bookyourinstructor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventScheduleStoreImpl implements EventScheduleStore {

    private final EventScheduleRepository eventScheduleRepository;
    private final GetEventScheduleRepository getEventScheduleRepository;
    private final EventRepository eventRepository;
    private final EventRealizationRepository eventRealizationRepository;
    private final UserRepository userRepository;
    private final EventScheduleStoreMapper mapper;

    @Override
    public void saveSchedules(List<EventSchedule> eventSchedules) {
        List<EventScheduleEntity> entities = eventSchedules.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        Iterable<EventScheduleEntity> savedEntities = eventScheduleRepository.saveAll(entities);
    }

    @Override
    public void update(Integer eventId, Integer eventRealization, Integer studentId, EventScheduleStatus status,
                       EventScheduleOwner owner, EventScheduleStatus newStatus, EventScheduleType newType, String eventName, String eventDescription,
                       String eventLocation, BigDecimal eventPrice) {
        eventScheduleRepository.update(eventId, eventRealization, studentId, status, owner, newStatus, newType, eventName,
                eventDescription, eventLocation, eventPrice);
    }

    @Override
    public void delete(Integer eventId, Collection<Integer> realizationIds, Integer studentId, EventScheduleStatus status) {
        eventScheduleRepository.delete(eventId, realizationIds, studentId, status);
    }

    @Override
    public List<GetEventScheduleResultItem> getSchedule(Integer userId, EventScheduleOwner owner, Instant now) {
        return getEventScheduleRepository.getSchedule(userId, owner, now);
    }

    private EventScheduleEntity mapToEntity(EventSchedule eventSchedule) {
        EventEntity event = eventRepository.findById(eventSchedule.getEventId())
                .orElseThrow(() -> new IllegalStateException("Event with id " + eventSchedule.getEventId() + " not found"));
        EventRealizationEntity eventRealization = eventRealizationRepository.findById(eventSchedule.getEventRealizationId())
                .orElseThrow(() -> new IllegalStateException("Event realization with id " + eventSchedule.getEventRealizationId() + " not found"));
        UserEntity student = userRepository.findById(eventSchedule.getStudentId())
                .orElseThrow(() -> new IllegalStateException("User with id " + eventSchedule.getStudentId() + " not found"));
        UserEntity instructor = userRepository.findById(eventSchedule.getInstructorId())
                .orElseThrow(() -> new IllegalStateException("User with id " + eventSchedule.getInstructorId() + " not found"));
        return mapper.mapToEntity(eventSchedule, event, eventRealization, student, instructor);
    }
}
