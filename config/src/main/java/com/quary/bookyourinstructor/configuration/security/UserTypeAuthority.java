package com.quary.bookyourinstructor.configuration.security;

import com.quary.bookyourinstructor.model.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class UserTypeAuthority implements GrantedAuthority {

    private final UserType userType;

    @Override
    public String getAuthority() {
        return userType.name();
    }
}
