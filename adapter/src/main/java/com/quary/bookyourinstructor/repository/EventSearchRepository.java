package com.quary.bookyourinstructor.repository;

import bookyourinstructor.usecase.event.search.data.DateRangeFilter;
import bookyourinstructor.usecase.event.search.data.EventTypeFilter;
import bookyourinstructor.usecase.event.search.data.TextSearchFilter;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.filter.search.TextSearchCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventSearchRepository {

    private final EntityManager entityManager;

    public List<SearchEventsResultItem> searchEvents(DateRangeFilter dateRange, TextSearchFilter text, EventTypeFilter eventType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<EventEntity> eventModel = metamodel.entity(EventEntity.class);

        Root<EventEntity> event = cq.from(EventEntity.class);
        ListJoin<EventEntity, EventRealizationEntity> realization = event.join(eventModel.getList("realizations", EventRealizationEntity.class));
        Join<EventEntity, UserEntity> instructor = event.join(eventModel.getSingularAttribute("instructor", UserEntity.class));

        Predicate eventFreePredicate = buildFreeEventsPredicate(cb, event);
        Optional<Predicate> dateRangePredicate = buildDateRangePredicate(cb, realization, dateRange);
        Optional<Predicate> textPredicate = buildTextPredicate(cb, event, instructor, text);
        Optional<Predicate> eventTypePredicate = buildEventTypePredicate(cb, event, eventType);

        Predicate[] mergedPredicates = mergePredicates(eventFreePredicate, dateRangePredicate, textPredicate, eventTypePredicate);
        cq.where(cb.and(mergedPredicates));
        cq.orderBy(buildOrder(cb, event, realization));

        TypedQuery<EventEntity> query = entityManager.createQuery(cq);
        List<EventEntity> result = query.getResultList();

        return toResultItems(result);
    }

    private Predicate buildFreeEventsPredicate(CriteriaBuilder cb, Root<EventEntity> event) {
        return cb.equal(event.get("status"), EventStatus.FREE);
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

        List<String> tokens = Arrays.stream(textFilter.getSearchText().split(" "))
                .map(String::toLowerCase)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        if (tokens.isEmpty()) {
            return Optional.empty();
        }

        List<Predicate> categoryPredicates = new ArrayList<>();

        if (textFilter.isCategorySelected(TextSearchCategory.NAME)) {
            Predicate[] namePredicates = tokens.stream()
                    .map(token -> cb.like(cb.lower(event.get("name")), '%' + token + '%'))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[]{});

            categoryPredicates.add(cb.and(namePredicates));
        }

        if (textFilter.isCategorySelected(TextSearchCategory.DESCRIPTION)) {
            Predicate[] descriptionPredicates = tokens.stream()
                    .map(token -> cb.like(cb.lower(event.get("description")), '%' + token + '%'))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[]{});

            categoryPredicates.add(cb.and(descriptionPredicates));
        }

        if (textFilter.isCategorySelected(TextSearchCategory.LOCATION)) {
            Predicate[] locationPredicates = tokens.stream()
                    .map(token -> cb.like(cb.lower(event.get("location")), '%' + token + '%'))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[]{});

            categoryPredicates.add(cb.and(locationPredicates));
        }

        if (textFilter.isCategorySelected(TextSearchCategory.INSTRUCTOR_NAME)) {
            Predicate[] instructorNamePredicates = tokens.stream()
                    .map(token -> cb.or(
                            cb.like(cb.lower(instructor.get("name")), '%' + token + '%'),
                            cb.like(cb.lower(instructor.get("surname")), '%' + token + '%')
                    ))
                    .collect(Collectors.toList())
                    .toArray(new Predicate[]{});

            categoryPredicates.add(cb.and(instructorNamePredicates));
        }

        return Optional.of(cb.or(categoryPredicates.toArray(new Predicate[]{})));
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
                cb.asc(realization.get("start")),
                cb.asc(event.get("id"))
        );
    }

    private List<SearchEventsResultItem> toResultItems(List<EventEntity> events) {
        return events.stream()
                .map(this::mapToResultItem)
                .collect(Collectors.toList());
    }

    private SearchEventsResultItem mapToResultItem(EventEntity event) {
        UserEntity instructor = event.getInstructor();
        String instructorName = instructor.getName() + " " + instructor.getSurname();
        return new SearchEventsResultItem(event.getId(), event.getVersion(), event.getName(),
                event.getDescription(), event.getLocation(), instructorName, event.getType(),
                event.getSingleEventStart(), event.getSingleEventEnd(), event.getCyclicDayOfWeek(),
                event.getCyclicEventStart(), event.getCyclicEventDuration());
    }
}
