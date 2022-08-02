package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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

    @Query("select r from EventRealizationEntity r where r.event.id = ?1 and r.start > ?2 and r.status = ?3")
    List<EventRealizationEntity> findAllByEventIdStartingAfterAndWithStatus(final Integer eventId, final Instant now, final EventRealizationStatus status);

    @Query(value = "select * from event_realization where event_id = ?1 and start_timestamp > ?2 order by start_timestamp for update nowait", nativeQuery = true)
    List<EventRealizationEntity> findAllByEventIdStartingAfterOrderByStartAscWithLockForUpdate(final Integer eventId, final Instant startThreshold);

    @Modifying
    void deleteAllByEventId(Integer eventId);

    @Query(value = "select * from event_realization where id = ?1 for update nowait", nativeQuery = true)
    Optional<EventRealizationEntity> findByIdWithLockForUpdate(Integer id);
}
