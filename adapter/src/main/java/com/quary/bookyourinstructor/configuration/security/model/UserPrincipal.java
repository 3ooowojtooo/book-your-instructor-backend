package com.quary.bookyourinstructor.configuration.security.model;

import bookyourinstructor.usecase.event.common.port.UserData;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;

@Getter
public class UserPrincipal implements UserDetails, UserData {

    private final Integer id;
    private final String email;
    private final String password;
    private final UserType userType;
    private final List<GrantedAuthority> authorities;

    public UserPrincipal(final UserEntity userEntity) {
        Objects.requireNonNull(userEntity, "UserEntity cannot be null");
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.userType = userEntity.getType();
        this.authorities = List.of(new UserTypeAuthority(userEntity.getType()));
    }

    @Override
    public boolean isInstructor() {
        return this.userType == UserType.INSTRUCTOR;
    }

    @Override
    public boolean isStudent() {
        return this.userType == UserType.STUDENT;
    }

    @Override
    public boolean isUndeclared() {
        return this.userType == UserType.UNDECLARED;
    }

    @Override
    public UserType getType() {
        return userType;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
