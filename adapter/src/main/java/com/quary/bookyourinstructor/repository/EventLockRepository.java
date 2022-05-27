package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventLockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLockRepository extends CrudRepository<EventLockEntity, Integer> {
}
