package com.quary.bookyourinstructor.entity;

import com.quary.bookyourinstructor.model.event.EventStatus;
import com.quary.bookyourinstructor.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private UserEntity instructor;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "creation_timestamp", nullable = false)
    private Instant createdAt;

    @Column(name = "single_start_timestamp")
    private LocalDateTime singleEventStart;

    @Column(name = "single_end_timestamp")
    private LocalDateTime singleEventEnd;

    @Column(name = "cyclic_start_time")
    private LocalTime cyclicEventStart;

    @Column(name = "cyclic_duration")
    private Duration cyclicEventDuration;

    @Column(name = "cyclic_day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek cyclicDayOfWeek;

    @Column(name = "cyclic_start_boundary")
    private LocalDateTime cyclicStartBoundary;

    @Column(name = "cyclic_end_boundary")
    private LocalDateTime cyclicEndBoundary;

    @Column(name = "cyclic_absence_event")
    private Boolean cyclicAbsenceEvent;

    @Column(name = "cyclic_absence_event_name")
    private String cyclicAbsenceEventName;

    @Column(name = "cyclic_absence_event_description")
    private String cyclicAbsenceEventDescription;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "cyclic_absence_event_parent_id")
    private EventEntity cyclicEventAbsenceParentEvent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cyclicEventAbsenceParentEvent", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventEntity> cyclicEventAbsenceChildEvent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventRealizationEntity> realizations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventLockEntity> locks = new ArrayList<>();
}
