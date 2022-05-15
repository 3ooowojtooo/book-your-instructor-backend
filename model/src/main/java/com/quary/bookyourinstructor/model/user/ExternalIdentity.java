package com.quary.bookyourinstructor.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@EqualsAndHashCode
@ToString(exclude = "id")
public class ExternalIdentity {

    private final String id;
    private final ExternalIdentityProvider provider;

    public ExternalIdentity(String id, ExternalIdentityProvider provider) {
        checkArgument(isNotBlank(id), "External id cannot be blank");
        checkNotNull(provider, "External id provider cannot be null");
        this.id = id;
        this.provider = provider;
    }
}
