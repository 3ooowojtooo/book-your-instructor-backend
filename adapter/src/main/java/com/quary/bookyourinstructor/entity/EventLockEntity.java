package com.quary.bookyourinstructor.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "event_lock", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventLockEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private EventEntity event;

    @Column(name = "event_version", nullable = false)
    private Integer eventVersion;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "expiration_timestamp", nullable = false)
    private Instant expirationTime;
}
