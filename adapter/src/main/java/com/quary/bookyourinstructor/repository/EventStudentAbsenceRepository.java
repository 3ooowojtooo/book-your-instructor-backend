package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventStudentAbsenceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EventStudentAbsenceRepository extends CrudRepository<EventStudentAbsenceEntity, Integer> {

    @Query("select case when count(a) > 0 then true else false end from EventStudentAbsenceEntity a where " +
            "a.eventRealization.id = ?1 and a.student.id = ?2")
    boolean existsByRealizationIdAndStudentId(Integer eventRealizationId, Integer studentId);
}
