package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.common.result.EventRealizationWithTimeStatus;
import bookyourinstructor.usecase.event.common.result.GetEventListResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.EventTimeStatus;
import com.quary.bookyourinstructor.model.event.EventType;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.quary.bookyourinstructor.repository.util.RepositoryUtils.mergePredicates;

@Repository
@RequiredArgsConstructor
public class GetEventListRepository {

    private final EntityManager entityManager;

    public List<GetEventListResultItem> getEventList(Integer userId, UserType userType, Instant now, boolean showPastEvents) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<QueryResult> cq = cb.createQuery(QueryResult.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventEntity> eventModel = metamodel.entity(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        ListJoin<EventEntity, EventRealizationEntity> realization = event.join(eventModel.getList("realizations", EventRealizationEntity.class));
        Join<EventEntity, UserEntity> user = buildUserJoin(event, eventModel, userType);

        Predicate userPredicate = buildUserPredicate(cb, user, userId);
        Predicate notDraftPredicate = buildNotDraftPredicate(cb, event);
        Optional<Predicate> showPastPredicate = buildShowPastPredicate(cb, realization, now, showPastEvents);
        Predicate[] mergedPredicates = mergePredicates(Optional.of(userPredicate), Optional.of(notDraftPredicate), showPastPredicate);

        cq.multiselect(event, cb.min(realization.get("start")));
        cq.where(mergedPredicates);
        cq.groupBy(event.get("id"));
        cq.orderBy(buildOrder(cb, event, realization));

        TypedQuery<QueryResult> query = entityManager.createQuery(cq);
        List<QueryResult> results = query.getResultList();

        return mapResults(results, now);
    }

    private static Join<EventEntity, UserEntity> buildUserJoin(Root<EventEntity> event, EntityType<EventEntity> eventModel,
                                                               UserType userType) {
        if (userType == UserType.STUDENT) {
            return event.join(eventModel.getSingularAttribute("student", UserEntity.class));
        }
        return event.join(eventModel.getSingularAttribute("instructor", UserEntity.class));
    }

    private static Predicate buildUserPredicate(CriteriaBuilder cb, Join<EventEntity, UserEntity> user,
                                                Integer userId) {
        return cb.equal(user.get("id"), userId);
    }

    private static Predicate buildNotDraftPredicate(CriteriaBuilder cb, Root<EventEntity> event) {
        return cb.notEqual(event.get("status"), EventStatus.DRAFT);
    }

    private static Optional<Predicate> buildShowPastPredicate(CriteriaBuilder cb, Join<EventEntity, EventRealizationEntity> realization,
                                                              Instant now, boolean showPastEvents) {
        if (showPastEvents) {
            return Optional.empty();
        }
        return Optional.of(
                cb.greaterThanOrEqualTo(realization.get("end"), now)
        );
    }

    private List<Order> buildOrder(CriteriaBuilder cb, Root<EventEntity> event, ListJoin<EventEntity, EventRealizationEntity> realization) {
        return List.of(
                cb.asc(cb.min(realization.get("start"))),
                cb.asc(event.get("id"))
        );
    }

    private List<GetEventListResultItem> mapResults(List<QueryResult> queryResults, Instant now) {
        return queryResults.stream()
                .map(result -> mapResult(result, now))
                .collect(Collectors.toList());
    }

    private GetEventListResultItem mapResult(QueryResult queryResult, Instant now) {
        EventEntity event = queryResult.getEvent();
        UserEntity instructor = event.getInstructor();
        UserEntity student = event.getStudent();

        List<EventRealizationWithTimeStatus> realizations = mapToRealizationsWithTimeStatus(event.getRealizations(), now);
        boolean hasAnyFutureRealizations = buildHasAnyFutureRealizations(event, realizations);
        boolean isFinished = buildIsFinished(realizations);
                String instructorName = buildUserFullName(instructor);
        String studentName = buildUserFullName(student);

        return new GetEventListResultItem(event.getId(), event.getVersion(), event.getName(), event.getDescription(),
                event.getLocation(), instructorName, studentName, event.getType(), event.getStatus(), event.getPrice(), event.getCreatedAt(), isFinished, hasAnyFutureRealizations,
                event.getSingleEventStart(), event.getSingleEventEnd(), event.getCyclicDayOfWeek(), event.getCyclicEventStart(),
                event.getCyclicEventDuration(), event.getCyclicStartBoundary(), event.getCyclicEndBoundary(), event.getCyclicAbsenceEvent(),
                event.getCyclicAbsenceEventName(), event.getCyclicAbsenceEventDescription(), realizations);
    }

    private static List<EventRealizationWithTimeStatus> mapToRealizationsWithTimeStatus(List<EventRealizationEntity> realizations, Instant now) {
        return realizations.stream()
                .map(realization -> mapToRealizationWithTimeStatus(realization, now))
                .collect(Collectors.toList());
    }

    private static EventRealizationWithTimeStatus mapToRealizationWithTimeStatus(EventRealizationEntity realization, Instant now) {
        EventTimeStatus timeStatus = buildTimeStatus(realization, now);
        return new EventRealizationWithTimeStatus(realization.getId(), realization.getStart(), realization.getEnd(),
                realization.getStatus(), timeStatus);
    }

    private static EventTimeStatus buildTimeStatus(EventRealizationEntity eventRealization, Instant now) {
        if (now.isBefore(eventRealization.getStart())) {
            return EventTimeStatus.FUTURE;
        } else if (now.isAfter(eventRealization.getEnd())) {
            return EventTimeStatus.PAST;
        }
        return EventTimeStatus.IN_PROGRESS;
    }

    private static boolean buildHasAnyFutureRealizations(EventEntity event, List<EventRealizationWithTimeStatus> realizations) {
        return realizations.stream()
                .anyMatch(realization -> realization.getTimeStatus() == EventTimeStatus.FUTURE && realization.getStatus() == EventRealizationStatus.BOOKED);
    }

    private static boolean buildIsFinished(List<EventRealizationWithTimeStatus> realizations) {
        return realizations.stream()
                .map(EventRealizationWithTimeStatus::getTimeStatus)
                .allMatch(timeStatus -> timeStatus == EventTimeStatus.PAST);
    }

    private static String buildUserFullName(UserEntity user) {
        if (user == null) {
            return null;
        }
        return user.getName() + " " + user.getSurname();
    }

    @RequiredArgsConstructor
    @Getter
    private static class QueryResult {
        private final EventEntity event;
        private final Instant minRealizationStart;
    }
}
