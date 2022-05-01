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
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOrigin origin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type = UserType.UNDECLARED;

    private String externalId;

    @Enumerated(EnumType.STRING)
    private ExternalIdentityProvider externalIdProvider;
}
