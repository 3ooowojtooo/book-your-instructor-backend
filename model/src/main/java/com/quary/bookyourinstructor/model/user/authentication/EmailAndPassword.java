package com.quary.bookyourinstructor.model.user.authentication;

import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
public class EmailAndPassword {

    private final String email;
    private final String password;

    public EmailAndPassword(String email, String password) {
        this.email = requireNonNull(email);
        this.password = requireNonNull(password);
    }
}