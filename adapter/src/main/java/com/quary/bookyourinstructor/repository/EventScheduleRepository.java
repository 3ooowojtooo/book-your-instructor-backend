package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import com.quary.bookyourinstructor.model.event.EventScheduleType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public interface EventScheduleRepository extends CrudRepository<EventScheduleEntity, Integer> {

    @Modifying
    @Query("update EventScheduleEntity s set s.status = ?6, s.type = ?7, s.eventName = ?8, s.eventDescription = ?9, s.eventLocation = ?10, " +
            "s.eventPrice = ?11 where s.event.id = ?1 and s.eventRealization.id = ?2 and s.student.id = ?3 " +
            "and s.status = ?4 and s.owner = ?5")
    void update(Integer eventId, Integer eventRealization, Integer studentId, EventScheduleStatus status,
                EventScheduleOwner owner, EventScheduleStatus newStatus, EventScheduleType newType, String eventName, String eventDescription,
                String eventLocation, BigDecimal eventPrice);

    @Modifying
    @Query("delete from EventScheduleEntity s where s.event.id = ?1 and s.eventRealization.id in ?2 and " +
            "s.student.id = ?3 and s.status = ?4")
    void delete(Integer eventId, Collection<Integer> realizationIds, Integer studentId, EventScheduleStatus status);
}
