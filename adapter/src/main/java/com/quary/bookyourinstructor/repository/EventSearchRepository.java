package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.EventTypeFilter;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.filter.search.TextSearchCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventSearchRepository {

    private final EntityManager entityManager;

    public List<SearchEventsResultItem> searchEvents(DateRangeFilter dateRange, TextSearchFilter text, EventTypeFilter eventType, Instant now) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchQueryResult> cq = cb.createQuery(SearchQueryResult.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventEntity> eventModel = metamodel.entity(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        ListJoin<EventEntity, EventRealizationEntity> realization = event.join(eventModel.getList("realizations", EventRealizationEntity.class));
        Join<EventEntity, UserEntity> instructor = event.join(eventModel.getSingularAttribute("instructor", UserEntity.class));

        Predicate eventFreePredicate = buildAvailableEventsPredicate(cb, event, realization, now);
        Optional<Predicate> dateRangePredicate = buildDateRangePredicate(cb, realization, dateRange);
        Optional<Predicate> textPredicate = buildTextPredicate(cb, event, instructor, text);
        Optional<Predicate> eventTypePredicate = buildEventTypePredicate(cb, event, eventType);
        Predicate[] mergedPredicates = mergePredicates(eventFreePredicate, dateRangePredicate, textPredicate, eventTypePredicate);

        cq.multiselect(event, cb.min(realization.get("start")));
        cq.where(cb.and(mergedPredicates));
        cq.groupBy(event.get("id"));
        cq.orderBy(buildOrder(cb, event, realization));

        TypedQuery<SearchQueryResult> query = entityManager.createQuery(cq);
        List<SearchQueryResult> result = query.getResultList();

        return toResultItems(result, now);
    }

    private Predicate buildAvailableEventsPredicate(CriteriaBuilder cb, Root<EventEntity> event, ListJoin<EventEntity,
            EventRealizationEntity> realization, Instant now) {
        return cb.and(
                cb.equal(event.get("status"), EventStatus.FREE),
                cb.equal(realization.get("status"), EventRealizationStatus.ACCEPTED),
                cb.greaterThan(realization.get("start"), now)
        );
    }

    private Optional<Predicate> buildDateRangePredicate(CriteriaBuilder cb, ListJoin<EventEntity, EventRealizationEntity> realization,
                                                        DateRangeFilter dateRangeFilter) {
        return Optional.ofNullable(dateRangeFilter)
                .map(filter -> cb.between(realization.get("start"), filter.getFrom(), filter.getTo()));
    }

    private Optional<Predicate> buildTextPredicate(CriteriaBuilder cb, Root<EventEntity> event, Join<EventEntity,
            UserEntity> instructor, TextSearchFilter textFilter) {
        if (textFilter == null) {
            return Optional.empty();
        }

        List<String> tokens = textFilter.getSearchTokens();

        List<Predicate> resultPredicates = tokens.stream()
                .map(token -> buildTokenPredicate(cb, event, instructor, textFilter, token))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (resultPredicates.isEmpty()) {
            return Optional.empty();
        }

        Predicate[] resultPredicatesArray = resultPredicates.toArray(new Predicate[]{});
        return Optional.of(cb.and(resultPredicatesArray));
    }

    private Optional<Predicate> buildTokenPredicate(CriteriaBuilder cb, Root<EventEntity> event,
                                                    Join<EventEntity, UserEntity> instructor,
                                                    TextSearchFilter textSearchFilter,
                                                    String token) {
        List<Predicate> tokenPredicates = new ArrayList<>();

        if (textSearchFilter.isCategorySelected(TextSearchCategory.NAME)) {
            Predicate namePredicate = buildNameTokenPredicate(cb, event, token);
            tokenPredicates.add(namePredicate);
        }

        if (textSearchFilter.isCategorySelected(TextSearchCategory.DESCRIPTION)) {
            Predicate descriptionPredicate = buildDescriptionPredicate(cb, event, token);
            tokenPredicates.add(descriptionPredicate);
        }

        if (textSearchFilter.isCategorySelected(TextSearchCategory.LOCATION)) {
            Predicate locationPredicate = buildLocationPredicate(cb, event, token);
            tokenPredicates.add(locationPredicate);
        }

        if (textSearchFilter.isCategorySelected(TextSearchCategory.INSTRUCTOR_NAME)) {
            Predicate instructorNamePredicate = buildInstructorNamePredicate(cb, instructor, token);
            tokenPredicates.add(instructorNamePredicate);
        }

        if (tokenPredicates.isEmpty()) {
            return Optional.empty();
        }

        Predicate[] tokenPredicatesArray = tokenPredicates.toArray(new Predicate[]{});
        return Optional.of(cb.or(tokenPredicatesArray));
    }

    private Predicate buildNameTokenPredicate(CriteriaBuilder cb, Root<EventEntity> event, String token) {
        return cb.like(cb.lower(event.get("name")), '%' + token + '%');
    }

    private Predicate buildDescriptionPredicate(CriteriaBuilder cb, Root<EventEntity> event, String token) {
        return cb.like(cb.lower(event.get("description")), '%' + token + '%');
    }

    private Predicate buildLocationPredicate(CriteriaBuilder cb, Root<EventEntity> event, String token) {
        return cb.like(cb.lower(event.get("location")), '%' + token + '%');
    }

    private Predicate buildInstructorNamePredicate(CriteriaBuilder cb, Join<EventEntity, UserEntity> instructor, String token) {
        return cb.or(
                cb.like(cb.lower(instructor.get("name")), '%' + token + '%'),
                cb.like(cb.lower(instructor.get("surname")), '%' + token + '%')
        );
    }

    private Optional<Predicate> buildEventTypePredicate(CriteriaBuilder cb, Root<EventEntity> event, EventTypeFilter eventTypeFilter) {
        if (eventTypeFilter == null || eventTypeFilter.allEventTypesSelected())
            return Optional.empty();

        return Optional.of(cb.equal(event.get("type"), eventTypeFilter.getEventType()));
    }

    @SafeVarargs
    private static Predicate[] mergePredicates(Predicate predicate, Optional<Predicate>... optionalPredicates) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(predicate);
        Arrays.stream(optionalPredicates)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(predicates::add);
        return predicates.toArray(new Predicate[]{});
    }

    private List<Order> buildOrder(CriteriaBuilder cb, Root<EventEntity> event, ListJoin<EventEntity, EventRealizationEntity> realization) {
        return List.of(
                cb.asc(cb.min(realization.get("start"))),
                cb.asc(event.get("id"))
        );
    }

    private List<SearchEventsResultItem> toResultItems(List<SearchQueryResult> results, Instant now) {
        return results.stream()
                .map(SearchQueryResult::getEvent)
                .map(event -> mapToResultItem(event, now))
                .collect(Collectors.toList());
    }

    private SearchEventsResultItem mapToResultItem(EventEntity event, Instant now) {
        UserEntity instructor = event.getInstructor();
        String instructorName = instructor.getName() + " " + instructor.getSurname();
        long futureRealizations = computeNotStartedRealizationsAmount(event, now);
        return new SearchEventsResultItem(event.getId(), event.getVersion(), event.getName(),
                event.getDescription(), event.getLocation(), instructorName, event.getType(), futureRealizations,
                event.getPrice(), event.getCreatedAt(), event.getSingleEventStart(), event.getSingleEventEnd(), event.getCyclicDayOfWeek(),
                event.getCyclicEventStart(), event.getCyclicEventDuration(),
                event.getCyclicStartBoundary(), event.getCyclicEndBoundary());
    }

    private long computeNotStartedRealizationsAmount(EventEntity event, Instant now) {
        return event.getRealizations().stream()
                .filter(realization -> realization.getStart().isAfter(now) && realization.getStatus() == EventRealizationStatus.ACCEPTED)
                .count();
    }

    @RequiredArgsConstructor
    private static class SearchQueryResult {
        @Getter
        private final EventEntity event;
        private final Instant minimumRealizationStart;
    }
}
