package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.model.event.EventStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Integer> {

    Optional<EventEntity> findByIdAndVersion(Integer id, Integer version);

    @Query(value = "select * from event where id = ?1 for share nowait", nativeQuery = true)
    Optional<EventEntity> findByIdAndLockForShare(Integer id);

    @Query(value = "select * from event where id = ?1 for update nowait", nativeQuery = true)
    Optional<EventEntity> findByIdAndLockForUpdate(Integer id);

    @Modifying
    @Query(value = "update EventEntity e set e.status = ?2, e.version = e.version + 1 where e.id = ?1")
    void setStatusByIdAndIncrementVersion(Integer id, EventStatus eventStatus);

    @Modifying
    @Query(value = "update EventEntity e set e.version = e.version + 1 where e.id = ?1")
    void incrementVersion(Integer id);

    @Modifying
    @Query(value = "update EventEntity e set e.cyclicStartBoundary = ?2, e.cyclicEndBoundary = ?3 where e.id = ?1")
    void updateCyclicEventBoundaries(Integer eventId, LocalDate startBoundary, LocalDate endBoundary);
}