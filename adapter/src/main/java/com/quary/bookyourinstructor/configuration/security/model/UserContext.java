package com.quary.bookyourinstructor.configuration.security.model;

import com.quary.bookyourinstructor.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;

@Getter
public class UserContext implements UserDetails {

    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserContext(final UserEntity userEntity) {
        Objects.requireNonNull(userEntity, "UserEntity cannot be null");
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.authorities = List.of(new UserTypeAuthority(userEntity.getType()));
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
