package com.quary.bookyourinstructor.model.user;

import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
public class EmailAndPassword {

    private final String email;
    private final String password;

    public EmailAndPassword(String email, String password) {
        requireNonNull(email);
        requireNonNull(password);
        this.email = email;
        this.password = password;
    }
}
