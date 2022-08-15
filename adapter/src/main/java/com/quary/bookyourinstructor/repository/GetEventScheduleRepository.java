package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleType;
import lombok.Getter;
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

    public List<GetEventScheduleResultItem> getSchedule(Integer userId, EventScheduleOwner owner, Instant now) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventScheduleEntity> cq = cb.createQuery(EventScheduleEntity.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventScheduleEntity> scheduleModel = metamodel.entity(EventScheduleEntity.class);

        Root<EventScheduleEntity> schedule = cq.from(EventScheduleEntity.class);
        Join<EventScheduleEntity, UserEntity> student = schedule.join(scheduleModel.getSingularAttribute("student", UserEntity.class));
        Join<EventScheduleEntity, UserEntity> instructor = schedule.join(scheduleModel.getSingularAttribute("instructor", UserEntity.class));

        Predicate userPredicate = buildUserPredicate(cb, schedule, student, instructor, userId, owner);
        Predicate notFinishedPredicate = buildNotFinishedPredicate(cb, schedule, now);
        Predicate[] mergedPredicates = new Predicate[]{userPredicate, notFinishedPredicate};

        cq.select(schedule);
        cq.where(cb.and(mergedPredicates));
        cq.orderBy(cb.asc(schedule.get("start")));

        TypedQuery<EventScheduleEntity> query = entityManager.createQuery(cq);
        List<EventScheduleEntity> result = query.getResultList();

        return mapToResultItems(result);
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

    private static Predicate buildNotFinishedPredicate(CriteriaBuilder cb, Root<EventScheduleEntity> schedule,
                                                       Instant now) {
        return cb.greaterThanOrEqualTo(schedule.get("end"), now);
    }

    private static List<GetEventScheduleResultItem> mapToResultItems(List<EventScheduleEntity> items) {
        return items.stream()
                .map(GetEventScheduleRepository::mapToResultItem)
                .collect(Collectors.toList());
    }

    private static GetEventScheduleResultItem mapToResultItem(EventScheduleEntity item) {
        String studentName = item.getStudent().getName() + " " + item.getStudent().getSurname();
        String instructorName = item.getInstructor().getName() + " " + item.getInstructor().getSurname();
        if (item.getType() == EventScheduleType.STATIC) {
            EventEntity event = item.getEvent();
            return new GetEventScheduleResultItem(event.getId(), event.getVersion(), item.getEventName(), item.getEventDescription(),
                    item.getEventLocation(), item.getEventPrice(), instructorName, studentName, item.getStatus(), item.getStart(), item.getEnd());
        } else {
            EventEntity event = item.getEvent();
            return new GetEventScheduleResultItem(event.getId(), event.getVersion(), event.getName(), event.getDescription(),
                    event.getLocation(), event.getPrice(), instructorName, studentName, item.getStatus(), item.getStart(), item.getEnd());
        }
    }
}
