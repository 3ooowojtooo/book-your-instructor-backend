package com.quary.bookyourinstructor.configuration.security;

import bookyourinstructor.usecase.authentication.credentials.PasswordEncoderHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderHelperImpl implements PasswordEncoderHelper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
