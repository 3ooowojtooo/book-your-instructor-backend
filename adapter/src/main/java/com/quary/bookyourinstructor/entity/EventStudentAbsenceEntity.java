package com.quary.bookyourinstructor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "event_student_absence", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventStudentAbsenceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_realization_id")
    private EventRealizationEntity eventRealization;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "student_id")
    private UserEntity student;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "event_location", nullable = false)
    private String eventLocation;

    @Column(name = "event_start_timestamp", nullable = false)
    private Instant eventStart;

    @Column(name = "event_end_timestamp", nullable = false)
    private Instant eventEnd;
}
