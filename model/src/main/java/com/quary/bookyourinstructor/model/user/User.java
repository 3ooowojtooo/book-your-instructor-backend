package com.quary.bookyourinstructor.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.validator.EmailValidator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@EqualsAndHashCode
@ToString(exclude = "password")
public class User {
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final Integer id;
    private final String email;
    private final String password;
    private final UserOrigin origin;
    private final UserType type;
    private final ExternalIdentity externalIdentity;

    public User(Integer id, String email, String password, UserOrigin origin, UserType type, ExternalIdentity externalIdentity) {
        validateConstructorArgs(email, password, origin, type, externalIdentity);
        this.id = id;
        this.email = email;
        this.password = password;
        this.origin = origin;
        this.type = type;
        this.externalIdentity = externalIdentity;
    }

    private static void validateConstructorArgs(String email, String password, UserOrigin origin, UserType type, ExternalIdentity externalIdentity) {
        checkArgument(EMAIL_VALIDATOR.isValid(email), "Email is invalid");
        checkNotNull(origin, "User origin cannot be null");
        checkNotNull(type, "User type cannot be null");
        if (UserOrigin.CREDENTIALS.equals(origin)) {
            checkArgument(isNotBlank(password), "Password cannot be blank for CREDENTIALS user");
        }
        if (UserOrigin.EXTERNAL.equals(origin)) {
            checkNotNull(externalIdentity, "External identity cannot be null for EXTERNAL user");
        }
    }

    public static User createNewCredentialsUser(String email, String password, UserType type) {
        return new User(null, email, password, UserOrigin.CREDENTIALS, type, null);
    }

    public static User createNewExternalUser(String email, UserType type, ExternalIdentity externalIdentity) {
        return new User(null, email, null, UserOrigin.EXTERNAL, type, externalIdentity);
    }
}
