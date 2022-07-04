package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventLockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface EventLockRepository extends CrudRepository<EventLockEntity, Integer> {

    boolean existsByEventIdAndExpirationTimeGreaterThan(Integer eventId, Instant now);

    void deleteByEventIdAndExpirationTimeLessThanEqual(Integer eventId, Instant now);
}
