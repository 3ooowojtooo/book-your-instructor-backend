package com.quary.bookyourinstructor.configuration.security;

import bookyourinstructor.usecase.authentication.AuthenticationStore;
import com.quary.bookyourinstructor.model.user.EmailAndPassword;
import com.quary.bookyourinstructor.model.user.exception.InvalidEmailOrPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationStoreImpl implements AuthenticationStore {

    private final AuthenticationManager authenticationManager;

    @Override
    public void tryAuthenticateOrThrow(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException {
        try {
            final Authentication authentication = buildAuthentication(emailAndPassword);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new InvalidEmailOrPasswordException();
        }
    }

    private Authentication buildAuthentication(final EmailAndPassword emailAndPassword) {
        return new UsernamePasswordAuthenticationToken(emailAndPassword.getEmail(), emailAndPassword.getPassword());
    }
}
