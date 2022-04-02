package com.quary.bookyourinstructor.configuration.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Component
public class SimpleUserDetailsService implements UserDetailsService {

    private static final Map<String, String> USERS = Map.of(
      "foo", "foo"
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(USERS.get(username))
                .map(password -> new User(username, password, emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException(username + " user not found"));
    }
}
