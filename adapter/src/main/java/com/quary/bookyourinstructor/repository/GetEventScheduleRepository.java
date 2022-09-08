package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleTimeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.quary.bookyourinstructor.repository.util.RepositoryUtils.mergePredicates;

@Repository
@RequiredArgsConstructor
public class GetEventScheduleRepository {

    private final EntityManager entityManager;

    public List<GetEventScheduleResultItem> getSchedule(Integer userId, EventScheduleOwner owner, Instant now, boolean showPastEvents) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventScheduleEntity> cq = cb.createQuery(EventScheduleEntity.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventScheduleEntity> scheduleModel = metamodel.entity(EventScheduleEntity.class);

        Root<EventScheduleEntity> schedule = cq.from(EventScheduleEntity.class);
        Join<EventScheduleEntity, EventRealizationEntity> realization = schedule.join(scheduleModel.getSingularAttribute("eventRealization", EventRealizationEntity.class));
        Join<EventScheduleEntity, UserEntity> student = schedule.join(scheduleModel.getSingularAttribute("student", UserEntity.class));
        Join<EventScheduleEntity, UserEntity> instructor = schedule.join(scheduleModel.getSingularAttribute("instructor", UserEntity.class));

        Predicate userPredicate = buildUserPredicate(cb, schedule, student, instructor, userId, owner);
        Optional<Predicate> showPastPredicate = buildShowPastPredicate(cb, realization, now, showPastEvents);
        Predicate[] mergedPredicates = mergePredicates(Optional.of(userPredicate), showPastPredicate);

        cq.select(schedule);
        cq.where(cb.and(mergedPredicates));
        cq.orderBy(buildOrder(cb, schedule, realization));

        TypedQuery<EventScheduleEntity> query = entityManager.createQuery(cq);
        List<EventScheduleEntity> result = query.getResultList();

        return mapToResultItems(result, now);
    }

    private static Predicate buildUserPredicate(CriteriaBuilder cb, Root<EventScheduleEntity> schedule,
                                                Join<EventScheduleEntity, UserEntity> student,
                                                Join<EventScheduleEntity, UserEntity> instructor,
                                                Integer userId,
                                                EventScheduleOwner owner) {
        if (owner == EventScheduleOwner.STUDENT) {
            return cb.and(
                    cb.equal(student.get("id"), userId),
                    cb.equal(schedule.get("owner"), owner)
            );
        } else {
            return cb.and(
                    cb.equal(instructor.get("id"), userId),
                    cb.equal(schedule.get("owner"), owner)
            );
        }
    }

    private static Optional<Predicate> buildShowPastPredicate(CriteriaBuilder cb,
                                                              Join<EventScheduleEntity, EventRealizationEntity> realization,
                                                              Instant now,
                                                              boolean showPastEvents) {
        if (showPastEvents) {
            return Optional.empty();
        }
        return Optional.of(
                cb.greaterThanOrEqualTo(realization.get("end"), now)
        );
    }

    private static List<Order> buildOrder(CriteriaBuilder cb, Root<EventScheduleEntity> schedule, Join<EventScheduleEntity,
            EventRealizationEntity> realization) {
        return List.of(
                cb.asc(realization.get("start")),
                cb.asc(schedule.get("id"))
        );
    }

    private static List<GetEventScheduleResultItem> mapToResultItems(List<EventScheduleEntity> items, Instant now) {
        return items.stream()
                .map(item -> mapToResultItem(item, now))
                .collect(Collectors.toList());
    }

    private static GetEventScheduleResultItem mapToResultItem(EventScheduleEntity item, Instant now) {
        String studentName = item.getStudent().getName() + " " + item.getStudent().getSurname();
        String instructorName = item.getInstructor().getName() + " " + item.getInstructor().getSurname();
        EventEntity event = item.getEvent();
        EventRealizationEntity realization = item.getEventRealization();
        EventScheduleTimeStatus timeStatus = buildTimeStatus(realization, now);
        return new GetEventScheduleResultItem(event.getId(), event.getVersion(), realization.getId(), event.getName(), event.getDescription(),
                event.getLocation(), event.getPrice(), instructorName, studentName, item.getStatus(), timeStatus, realization.getStart(), realization.getEnd());
    }

    private static EventScheduleTimeStatus buildTimeStatus(EventRealizationEntity eventRealization, Instant now) {
        if (now.isBefore(eventRealization.getStart())) {
            return EventScheduleTimeStatus.FUTURE;
        } else if (now.isAfter(eventRealization.getEnd())) {
            return EventScheduleTimeStatus.PAST;
        }
        return EventScheduleTimeStatus.IN_PROGRESS;
    }
}
