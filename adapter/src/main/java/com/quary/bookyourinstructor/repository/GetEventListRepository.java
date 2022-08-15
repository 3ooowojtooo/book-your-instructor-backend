package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.common.result.GetEventListResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.user.UserType;
import com.quary.bookyourinstructor.service.eventrealization.EventRealizationStoreMapper;
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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GetEventListRepository {

    private final EntityManager entityManager;
    private final EventRealizationStoreMapper mapper;

    public List<GetEventListResultItem> getEventList(Integer userId, UserType userType, Instant now) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<QueryResult> cq = cb.createQuery(QueryResult.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventEntity> eventModel = metamodel.entity(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        ListJoin<EventEntity, EventRealizationEntity> realization = event.join(eventModel.getList("realizations", EventRealizationEntity.class));
        Join<EventEntity, UserEntity> user = buildUserJoin(event, eventModel, userType);

        Predicate userPredicate = buildUserPredicate(cb, user, userId, userType);

        cq.multiselect(event, cb.min(realization.get("start")));
        cq.where(new Predicate[]{userPredicate});
        cq.groupBy(event.get("id"));
        cq.orderBy(buildOrder(cb, event, realization));

        TypedQuery<QueryResult> query = entityManager.createQuery(cq);
        List<QueryResult> results = query.getResultList();

        return mapResults(results);
    }

    private static Join<EventEntity, UserEntity> buildUserJoin(Root<EventEntity> event, EntityType<EventEntity> eventModel,
                                                               UserType userType) {
        if (userType == UserType.STUDENT) {
            return event.join(eventModel.getSingularAttribute("student", UserEntity.class));
        }
        return event.join(eventModel.getSingularAttribute("instructor", UserEntity.class));
    }

    private static Predicate buildUserPredicate(CriteriaBuilder cb, Join<EventEntity, UserEntity> user,
                                                Integer userId, UserType userType) {
        return cb.equal(user.get("id"), userId);
    }

    private List<Order> buildOrder(CriteriaBuilder cb, Root<EventEntity> event, ListJoin<EventEntity, EventRealizationEntity> realization) {
        return List.of(
                cb.asc(cb.min(realization.get("start"))),
                cb.asc(event.get("id"))
        );
    }

    private List<GetEventListResultItem> mapResults(List<QueryResult> queryResults) {
        return queryResults.stream()
                .map(this::mapResult)
                .collect(Collectors.toList());
    }

    private GetEventListResultItem mapResult(QueryResult queryResult) {
        EventEntity event = queryResult.getEvent();
        List<EventRealization> realizations = mapper.mapToEventRealizations(event.getRealizations());
        UserEntity instructor = event.getInstructor();
        UserEntity student = event.getStudent();

        String instructorName = buildUserFullName(instructor);
        String studentName = buildUserFullName(student);

        return new GetEventListResultItem(event.getId(), event.getVersion(), event.getName(), event.getDescription(),
                event.getLocation(), instructorName, studentName, event.getType(), event.getStatus(), event.getPrice(), event.getCreatedAt(),
                event.getSingleEventStart(), event.getSingleEventEnd(), event.getCyclicDayOfWeek(), event.getCyclicEventStart(),
                event.getCyclicEventDuration(), event.getCyclicStartBoundary(), event.getCyclicEndBoundary(), event.getCyclicAbsenceEvent(),
                event.getCyclicAbsenceEventName(), event.getCyclicAbsenceEventDescription(), realizations);
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
