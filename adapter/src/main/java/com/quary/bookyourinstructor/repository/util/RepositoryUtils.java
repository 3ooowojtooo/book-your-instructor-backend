package com.quary.bookyourinstructor.repository.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryUtils {

    @SafeVarargs
    public static Predicate[] mergePredicates(Predicate predicate, Optional<Predicate>... optionalPredicates) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(predicate);
        Arrays.stream(optionalPredicates)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(predicates::add);
        return predicates.toArray(new Predicate[]{});
    }
}
