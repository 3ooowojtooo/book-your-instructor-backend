package com.quary.bookyourinstructor.repository;

import com.quary.bookyourinstructor.entity.EventScheduleEntity;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EventScheduleRepository extends CrudRepository<EventScheduleEntity, Integer> {

    @Modifying
    @Query("update EventScheduleEntity s set s.status = ?5 where s.event.id = ?1 and s.eventRealization.id = ?2 " +
            "and s.status = ?3 and s.owner = ?4")
    void update(Integer eventId, Integer eventRealization, EventScheduleStatus status, EventScheduleOwner owner,
                EventScheduleStatus newStatus);

    @Modifying
    @Query("delete from EventScheduleEntity s where s.event.id = ?1 and s.eventRealization.id in ?2 and s.status = ?3")
    void delete(Integer eventId, Collection<Integer> realizationIds, EventScheduleStatus status);
}
