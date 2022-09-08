package com.quary.bookyourinstructor.entity;

import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import com.quary.bookyourinstructor.model.event.EventScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "event_schedule", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventScheduleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_realization_id", nullable = false)
    private EventRealizationEntity eventRealization;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "instructor_id", nullable = false)
    private UserEntity instructor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventScheduleStatus status;

    @Column(name = "owner", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventScheduleOwner owner;
}
