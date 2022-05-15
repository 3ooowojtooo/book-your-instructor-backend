package com.quary.bookyourinstructor.model.authentication;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class EmailAndPassword {

    private final String email;
    private final String password;

    public EmailAndPassword(String email, String password) {
        checkArgument(isNotBlank(email), "Email cannot be blank");
        checkArgument(isNotBlank(password), "Password cannot be blank");
        this.email = email;
        this.password = password;
    }
}