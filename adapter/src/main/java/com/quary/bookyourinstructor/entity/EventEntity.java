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
    private LocalDate cyclicStartBoundary;

    @Column(name = "cyclic_end_boundary")
    private LocalDate cyclicEndBoundary;

    @Column(name = "cyclic_absence_event", nullable = false)
    private Boolean cyclicAbsenceEvent;

    @Column(name = "cyclic_absence_event_name")
    private String cyclicAbsenceEventName;

    @Column(name = "cyclic_absence_event_description")
    private String cyclicAbsenceEventDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    List<EventRealizationEntity> realizations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    List<EventLockEntity> locks = new ArrayList<>();
}
