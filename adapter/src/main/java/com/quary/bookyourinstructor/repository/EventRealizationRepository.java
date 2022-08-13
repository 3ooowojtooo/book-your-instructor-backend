package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
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
    @Query("update EventRealizationEntity r set r.status = ?1 where r.id in ?2")
    void setStatusForEventRealizations(EventRealizationStatus status, Collection<Integer> ids);

    @Modifying
    @Query("update EventRealizationEntity r set r.status = ?2 where r.event.id = ?3 and r.status = ?1")
    void setStatusForEventRealizationsWithStatus(EventRealizationStatus currentStatus, EventRealizationStatus newStatus,
                                                 Integer eventId);

    @Modifying
    @Query("update EventRealizationEntity r set r.status = ?1 where r.id = ?2")
    void setStatusForEventRealization(EventRealizationStatus status, Integer id);

    @Query("select r from EventRealizationEntity r where r.event.id = ?1")
    List<EventRealizationEntity> findAllByEventId(final Integer eventId);

    @Query("select r from EventRealizationEntity r where r.event.id = ?1 and r.start > ?2 and r.status = ?3")
    List<EventRealizationEntity> findAllByEventIdStartingAfterAndWithStatus(final Integer eventId, final Instant now, final EventRealizationStatus status);

    @Query(value = "select * from event_realization where event_id = ?1 and status in ?2 and start_timestamp > ?3 order by start_timestamp for update nowait", nativeQuery = true)
    List<EventRealizationEntity> findAllByEventIdAndStatusStartingAfterOrderByStartAscWithLockForUpdate(final Integer eventId, Collection<String> statuses, final Instant startThreshold);

    @Query("select r from EventRealizationEntity r where r.event.id = ?1 and r.status = ?2 and r.student.id = ?3")
    List<EventRealizationEntity> findAllRealizationWithStatusAndStudentId(Integer eventId, EventRealizationStatus status, Integer studentId);

    @Modifying
    void deleteAllByEventId(Integer eventId);

    @Query(value = "select * from event_realization where id = ?1 for update nowait", nativeQuery = true)
    Optional<EventRealizationEntity> findByIdWithLockForUpdate(Integer id);
}
