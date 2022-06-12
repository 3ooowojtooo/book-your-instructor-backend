package com.quary.bookyourinstructor.entity;

import com.quary.bookyourinstructor.model.event.EventRealizationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_realization", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventRealizationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventRealizationStatus status;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "student_id")
    private UserEntity student;

    @Column(name = "start_timestamp", nullable = false)
    private Instant start;

    @Column(name = "end_timestamp", nullable = false)
    private Instant end;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventRealization", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventStudentAbsenceEntity> studentAbsences = new ArrayList<>();

}
