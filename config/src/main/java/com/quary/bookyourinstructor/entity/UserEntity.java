package com.quary.bookyourinstructor.entity;

import com.quary.bookyourinstructor.model.user.ExternalIdentityProvider;
import com.quary.bookyourinstructor.model.user.UserOrigin;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
}
