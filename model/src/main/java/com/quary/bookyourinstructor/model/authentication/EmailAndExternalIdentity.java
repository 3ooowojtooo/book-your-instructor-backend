package com.quary.bookyourinstructor.model.authentication;

import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class EmailAndExternalIdentity {

    private final String email;
    private final ExternalIdentity externalIdentity;

    public EmailAndExternalIdentity(String email, ExternalIdentity externalIdentity) {
        checkArgument(isNotBlank(email), "Email cannot be blank");
        checkNotNull(externalIdentity, "External identity cannot be null");
        this.email = email;
        this.externalIdentity = externalIdentity;
    }
}
