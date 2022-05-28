package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Integer> {

    Optional<EventEntity> findByIdAndVersion(Integer id, Integer version);

    @Query(value = "select * from event where id = ?1 for share", nativeQuery = true)
    Optional<EventEntity> findByIdAndLockForShare(Integer id);
}