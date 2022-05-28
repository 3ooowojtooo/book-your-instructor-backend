package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRealizationRepository extends CrudRepository<EventRealizationEntity, Integer> {

    @Modifying
    @Query("update EventRealizationEntity r set r.student.id = ?1 where r.event.id = ?2")
    void setStudentIdForEventRealizations(Integer studentId, Integer eventId);
}
