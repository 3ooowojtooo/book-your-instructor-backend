package com.quary.bookyourinstructor.entity;

import com.quary.bookyourinstructor.model.user.ExternalIdentityProvider;
import com.quary.bookyourinstructor.model.user.UserOrigin;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "origin", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOrigin origin;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type = UserType.UNDECLARED;

    @Column(name = "external_id")
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "external_id_provider")
    private ExternalIdentityProvider externalIdProvider;

    @OneToMany(mappedBy = "instructor", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventEntity> events = new ArrayList<>();

    @OneToMany(mappedBy = "student", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EventRealizationEntity> eventRealizations = new ArrayList<>();
}
