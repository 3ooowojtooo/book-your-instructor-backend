package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRealizationRepository extends CrudRepository<EventRealizationEntity, Integer> {

    @Modifying
    @Query("update EventRealizationEntity r set r.student.id = ?1 where r.event.id = ?2")
    void setStudentIdForEventRealizations(Integer studentId, Integer eventId);

    @Modifying
    @Query("update EventRealizationEntity r set r.student.id = ?1 where r.id = ?2")
    void setStudentIdForEventRealization(Integer studentId, Integer id);

    @Modifying
    @Query("update EventRealizationEntity r set r.status = ?1 where r.event.id = ?2")
    void setStatusForEventRealizations(EventRealizationStatus status, Integer eventId);

    @Modifying
    @Query("update EventRealizationEntity r set r.status = ?1 where r.id = ?2")
    void setStatusForEventRealization(EventRealizationStatus status, Integer id);

    @Query("select r from EventRealizationEntity r where r.event.id = ?1")
    List<EventRealizationEntity> findAllByEventId(final Integer eventId);

    @Modifying
    void deleteAllByEventId(Integer eventId);

    @Query(value = "select * from event_realization where id = ?1 for update", nativeQuery = true)
    Optional<EventRealizationEntity> findByIdWithLockForUpdate(Integer id);
}
