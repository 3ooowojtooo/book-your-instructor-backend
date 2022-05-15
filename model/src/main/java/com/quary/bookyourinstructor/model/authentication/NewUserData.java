package com.quary.bookyourinstructor.model.authentication;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class NewUserData {

    private final String email;
    private final String password;
    private final String name;
    private final String surname;
    private final NewUserType type;

    public NewUserData(String email, String password, String name, String surname, NewUserType type) {
        checkArgument(isNotBlank(email), "New user email cannot be blank");
        checkArgument(isNotBlank(password), "New user password cannot be blank");
        checkArgument(isNotBlank(name), "New user name cannot be blank");
        checkArgument(isNotBlank(surname), "New user surname cannot be blank");
        checkNotNull(type, "New user type cannot be null");
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.type = type;
    }
}
